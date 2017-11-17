package com.cubetech.facturador.emisor.interfaces.facade.internal;
/*
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.cubetech.facturador.emisor.AppConfigTest;
import com.cubetech.facturador.emisor.interfaces.facade.CatalogosService;
import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.CatalogoDTO;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=AppConfigTest.class)
public class CatalogosServiceImplTest {

	@Autowired
	CatalogosService catalogosService;
	
	@Test
	public void test2catalogos() {
		Map<String, String> catalogos = new HashMap<String, String>();
		Map<String, CatalogoDTO> respuesta;
		boolean asser = true;
		
		catalogos.put("regimenFiscal", "601");
		catalogos.put("codigoPostal", "78398");
		respuesta = catalogosService.consultaCatalogos(catalogos);
		
		asser = respuesta.containsKey("regimenFiscal") && respuesta.containsKey("codigoPostal");
		
		assertTrue(asser);
		
	}
	
	@Test
	public void testVigenteFalse() {
		Map<String, String> catalogos = new HashMap<String, String>();
		Map<String, CatalogoDTO> respuesta;
		boolean asser = true;
		
		catalogos.put("regimenFiscal", "628");
		catalogos.put("codigoPostal", "78398");
		respuesta = catalogosService.consultaCatalogos(catalogos);
		
		asser = respuesta.containsKey("regimenFiscal") && respuesta.containsKey("codigoPostal")
				&& !respuesta.get("regimenFiscal").isVigente();
		
		assertTrue(asser);
		
	}

}
*/
