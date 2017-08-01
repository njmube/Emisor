package com.cubetech.facturador.emisor.application.impl;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cubetech.facturador.emisor.application.CuentaService;
import com.cubetech.facturador.emisor.application.EmisorService;
import com.cubetech.facturador.emisor.application.assembler.EmisorAssembler;
import com.cubetech.facturador.emisor.domain.cuenta.ArchivoCuenta;
import com.cubetech.facturador.emisor.domain.cuenta.Cuenta;
import com.cubetech.facturador.emisor.domain.cuenta.CuentaRepository;
import com.cubetech.facturador.emisor.domain.cuenta.Emisor;
import com.cubetech.facturador.emisor.interfaces.facade.CatalogosService;
import com.cubetech.facturador.emisor.interfaces.facade.dto.ArchivoDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.CertificadoDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.CuentaDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.DireccionDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.EmisorDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.RegistroEmisorDTO;
import com.cubetech.facturador.emisor.interfaces.rest.archivos.ArchivoRepDTO;
import com.cubetech.facturador.emisor.interfaces.rest.archivos.ArchivoRepository;

@Service
public class EmisorServiceImpl implements  EmisorService{

	private final static Logger logger = LoggerFactory.getLogger(EmisorServiceImpl.class);
	
	@Autowired
	CuentaRepository cuentaRepository;
	@Autowired
	CuentaService cuentaService;
	@Autowired
	CatalogosService catalogoService;
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	ArchivoRepository archivoRepository;
	
	@Override
	public RegistroEmisorDTO creaEmisor(String cuenta, EmisorDTO emisor) {
		RegistroEmisorDTO ret;
		Cuenta cta;
		Emisor e;
		Emisor tmp;
		EmisorAssembler emisorAssembler = new EmisorAssembler(catalogoService, modelMapper);
		boolean banUpdate = true;
		
		try{
			cta = consultaCuenta(cuenta);
			logger.debug("Cuenta:" + cta.toString());
		}catch(Exception exp){
			logger.error("Cuenta: " + cuenta, exp);
			throw exp;
		}
		
		try{
			if(emisor.getCorrelacion() != null && !emisor.getCorrelacion().isEmpty() ){
				logger.info("Registro Correlation: " + emisor.getCorrelacion());				
				tmp = emisorAssembler.toEmisor(emisor);
				try{
					e = cta.buscaEmisor(emisor.getCorrelacion());
					logger.debug("EmisorEncontrado: {}", e.toString());
					consultaArchivos(cta,e);
					if(e.sameIdentityAs(tmp)){
						banUpdate = false;
					}else{
						tmp.correlacion(e);
						e = tmp;
					}
				}catch(ValidationException excep){
					logger.info("New emisor: {}" , tmp.toString());
					e = tmp;
				}
				if(banUpdate){
					e.valida();
					actualizaCorrelaciones(e);
					save(cta, e);
				}
			}else{
				throw new ValidationException("No se puede registrar un emisor sin un identificado de correlacion");
			}
			
		}catch(Exception exp){
			logger.error(emisor.toString(), exp);
			throw exp;
		}
		ret = emisorAssembler.emisorToRegistroEmisorDTO(e);
		return ret;
	}
	
	private void actualizaCorrelaciones(Emisor e){
		//Consultar el repositorio de comprobantes por numero de certificado
		//Si encuentra alguno se genera un nuevo correlacion para el certificado de clave publica
	}
	
	@Transactional
	private void save(Cuenta cuenta, Emisor emisor) throws PersistenceException{
		EmisorAssembler assembler = new EmisorAssembler(modelMapper);
		List<ArchivoRepDTO> archs = assembler.emisorToListArchivoRepDTO(emisor);
		
		cuenta.upsert(emisor);
		this.cuentaRepository.save(cuenta);
		archivoRepository.save(cuenta.getCorrelacion(), archs);
		
	}
	
