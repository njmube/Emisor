package com.cubetech.facturador.emisor.interfaces.facade.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cubetech.facturador.emisor.interfaces.facade.CatalogosService;
import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.CatalogoDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.CodigoPostalDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.RegimenFiscalDTO;
import com.cubetech.facturador.emisor.interfaces.facade.internal.assembler.CatalogosDTOAssembler;
import com.cubetech.facturador.emisor.interfaces.rest.client.CatalogosRepository;

@Service
public class CatalogosServiceImpl implements CatalogosService {

	private final static Logger logger = LoggerFactory.getLogger(CatalogosServiceImpl.class);
	
	@Autowired
	CatalogosRepository restCatalogoRespository;
	
	@Autowired
	CatalogosDTOAssembler catDTOAssembler;
	
	private static final String uriCatlista = "/catalogo/clave?";
	
	@Override
	public Map<String, CatalogoDTO> consultaCatalogos(Map<String, String> catalogos) {
		Map<String, CatalogoDTO> ret;
		Map<String, List<CatalogoDTO>> tmp;
		List<Map<?, ?>> respuesta;
		List<CatalogoDTO> lCatalogos = convierteMap(catalogos);
		String uri = GeneraURI(lCatalogos);
		logger.debug(uri);
		respuesta = restCatalogoRespository.getList(uri);
		tmp = catDTOAssembler.toCatalogoDTO(respuesta);
		ret = filtraRespuesta(tmp);
		
		if(logger.isDebugEnabled())
			logger.debug(" Return consultaCatalogos: {}", ret.toString());
		return ret;
	}
	
	private Map<String, CatalogoDTO> filtraRespuesta(Map<String, List<CatalogoDTO>> catalogos){
		Map<String, CatalogoDTO> ret = new HashMap<String, CatalogoDTO>();
		
		for (Map.Entry<String, List<CatalogoDTO>> entry : catalogos.entrySet()){
			CatalogoDTO tmp = buscaCatalogo(entry.getValue());
			if(tmp != null){
				ret.put(entry.getKey(), tmp);
			}
		}
		return ret;
	}
	
	private CatalogoDTO buscaCatalogo(List<CatalogoDTO> catalogo){
		CatalogoDTO ret = null;
		CatalogoDTO tmp;
		int index = -1;
		int i = 0;
		
		if(catalogo.size() > 0){
			for(i =0; i < catalogo.size() && index == -1 ; i ++){
				tmp = catalogo.get(i);
				if(tmp.isVigente()){
					index = i;
				}
			}
			if(index == -1){
				index = 0;
			}
			ret = catalogo.get(index);
		}
		return ret;
	}
	
	private List<CatalogoDTO> convierteMap(Map<String, String> catalogos){
		List<CatalogoDTO> ret = new ArrayList<CatalogoDTO>();
		CatalogoDTO tmp;
		
		for (Map.Entry<String, String> entry : catalogos.entrySet()){
			tmp = null;
			switch(entry.getKey()){
			case "regimenFiscal":
					tmp = new RegimenFiscalDTO();
				break;
			case "codigoPostal":
				tmp = new CodigoPostalDTO();
				break;
			}
			if(tmp != null){
				tmp.setClaveSat(entry.getValue());
				ret.add(tmp);
			}
		    //System.out.println(entry.getKey() + "/" + entry.getValue());
		}
		
		return ret;
	}
	
	private String GeneraURI(List<CatalogoDTO> catalogos){
		String ret = uriCatlista;
		int i = 0;
		
		for(i = 0; i < catalogos.size() ; i++){
			CatalogoDTO tmp = catalogos.get(i);
			if(i != 0){
				ret = ret + "&";
			}
			ret = ret + tmp.url();
		}
		
		return ret;
	}
	

}
