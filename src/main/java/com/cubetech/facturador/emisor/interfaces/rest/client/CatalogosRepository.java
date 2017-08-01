package com.cubetech.facturador.emisor.interfaces.rest.client;


	import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
	import org.springframework.http.HttpEntity;
	import org.springframework.http.HttpHeaders;
	import org.springframework.http.HttpMethod;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Repository;
	import org.springframework.web.client.RestClientException;
	import org.springframework.web.client.RestTemplate;

import com.cubetech.facturador.emisor.interceptor.RequestLoggingInterceptor;
@Repository
	public class CatalogosRepository {
	
		private final static Logger logger = LoggerFactory.getLogger(CatalogosRepository.class);

	  private static final String server = "http://localhost:57777";
	  private RestTemplate rest;
	  private HttpHeaders headers;
	  private HttpStatus status;

	  public CatalogosRepository() {
	    this.rest = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
	    List<ClientHttpRequestInterceptor> tmp = new ArrayList<ClientHttpRequestInterceptor>();
	    tmp.add(new RequestLoggingInterceptor());
	    rest.setInterceptors(tmp);
	    this.headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
	    headers.add("Accept", "*/*");
	  }
	  
	  public <CatalogosDTO> List<CatalogosDTO> getList(String uri) throws RestClientException{
  		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
  		ParameterizedTypeReference<List<CatalogosDTO>> t = new ParameterizedTypeReference<List<CatalogosDTO>>() {};
  		ResponseEntity<List<CatalogosDTO>> response = rest.exchange(server + uri, HttpMethod.GET, requestEntity, t);
  		return response.getBody();
	  }
	  
/*
	  public String get(String uri) {
	    HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
	    ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.GET, requestEntity, String.class);
	    this.setStatus(responseEntity.getStatusCode());
	    return responseEntity.getBody();
	  }

	  public String post(String uri, String json) {   
	    HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
	    ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.POST, requestEntity, String.class);
	    this.setStatus(responseEntity.getStatusCode());
	    return responseEntity.getBody();
	  }

	  public void put(String uri, String json) {
	    HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
	    ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.PUT, requestEntity, null);
	    this.setStatus(responseEntity.getStatusCode());   
	  }

	  public void delete(String uri) {
	    HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
	    ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.DELETE, requestEntity, null);
	    this.setStatus(responseEntity.getStatusCode());
	  }
*/
	  public HttpStatus getStatus() {
	    return status;
	  }

	  public void setStatus(HttpStatus status) {
	    this.status = status;
	  } 
	}

