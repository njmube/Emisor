package com.cubetech.facturador.emisor.interfaces.rest.archivos.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.ws.http.HTTPException;

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
import org.springframework.web.client.RestTemplate;

import com.cubetech.facturador.emisor.AppProperties;
import com.cubetech.facturador.emisor.interceptor.RequestLoggingInterceptor;
import com.cubetech.facturador.emisor.interfaces.rest.archivos.ArchivoRepDTO;
import com.cubetech.facturador.emisor.interfaces.rest.archivos.ArchivoRepository;

@Repository
public class ArchivoRepositoryImpl implements ArchivoRepository {
	
	@Autowired
	AppProperties properties;
	
  private RestTemplate rest;
  private HttpStatus status;
	
  public ArchivoRepositoryImpl() {
    this.rest = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
    List<ClientHttpRequestInterceptor> tmp = new ArrayList<ClientHttpRequestInterceptor>();
    tmp.add(new RequestLoggingInterceptor());
    rest.setInterceptors(tmp);
  }
  
	@Override
	public ArchivoRepDTO findbyCuentaCorrelacion(String cuenta, String correlation) {
		ArchivoRepDTO ret = null;
		final String url = properties.getArchivourl() + "/Archivo" + "/" + correlation;
		HttpHeaders headers = ArchivoRepositoryImpl.HedersJson();
		headers.set("cuenta", cuenta);
		HttpEntity<ArchivoRepDTO> request = new  HttpEntity<>(headers);
		
		ResponseEntity<ArchivoRepDTO> response = rest.exchange(url, HttpMethod.GET, request, ArchivoRepDTO.class);

		status = response.getStatusCode();
		
		if(status == HttpStatus.ACCEPTED){
			ret = response.getBody();
		}
		
		return ret;
	}

	public static HttpHeaders HedersJson(){
		HttpHeaders headers =  new HttpHeaders();
		
		headers.add("Content-Type", "application/json");
    headers.add("Accept", "*/*");
    headers.add("Authorization","Basic dXNlcjo1RFtqWjRSXyRyOHdZLlNM");
		return headers;
	}
	
	@Override
	public List<ArchivoRepDTO> save(String cuenta, List<ArchivoRepDTO> archivo) {
		final String url = properties.getArchivourl() +  "/Archivo";
		ArchivoRepDTO[] tmp = archivo.toArray( new ArchivoRepDTO[0]);
		ArchivoRepDTO[] respuesta;
		List<ArchivoRepDTO> ret;
		HttpHeaders headers = ArchivoRepositoryImpl.HedersJson();
		headers.set("cuenta", cuenta);
		
		HttpEntity<ArchivoRepDTO[]> request = new  HttpEntity<>(tmp, headers);
		ResponseEntity<ArchivoRepDTO[]> response = rest.exchange(url, HttpMethod.POST, request, ArchivoRepDTO[].class);

		status = response.getStatusCode();
		
		if(status == HttpStatus.OK){
			respuesta = response.getBody();
			ret = Arrays.asList(respuesta);
		}else{
			throw new HTTPException(status.value());
		}
		
		return ret;
	}

}
