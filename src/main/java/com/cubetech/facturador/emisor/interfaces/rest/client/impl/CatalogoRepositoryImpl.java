package com.cubetech.facturador.emisor.interfaces.rest.client.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.cubetech.facturador.emisor.AppProperties;
import com.cubetech.facturador.emisor.interceptor.RequestLoggingInterceptor;
import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.CatalogoDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.CodigoPostalDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos.RegimenFiscalDTO;
import com.cubetech.facturador.emisor.interfaces.rest.client.CatalogoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Repository
public class CatalogoRepositoryImpl implements CatalogoRepository {

	private final static Logger logger = LoggerFactory.getLogger(CatalogoRepositoryImpl.class);
	
	private RestTemplate restTemplate;
	private HttpHeaders headers;
	private AppProperties properties;
  
  @Autowired
	public CatalogoRepositoryImpl(AppProperties properties){
		this.restTemplate = new RestTemplate(getClientHttpRequestFactory());
    List<ClientHttpRequestInterceptor> tmp = new ArrayList<ClientHttpRequestInterceptor>();
    tmp.add(new RequestLoggingInterceptor());
    this.restTemplate.setInterceptors(tmp);
    
    this.properties = properties;
    this.headers = new HttpHeaders();
    this.headers.add("Content-Type", "application/json");
    this.headers.add("Accept", "*/*");
	}
	
	@Override
	public Map<String, CatalogoDTO> consultaClaves(Map<String, String> claves) throws RestClientException {
		String respuesta;
		Map<String, CatalogoDTO> ret;
		
		respuesta = consulta(claves);
		ret = convierte(respuesta);
		
		return ret;
	}
	
	public String consulta(Map<String, String> claves) throws RestClientException{
		String ret;
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
		ResponseEntity<String> response;
		
		params.setAll(claves);
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(properties.getCatalogourl() + "/catalogo/clave").queryParams(params).build();
		try{
			response = restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, requestEntity, String.class);
		}catch(RestClientException e){
			logger.error("Consulta de catalogos URL {}", uriComponents.toUri(),e);
			throw new RestClientException("Error de conexion al consultar los catalogos ");
		}
		if(response.getStatusCode() == HttpStatus.OK)
			ret = response.getBody();
		else{
			logger.error("No fue posible hacer la consulta de los catalogos Url: {} CodigoRestpuetsa: {}", uriComponents.toUri(), response.getStatusCode()  );
			throw new RestClientException("Error de conexion al consultar los catalogos ");
		}
			
		return ret;
	}
	
	public Map<String, CatalogoDTO> convierte(String datos){
		Map<String, CatalogoDTO> ret = new HashMap<>();
		
		JsonNode rootNode;
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode nombre;
		JsonNode dato;
		CatalogoDTO[] catalogos;
		JsonNode elemento;
		String key;
    
    try {
			rootNode = objectMapper.readTree(datos);
			Iterator<JsonNode> elements = rootNode.elements();
			
			while(elements.hasNext()){
				elemento = elements.next();
				
				if(logger.isDebugEnabled()){
					logger.debug("Convirtiendo catalogo {}", elemento);
				}
				
				nombre = elemento.path("nombre");
				dato = elemento.path("datos");
				catalogos = null;
				switch(nombre.asText()){
				case "codigoPostal":
					catalogos = objectMapper.convertValue(dato,CodigoPostalDTO[].class);
					break;
				case "regimenFiscal":
					catalogos = objectMapper.convertValue(dato,RegimenFiscalDTO[].class);
					break;

				}
				if(catalogos !=null && catalogos.length > 0){
					key = nombre.asText();
					ret.put(key, catalogos[0]);
				}
				
			}
			
		} catch (JsonProcessingException e) {
			logger.error("Error al convertir a JSON datos {}", datos, e );
			throw new RestClientException("Error al consultar catalogos conversion datos: " + datos);
		} catch (IOException e) {
			logger.error("Error al lectura al convertir JSON datos {}", datos, e );
			throw new RestClientException("Error al consultar catalogos conversion datos: " + datos);
		}
		
		return ret;
	}
	
	private BufferingClientHttpRequestFactory getClientHttpRequestFactory() {
    int timeout = 5000;
    
    SimpleClientHttpRequestFactory clientHttpRequestFactory
      = new SimpleClientHttpRequestFactory();
    clientHttpRequestFactory.setConnectTimeout(timeout);
    clientHttpRequestFactory.setReadTimeout(timeout);
    return new BufferingClientHttpRequestFactory(clientHttpRequestFactory);
	}

}