	private void consultaArchivos(Cuenta cuenta, Emisor emisor){
		ArchivoRepDTO archivo;
		ArchivoCuenta tmp;
		if(emisor.getLogo() != null && emisor.getLogo().getCorrelacion()!= null ){
			archivo = archivoRepository.findbyCuentaCorrelacion(cuenta.getCorrelacion(), emisor.getLogo().getCorrelacion());
			if(archivo == null){
				logger.error("No se encutra el archivo: {}", emisor.getLogo());
				throw new IllegalArgumentException("No se encutra el archivo: " + emisor.getLogo().toString());
			}
			tmp = this.modelMapper.map(archivo, ArchivoCuenta.class);
			tmp.setContent(ArchivoCuenta.toByteArray(archivo.getContent()));
			emisor.getLogo().actualiza(tmp);
		}
		
		if(emisor.getCertificado() != null && emisor.getCertificado().getPrivado() != null && emisor.getCertificado().getPrivado().getCorrelacion()!= null){
			archivo = archivoRepository.findbyCuentaCorrelacion(cuenta.getCorrelacion(), emisor.getCertificado().getPrivado().getCorrelacion());
			if(archivo == null){
				logger.error("No se encutra el archivo: {}", emisor.getCertificado().getPrivado());
				throw new IllegalArgumentException("No se encutra el archivo: " + emisor.getCertificado().getPrivado().toString());
			}
			tmp = this.modelMapper.map(archivo, ArchivoCuenta.class);
			tmp.setContent(ArchivoCuenta.toByteArray(archivo.getContent()));
			emisor.getCertificado().getPrivado().actualiza(tmp);
		}else{
			logger.error("No se encutra el archivo: {}", emisor.getCertificado().getPrivado());
			throw new IllegalArgumentException("No se encutra el archivo Logo: " + emisor.toString());
		}
		if(emisor.getCertificado() != null && emisor.getCertificado().getPublico() != null && emisor.getCertificado().getPublico().getCorrelacion()!= null){
			archivo = archivoRepository.findbyCuentaCorrelacion(cuenta.getCorrelacion(), emisor.getCertificado().getPublico().getCorrelacion());
			if(archivo == null){
				logger.error("No se encutra el archivo: {}", emisor.getCertificado().getPublico());
				throw new IllegalArgumentException("No se encutra el archivo: " + emisor.getCertificado().getPublico().toString());
			}
			tmp = this.modelMapper.map(archivo, ArchivoCuenta.class);
			tmp.setContent(ArchivoCuenta.toByteArray(archivo.getContent()));
			emisor.getCertificado().getPublico().actualiza(tmp);
		}else{
			logger.error("No se encutra el archivo: {}", emisor.getCertificado().getPublico());
			throw new IllegalArgumentException("No se encutra el archivo Logo: " + emisor.toString());
		}
	}
	
	private Cuenta consultaCuenta(String cuenta){
		Cuenta ret = null;
		ret = cuentaRepository.findOneByCorrelacion(cuenta);
		if(ret == null)
			throw new ValidationException("La cuenta no existe");
		if(!ret.isActiva())
			throw new ValidationException("Cuenta Inactiva");
		
		return ret;
	}
	
