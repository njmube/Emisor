package com.cubetech.facturador.emisor.application.assembler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;
import javax.xml.bind.DatatypeConverter;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cubetech.facturador.emisor.domain.catalogo.CodigoPostal;
import com.cubetech.facturador.emisor.domain.catalogo.RegimenFiscal;
import com.cubetech.facturador.emisor.domain.cuenta.Emisor;
import com.cubetech.facturador.emisor.interfaces.facade.CatalogosService;
import com.cubetech.facturador.emisor.interfaces.facade.dto.EmisorDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.EmitirDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.RegistroEmisorDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.CatalogoDTO;
import com.cubetech.facturador.emisor.interfaces.rest.archivos.ArchivoRepDTO;

public class EmisorAssembler {
	private final static Logger logger = LoggerFactory.getLogger(EmisorAssembler.class);
	
	private CatalogosService catalogoService;
	private ModelMapper modelMapper;
	
	public EmisorAssembler(CatalogosService catalogoService, ModelMapper modelMapper){
		this.catalogoService = catalogoService;
		this.modelMapper = modelMapper;
	}
	public EmisorAssembler(ModelMapper modelMapper){
		this.modelMapper = modelMapper;
	}
	
	public Emisor toEmisor(EmisorDTO edto){
		Map<String, String> map = new HashMap<String,String>();
		Map<String, CatalogoDTO> catalogos;
		Emisor ret;
		RegimenFiscal regimenFiscal;
		CodigoPostal codigoPostal;
		
		if(logger.isDebugEnabled())
			logger.debug(edto.toString());
		
		map.put("regimenFiscal", edto.getRegimenFiscal());
		map.put("codigoPostal", edto.getLugarExpedicion());
		
		catalogos = catalogoService.consultaCatalogos(map);
		try{
			ret = modelMapper.map(edto, Emisor.class);
			
			if(logger.isDebugEnabled())
				logger.debug("Conversion: {}", ret.toString());
			
		}catch(Exception e){
			logger.error("Conversion:" + edto.toString(), e);
			throw e;
		}
		if(catalogos.containsKey("regimenFiscal")){
			regimenFiscal = modelMapper.map(catalogos.get("regimenFiscal"), RegimenFiscal.class);
			ret.setRegimenFiscal(regimenFiscal);
		}
		else{
			throw new ValidationException("La clave de regimen fiscal es incorrecta");
		}
		if(catalogos.containsKey("codigoPostal")){
			codigoPostal = modelMapper.map(catalogos.get("codigoPostal"), CodigoPostal.class);
			ret.setLugarExpedicion(codigoPostal);
		}else{
			throw new ValidationException("La clave de lugar expedicion en incorrecta es incorrecta");
		}
		return ret;
	}
	
	public List<ArchivoRepDTO> emisorToListArchivoRepDTO(Emisor e){
		List<ArchivoRepDTO> ret = new ArrayList<>();
		ArchivoRepDTO tmp;
		
		if(e.getLogo() != null){
			tmp = modelMapper.map(e.getLogo(), ArchivoRepDTO.class);
			tmp.setContent(DatatypeConverter.printBase64Binary(e.getLogo().getContent()));
			ret.add(tmp);
		}
		tmp = modelMapper.map(e.getCertificado().getPublico(), ArchivoRepDTO.class);
		tmp.setContent(DatatypeConverter.printBase64Binary(e.getCertificado().getPublico().getContent()));
		ret.add(tmp);
		
		tmp = modelMapper.map(e.getCertificado().getPrivado(), ArchivoRepDTO.class);
		tmp.setContent(DatatypeConverter.printBase64Binary(e.getCertificado().getPrivado().getContent()));
		ret.add(tmp);
		
		return ret;
	}
	public RegistroEmisorDTO emisorToRegistroEmisorDTO(Emisor e) {
		RegistroEmisorDTO ret = new RegistroEmisorDTO();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		ret.setNumeroCertificado(e.getCertificado().getSerie());
		ret.setFechaInicial( formatter.format(e.getCertificado().getVigencia().getFechaInicial()));
		ret.setFechaFinal( formatter.format(e.getCertificado().getVigencia().getFechaFinal()));
		return ret;
	}
	
	public EmitirDTO emisorToEmitirDTO(Emisor e){
		EmitirDTO ret = new EmitirDTO();
		
		ret.setNoCertificado(e.getCertificado().getSerie());
		ret.setCertificado(e.getCertificado().getPublico().getCorrelacion());
		ret.setLugarExpedicion(e.getLugarExpedicion().getClaveSat());
		ret.setRfc(e.getEmisor().getRfc().getRfc());
		ret.setNombre(e.getEmisor().getRazonSocial().getNombre());
		ret.setRegimenFiscal(e.getRegimenFiscal().getClaveSat());
		ret.setPass(e.getCertificado().getPassword());
		ret.setPrivadoId(e.getCertificado().getPrivado().getCorrelacion());
		ret.setEmitir(e.getCertificado().vigente());
		
		return ret;
	}
}
