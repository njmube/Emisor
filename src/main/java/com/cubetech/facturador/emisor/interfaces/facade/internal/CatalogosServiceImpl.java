package com.cubetech.facturador.emisor.interfaces.facade.internal;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cubetech.facturador.emisor.interfaces.facade.CatalogosService;
import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.CatalogoDTO;
import com.cubetech.facturador.emisor.interfaces.rest.client.CatalogoRepository;

@Service
public class CatalogosServiceImpl implements CatalogosService {
	
	@Autowired
	CatalogoRepository restCatalogoRespository;
	
	@Override
	public Map<String, CatalogoDTO> consultaCatalogos(Map<String, String> catalogos) {
		Map<String, CatalogoDTO> ret;
		
		ret = restCatalogoRespository.consultaClaves(catalogos);
		
		return ret;
	}
	
	

}
