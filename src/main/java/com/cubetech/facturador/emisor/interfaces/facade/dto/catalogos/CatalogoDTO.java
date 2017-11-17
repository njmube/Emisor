package com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos;

import java.io.Serializable;

import lombok.Data;

@Data
public abstract class CatalogoDTO implements Serializable {
	
	private static final long serialVersionUID = 4777133482257439636L;
	private String claveSat;
	private String descripcion;
	private boolean vigente;
	
	public abstract String url();
	public abstract String getName();
	
	public CatalogoDTO(){
		
	}

}
