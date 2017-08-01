package com.cubetech.facturador.emisor.interfaces.facade;

import java.util.Map;

import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.CatalogoDTO;

public interface CatalogosService {
	public Map<String, CatalogoDTO> consultaCatalogos(Map<String,String> catalogos); 
}
