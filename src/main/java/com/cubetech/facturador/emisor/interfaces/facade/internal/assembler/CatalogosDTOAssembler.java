package com.cubetech.facturador.emisor.interfaces.facade.internal.assembler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.CatalogoDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.CatalogosDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.CodigoPostalDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.RegimenFiscalDTO;

public class CatalogosDTOAssembler {
	
	private final static Logger logger = LoggerFactory.getLogger(CatalogosDTOAssembler.class);

	@SuppressWarnings("unchecked")
	public Map<String, List<CatalogoDTO>> toCatalogoDTO(List<Map<?,?>> datos) throws IllegalArgumentException{
		List<CatalogoDTO> regimen = new ArrayList<CatalogoDTO>();
		CatalogoDTO tmp;
		Map<String, List<CatalogoDTO>> ret = new HashMap<String, List<CatalogoDTO>>();
		
		if(logger.isDebugEnabled())
			logger.debug("Entrada:{}", datos.toString());
		
		
		for(Map<?,?> data : datos){
			List<CatalogoDTO> ltmp;
			if(data.containsKey("nombre")){
				String nombre = (String) data.get("nombre");
				if(ret.containsKey(nombre)){
					ltmp = ret.get(nombre);
				}
				else{
					ltmp = new ArrayList<CatalogoDTO>();
					ret.put(nombre, ltmp);
				}
				List<Map<?,?>> info = (List<Map<?, ?>>) data.get("datos");
				
				for(Map<?,?> d : info){
					tmp = null;
					switch(nombre){
					case "regimenFiscal":
						tmp = new RegimenFiscalDTO(d);
						break;
					case "codigoPostal":
						tmp = new CodigoPostalDTO(d);
						break;
					}
					if(tmp!= null){
						ltmp.add(tmp);
					}
				}
			}else{
				throw new IllegalArgumentException("No se encontro el atributo [nombre]");
			}
		}
		if(logger.isDebugEnabled())
			logger.debug("Salida:{}", ret.toString());
		return ret;
	}
}
