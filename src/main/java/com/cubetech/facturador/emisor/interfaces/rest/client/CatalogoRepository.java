package com.cubetech.facturador.emisor.interfaces.rest.client;

import java.util.Map;

import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.CatalogoDTO;


public interface CatalogoRepository {
	public Map<String, CatalogoDTO> consultaClaves(Map<String,String> claves);
}
