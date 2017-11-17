package com.cubetech.facturador.emisor.interfaces.rest.client;
/*
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.web.client.RestClientException;

import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.CatalogoDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.CatalogosDTO;
import com.cubetech.facturador.emisor.interfaces.facade.internal.assembler.CatalogosDTOAssembler;
import com.cubetech.facturador.emisor.interfaces.rest.client.CatalogosRepository;

public class CatalogosRepositoryTest {
	
	private CatalogosDTOAssembler dtoAssembler = new CatalogosDTOAssembler();
	private CatalogosRepository restCliente = new CatalogosRepository();
	
	@Test
	public void gettest() {
		List<Map<?, ?>> respuesta;
		Map<String,List<CatalogoDTO>> cat;
		try{
			 respuesta = restCliente.getList("/catalogo/clave?regimenFiscal=601&codigoPostal=78398");
			 

			 cat = dtoAssembler.toCatalogoDTO(respuesta);

			 
			/* for(CatalogosDTO tmp: respuesta){
					cat = dtoAssembler.toCatalogoDTO(tmp);
					if(cat.size() > 0){
						cat.get(0);
					}
				}
		}catch(RestClientException e){
			e.printStackTrace();
		}
		
	}
}*/
