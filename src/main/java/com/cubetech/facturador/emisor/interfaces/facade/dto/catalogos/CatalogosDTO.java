package com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cubetech.facturador.emisor.interfaces.facade.internal.deserializer.DatosJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.ToString;

@Data
public class CatalogosDTO implements Serializable{
	
	private String nombre;
	//@JsonDeserialize(using = DatosJsonDeserializer.class)
	List<String> datos;
	
	public CatalogosDTO(String nombre){
		this.nombre = nombre;
		datos = new ArrayList<String>();
	}
	public CatalogosDTO(){
		/*this.nombre = nombre;*/
		datos = new ArrayList<String>();
	}

}
