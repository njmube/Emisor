package com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public abstract class CatalogoDTO implements Serializable {
	
	private String claveSat;
	private String descripcion;
	private boolean vigente;
	
	public abstract String url();
	public abstract String getName();
	
	public CatalogoDTO(){
		
	}
	public CatalogoDTO(Map<?,?> map){
		setClaveSat((String) map.get("claveSat"));
		setDescripcion((String) map.get("descripcion"));
		setVigente((boolean)map.get("vigente"));
	}
	

}
