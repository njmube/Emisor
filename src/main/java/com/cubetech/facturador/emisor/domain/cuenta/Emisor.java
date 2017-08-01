package com.cubetech.facturador.emisor.domain.cuenta;

import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cubetech.facturador.emisor.domain.catalogo.CodigoPostal;
import com.cubetech.facturador.emisor.domain.catalogo.RegimenFiscal;
import com.cubetech.facturador.emisor.domain.common.Direccion;
import com.cubetech.facturador.emisor.domain.common.PersonaFiscal;
import com.cubetech.facturador.emisor.domain.shared.Entidad;

import lombok.Data;

@Entity
@Data
public class Emisor implements Entidad<Emisor> {
	
	@Transient
	private final static Logger logger = LoggerFactory.getLogger(Emisor.class);

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ems_id")
	private long id;
	
	@Column(name = "cta_id", insertable= false, updatable = false)
	private long cuenta;
	
	@Column(nullable=false, updatable = false)
	private String correlacion;
	@Embedded
	private PersonaFiscal emisor;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="claveSat", column=@Column(name="regimen_sat")),
	})
	private RegimenFiscal regimenFiscal;
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="claveSat", column=@Column(name="expedicion_sat")),
	})
	private CodigoPostal lugarExpedicion;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="correlacion", column=@Column(name="logo_id", nullable= true))
	})
	private ArchivoCuenta logo;
	private Direccion direccion;
	private Certificado certificado;
	
	@Override
	public boolean sameIdentityAs(Emisor other) {
		boolean ret = true;
		 ret = this.correlacion.equals(other.correlacion)					&& this.emisor.sameValueAs(other.emisor);
		 ret = ret && this.regimenFiscal.sameValueAs(other.regimenFiscal); 	
		 ret = ret && this.lugarExpedicion.sameValueAs(other.lugarExpedicion);
		 ret = ret && ((this.logo == null && other.logo == null) || (this.logo != null &&  this.logo.sameValueAs(other.logo))); 										
		 ret = ret && ((this.direccion == null && other.direccion == null) || (this.direccion != null && this.direccion.sameValueAs(other.direccion)));
		 ret = ret && this.certificado.sameValueAs(other.certificado);
		 return ret;
	}
	
	public void valida() throws ValidationException{
		
		this.emisor.validaRegimenFiscal(this.regimenFiscal);
		
		if(!this.lugarExpedicion.isVigente())
			throw new ValidationException("CP. " + this.lugarExpedicion.toString() + "no esta vigente");
		try{
			this.certificado.extraeInformacion();
		}catch(CertificateException excep){
			throw new ValidationException("Error al extraer los datos del certificado");
		}
		try{
			this.certificado.validoEmitir(this.emisor);
		}catch(ValidationException e){
			logger.error("{}" , this.toString(), e);
			throw e;
		}
		try{
			if(!this.certificado.comprueba())
				throw new ValidationException("Los certificados no corresponden");
		}catch(GeneralSecurityException e){
			logger.error("Error" + this.toString(), e);
			throw new ValidationException("Certificado Invalido: " + e.getMessage());
		}
		correlacion();
	}	
	public void correlacion(){
		if(this.logo != null)
			this.logo.correlacion();
		this.certificado.correlacion();
	}
	public void correlacion(Emisor other){
		if(this.logo != null)
			this.logo.correlacion(other.logo);
		this.certificado.correlacion(other.certificado);
	}
	public void copia(Emisor other){
		this.emisor = other.emisor;
		this.certificado = other.certificado;
		this.direccion = other.direccion;
		this.logo = other.logo;
		this.lugarExpedicion = other.lugarExpedicion;
		this.regimenFiscal = other.regimenFiscal;
	}
}