	@Override
	public List<EmisorDTO> consulta(String cuenta) {
		CuentaDTO ret = new CuentaDTO();
		EmisorDTO emisortmp = new EmisorDTO();
		CertificadoDTO certificadotmp = new CertificadoDTO();
		DireccionDTO direcciontmp = new DireccionDTO();
		ArchivoDTO logo = new ArchivoDTO();
		ArchivoDTO privado = new ArchivoDTO();
		ArchivoDTO publico = new ArchivoDTO();
		
		direcciontmp.setCalle("Av siempre viva");
		direcciontmp.setCodigoPostal("78398");
		direcciontmp.setColonia("El paseo");
		direcciontmp.setEstado("San Luis Potosi");
		direcciontmp.setMunicipio("SLP");
		direcciontmp.setNumeroExterior("322");
		direcciontmp.setPais("Mexico");
		
		logo.setNombre("LogotipoCompañia.jpg");
		logo.setTipo("JPG/Image");
		logo.setContent("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");
		
		privado.setNombre("archivo.key");
		privado.setTipo("Application/certificate");
		privado.setContent("MIIFDjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQIAgEAAoIBAQACAggAMBQGCCqGSIb3DQMHBAgwggS9AgEAMASCBMhmmi2054QNyuqIZ4FuaeycFStJ3cQ9/1YxuvsTzd+sBvrxLcmBkZvKcnXCEChUMCXQnCk7W1sJQe14Vd2OGeLlvh7A64k7Qw6i/KhQ7dDFEfhhA/LZtKtJM2B32/XklsMZRNuwT0B+B90zBCvZAEWV5P9Y1y095mh61QJBwKCFgcaS+t8cwGKknO1afOLft7ONI1uZoJYCeirkL17XqpGcep9zsdc9FZlE0ozyHwz1P2AmstPwqg9pzu3u/UKe7oWfK5/G5087hBVp8nw3e7p51PuHcrTT6JCa3XsG9wYYbgyDiOv+fqtjm6/vOUANJwWbHy+DPz+rQ15kX9V00ZSRakbdbKE2JHoo3AXIfVrB2ryTY8vLLqArYjZzBiPNqBpiErVMuTA2DURwq/Uanodahl/idRC8jEeKxhIypFEk3x6Iz1TWXFIUZ3wE0/KU06J48bAtiEUwZx4aGcCAAKdL4Q201P2OvkBBEqemdoM+rXZCL1e9f6egdmKFnO6Un27kSTxtm5ONNyQ4/cKvFRh7Bxsj5x8jj37+fKulXH6kcX0sk6VctP8uaeVJ5Bc/AFvOJyNSCjNGFhzr8mC/7C/Dpxk/e3YL0QT9tIU11yiCF1HtA+ULtF4KDFCTSEUlEHT9rnXL5eeNeGRtfc5cy9X8kcqbVmDCPav7w8ilPXwfpAJdLg3zcZgF6PhpnweR7o7V4k7uEvYPDBEsPIcaSIEsNL8Winnp+qwLmG2fEEZKfzHVgOg01RNhOSw6J02uIH4HVxnO2doLJk+mc/3GaD3OSl38hjTycghpwOQJPvL87D8bdFlRPktxK36m72/AdqJ3G/KMtCIZ59YIfDbHtqecC/083wlb29ZyEiAt2IfHMFHWj99olbcBbIG+FG90dgY/iyEnAJTNsUDiFoSunqB7GmSjj9ol/CaTIfWDh3gHMSPst0m4hJtpGLJaKA/ULyiMeT5NXnXm16nFhnFFkMrLYIxqWk8Jib4P/k4F7AW01DoHVEx4l11Tb4yGIVJX+qGfCdw57Zx/3N5SYok0Fj8MQXn721Mt/ev93dIY7CVa//0foTUKRtpieotj8xTkQDQXJmJc0yFcGnUEXqVP2Cqo2rO/Enr+4sGLb9wKS2f62VfpVYVd1bMsTpbraP7aU7nQaeDBhzTruPniFCGbq2KNg3SJn7SsyYXbQwTZwOCyEmb8pBoLasFD3c7y9aDPZ6AElpEzmJAMGyrh4amB8P28NTyDpohF7cXUyYgzL/okyMrqjpxv4c5qRZV0alNnM8j+ewk+iiRo4fXdwk/7l8lnhWdbSkXVcBRLbIOOgJZ9AufNWfL+6IGyC1/N9NeNlzZLGxaw/NRmPl+GG49Pk2wKZd9hNNGDSH0TKta8Id/DsATB77ZiwLDj/hOFpryHg4mgS8xrTjhhpXcypIXqsa2CX20BDx4MmEojEuB+j65Q0FBM7C9KALrpcdYJIHc9z+ahPlFLs6oR/w1mXCRAOUfkgyrgBxLGFJlVZw3MSYHlRlCyZwXXwYTMGXTqjJ8z/7lkEXRjW3SVtQ3EFUrVQ0gz3LikhkU+dbaO60w51ixbqtMLxKLzAGQiemN6uFe/DhrXJKkR6eSNv5Gw6hkt/6X51IPL59gCYmo=");

		publico.setNombre("archivo.cer");
		publico.setTipo("Application/certificate");
		publico.setContent("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");

		certificadotmp.setPassword("CMC161114E10");
		certificadotmp.setPrivado(privado);
		certificadotmp.setPublico(publico);
		
		emisortmp.setCertificado(certificadotmp);
		emisortmp.setCorrelacion("1");
		emisortmp.setDireccion(direcciontmp);
		emisortmp.setLugarExpedicion("78398");
		emisortmp.setLogo(logo);
		emisortmp.setNombre("CUBETECHNOLOGIC");
		emisortmp.setRegimenFiscal("601");
		emisortmp.setRfc("CMC161114E10");
		
		ret.setCorrelacion("123456789-987654321");
		ret.getEmisores().add(emisortmp);
		
		
		return ret.getEmisores();
		
	}

}
