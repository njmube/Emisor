package com.cubetech.facturador.emisor.domain.cuenta;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.security.auth.x500.X500Principal;
import javax.validation.ValidationException;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.ssl.PKCS8Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cubetech.facturador.emisor.domain.common.AlgoritmosFirma;
import com.cubetech.facturador.emisor.domain.common.PersonaFiscal;
import com.cubetech.facturador.emisor.domain.common.RazonSocial;
import com.cubetech.facturador.emisor.domain.common.Rfc;
import com.cubetech.facturador.emisor.domain.common.Vigencia;
import com.cubetech.facturador.emisor.domain.shared.ValueObject;

import lombok.Data;
import lombok.ToString;

@Data
@Embeddable
@ToString(exclude="password")
public class Certificado implements ValueObject<Certificado> {
	
	private final static Logger logger = LoggerFactory.getLogger(Certificado.class);

	public static final String SAT = "Servicio de Administración Tributaria";
	
	private static final String expRFC =  "^.*RFC=(([A-Z&Ñ]{3,4})(\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01]))([A-Z\\d]{2})([A\\d])).*?$";
	private static final Pattern patronRFC = Pattern.compile(expRFC);
	
	private static final String expRS = "^.*O=(.*),\\sOID.*?$";
	private static final Pattern patronRS = Pattern.compile(expRS);
	
	private static final String expSAT = "^.*O=(.*),\\s?CN.*?$";
	private static final Pattern patronSAT = Pattern.compile(expSAT);
	
	public static final  String cadenaVerificacion = "cadenadeVerificacion_|3.3|";
	
