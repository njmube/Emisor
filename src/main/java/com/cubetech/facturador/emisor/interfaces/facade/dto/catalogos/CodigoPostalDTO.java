package com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class CodigoPostalDTO extends CatalogoDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6660838013234387933L;
	private static final String NAME = "codigoPostal";
	private String estado;
	private String municipio;
	private String localidad;
	
	@Override
	public String url() {
		return NAME + "=" + super.getClaveSat();
	}
	@Override
	public String getName(){
		return NAME;
	}
	
	public CodigoPostalDTO(){
		
	}
	
}
