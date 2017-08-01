package com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos;

import java.util.Map;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper=true)
public class CodigoPostalDTO extends CatalogoDTO {
	
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
	
	public CodigoPostalDTO(Map<?,?> map){
		super(map);
		setEstado((String)map.get("estado"));
		setMunicipio((String)map.get("municipio"));
		setLocalidad((String)map.get("localidad"));
	}
	
}