	private String serie;
	private Vigencia	vigencia;
	@AttributeOverrides({
		@AttributeOverride(name="rfc", column=@Column(nullable= true, name= "rfc_certificado"))
	})
	private Rfc rfc;
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="nombre", column=@Column(name="rs_certificado"))
	})
	private RazonSocial razonSocial;
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="correlacion", column=@Column(name="publico_id", nullable= false))
	})
	private ArchivoCuenta publico;
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="correlacion", column=@Column(name="privado_id", nullable= false))
	})
	private ArchivoCuenta privado;
	private String password;
	
	public Certificado(){
		this.vigencia = new Vigencia();
	}
	
	public void asignaPublico(ArchivoCuenta pub) throws CertificateException{
		extraeInformacion(pub);
		this.publico = pub; 
	}
	
	public void extraeInformacion() throws CertificateException{
		extraeInformacion(this.publico);
	}
	
	private void extraeInformacion(ArchivoCuenta pub) throws CertificateException{
		InputStream pubcer = new ByteArrayInputStream(pub.getContent());
		X509Certificate certificado = null;			
		try{
		 certificado = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(pubcer);
		 
		 if(ValidaIsser(certificado.getIssuerX500Principal())){
			 
			 this.serie = new String(certificado.getSerialNumber().toByteArray());
	     this.vigencia.setFechaInicial(certificado.getNotBefore());
	     this.vigencia.setFechaFinal(certificado.getNotAfter());
	     this.rfc = ConsultaRFC(certificado.getSubjectX500Principal());
	     this.razonSocial = ConsultaRazonSocial(certificado.getSubjectX500Principal());
		 }
		 
		}catch(Exception e){
			logger.error("asignaPublico: " + pub.getCorrelacion(), e);
			throw e;
		}
	}
	
	public static  Rfc ConsultaRFC(X500Principal subject){
		Map<String, String> oidMap = new HashMap<String, String>();
		Rfc rfc = null;
		oidMap.put("2.5.4.45", "RFC");
		Matcher matcher = patronRFC.matcher(subject.getName( X500Principal.RFC1779 , oidMap));
    try{
    	if(matcher.matches()){
    		rfc = new Rfc(matcher.group(1));
    	}
    }catch(IllegalArgumentException ae){
   	 	
    	rfc = new Rfc();
    }
    return rfc;
	}
	
	public static  RazonSocial ConsultaRazonSocial(X500Principal subject){
		RazonSocial rs = null;
		
		Matcher matcher = patronRS.matcher(subject.getName(X500Principal.RFC1779));
    try{
    	if(matcher.matches()){
    		rs = new RazonSocial(matcher.group(1));
    	}
    }catch(IllegalArgumentException ae){
    	rs = new RazonSocial();
    }
    return rs;
	}
	
	public static String ConsultaEmisor(X500Principal isser){
		String ret = "";
		
		Matcher matcher = patronSAT.matcher(isser.getName());
 		if(matcher.matches()){
 			ret = matcher.group(1);
 		}
		
		return ret;
	}
	
	public static boolean ValidaIsser(X500Principal isser){
		boolean ret = true;
		String emisor = ConsultaEmisor(isser);
		
		ret = ret && ((emisor.length() == 0) || (emisor.length() > 0 && emisor.equals(SAT)));
		
		return ret;
	}
	
	public byte[] timbra(String datos) throws GeneralSecurityException{
		return timbra(datos, AlgoritmosFirma.SHA256RSA);
	}
	
	public byte[] timbra(String datos, AlgoritmosFirma algoritmo) throws GeneralSecurityException{
		byte[] signed = null;
		PKCS8Key pkcs8= null;
		byte[] decrypted = null;
		byte[] datosByte = null;
		PKCS8EncodedKeySpec spec = null;
		PrivateKey pk = null;
		Signature rsa = null;
		String encoded;
		
		if(this.privado != null && this.privado.getContent().length > 0 && this.password!= null && this.password.length() > 0){
			try{
				pkcs8 = new PKCS8Key( this.privado.getContent(), this.password.toCharArray());
			  decrypted = pkcs8.getDecryptedBytes();
			  spec = new PKCS8EncodedKeySpec( decrypted );
			 
			  if ( pkcs8.isDSA() )
			  {
			    pk = KeyFactory.getInstance( "DSA" ).generatePrivate( spec );
			  }
			  else if ( pkcs8.isRSA() )
			  {
			    pk = KeyFactory.getInstance( "RSA" ).generatePrivate( spec );
			  }
			  pk = pkcs8.getPrivateKey();
			  rsa = Signature.getInstance(algoritmo.toString());
			  rsa.initSign(pk);
			  datosByte = datos.getBytes();
			  rsa.update(datosByte);
			  signed = rsa.sign();
			  encoded = DatatypeConverter.printBase64Binary(signed);
			  logger.debug(encoded);
			}catch(BadPaddingException e){
				logger.error("Cadena: " + datos + "/Timbra: " + this.getRfc(), e);
				throw new BadPaddingException("Contraseña incorrecta");
			}
			catch(Exception e){
				logger.error("Cadena: " + datos + "/Timbra: " + this.getRfc(), e);
				throw e;
			}
		}
		else{
			logger.error("Archivo:" + this.privado!= null ? Integer.toString(this.privado.getContent().length) : "null" + "/Password:" + this.password != null ? Integer.toString(this.password.length()): "null" );
			throw new InvalidKeyException("Datos insuficientes");
		}
		
		return signed;
	}
	
	public boolean verifica(String datos, byte[] firma) throws GeneralSecurityException{
		return verifica(datos, firma, AlgoritmosFirma.SHA256RSA);
	}
	
	public boolean verifica(String datos, byte[] firma, AlgoritmosFirma algoritmo) throws GeneralSecurityException{
		boolean ret = true;
		InputStream pubcer = null; 
		X509Certificate certificado = null;
		byte[] datosByte = null;
		
		if(this.publico != null && this.publico.getContent().length > 0){
			pubcer = new ByteArrayInputStream(this.publico.getContent());
			try {
				certificado = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(pubcer);
				Signature sig = Signature.getInstance(algoritmo.toString());
        sig.initVerify(certificado);
        
        datosByte = datos.getBytes();
			  sig.update(datosByte);
        
			  ret = sig.verify(firma);
        
			} catch (CertificateException | NoSuchAlgorithmException e) {
				logger.error("Certificado: " + this.publico.getCorrelacion(), e);
				throw e;
			}
		}
		
		return ret;
	}
	
	public boolean comprueba() throws GeneralSecurityException{
		boolean ret = false;
		byte[] signed = null;
	
		if(this.privado != null && this.privado.getContent().length > 0 && this.publico != null && this.publico.getContent().length > 0 && this.password!= null && this.password.length() > 0){
			try{
				signed = timbra(Certificado.cadenaVerificacion);
				ret = verifica(Certificado.cadenaVerificacion, signed);
			}catch(SignatureException e){
				logger.error("Fallo al firmar", e);
				throw new SignatureException("Los archivos del certificado no coresponden");
			}catch(GeneralSecurityException e){
				logger.error("Fallo al firmar", e);
				throw e;
			}
		}
		else{
			throw new InvalidKeyException("Datos insuficientes");
		}
	 
	 return ret;
	}
	
	@Override
	public boolean sameValueAs(Certificado other) {
		// TODO Auto-generated method stub
		return this.password.equals(other.password) && this.privado.sameValueAs(other.privado) && 
			this.publico.sameValueAs(other.publico);
	}
	
	public void validoEmitir(PersonaFiscal emisor) throws ValidationException{
		
		if(this.serie == null || this.serie.isEmpty()){
			throw new ValidationException("No se pudo leer el numero de serie");
		}
		if(!vigente()){
			throw new ValidationException("El certificado se encuentra vencido " + this.getVigencia().fechaLegible() );
		}
		
		if(!this.rfc.getRfc().isEmpty()){
			if(!(this.rfc.valido() && emisor.getRfc().sameValueAs(this.rfc)))
				throw new ValidationException("El RFC no corresponde con el registrado en el certificado " + this.rfc.toString() );
		}
		
		if(!this.razonSocial.getNombre().isEmpty() && emisor.getRazonSocial() != null ){
			if(!this.razonSocial.sameValueAs(emisor.getRazonSocial()))
				throw new ValidationException("La razon social no corresponde con el registrado en el certificado " + this.razonSocial );
		}
	}
	
	public boolean vigente(){
		return this.vigencia.vigente();
	}
	
	public void correlacion(){
		this.publico.correlacion();
		this.privado.correlacion();
	}
	public void correlacion(Certificado other){
		this.publico.correlacion(other.publico);
		this.privado.correlacion(other.privado);
	}
}
