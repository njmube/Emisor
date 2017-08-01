package com.cubetech.facturador.emisor.interfaces.rest.archivos.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.cubetech.facturador.emisor.AppConfigTest;
import com.cubetech.facturador.emisor.interfaces.rest.archivos.ArchivoRepDTO;
import com.cubetech.facturador.emisor.interfaces.rest.archivos.ArchivoRepository;
import com.cubetech.facturador.emisor.interfaces.rest.archivos.impl.ArchivoRepositoryImpl;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=AppConfigTest.class)
@TestPropertySource("/application.properties")
public class ArchivoRepositoryImplTest {

	@Autowired
	ArchivoRepository archivoRepository = new ArchivoRepositoryImpl();
	
	@Test
	public void findbyCuentaCorrelacionTest(){
		ArchivoRepDTO respuesta = archivoRepository.findbyCuentaCorrelacion("123-456-789", "21b06947-0c81-4414-8235-e3e86ad78277");
		
		assertTrue(respuesta.getCorrelacion().equals("21b06947-0c81-4414-8235-e3e86ad78277"));
		
	}
	
	//@Test
	public void savetest() {
		List<ArchivoRepDTO> archivos = new ArrayList<>();
		boolean band = false;
		
		UUID idOne = UUID.randomUUID();
		UUID idtwo = UUID.randomUUID();
		ArchivoRepDTO tmp = new ArchivoRepDTO();
		tmp.setContent("DSSFASDBCMANSA");
		tmp.setCorrelacion(idOne.toString());
		tmp.setNombre("UnArchivo.pdf");;
		tmp.setTipo("Application/pdf");
		ArchivoRepDTO tmp2 = new ArchivoRepDTO();
		tmp2.setContent("DSSFASDBCMANSA");
		tmp2.setCorrelacion(idtwo.toString());
		tmp2.setNombre("UnArchivo.pdf");;
		tmp2.setTipo("Application/pdf");
		archivos.add(tmp);
		archivos.add(tmp2);
		List<ArchivoRepDTO> respuesta = archivoRepository.save("123-456-789", archivos);
		
		band = archivos.size() == respuesta.size();
		for(ArchivoRepDTO request : archivos){	
			boolean band2 = false;
			for(ArchivoRepDTO respu : respuesta){
				if(!band2){
					if(request.getCorrelacion().equals(respu.getCorrelacion())){
						band2 = respu.getId().length() > 0;
						break;
					}
				}
			}
			band = band & band2;
		}
		assertTrue(band);
	}

}
