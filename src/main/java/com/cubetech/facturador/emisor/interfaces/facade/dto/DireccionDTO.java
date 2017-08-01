package com.cubetech.facturador.emisor.interfaces.facade.dto;

import lombok.Data;

@Data
public class DireccionDTO {
	private String calle;
	private String numeroExterior;
	private String numeroInterior;
	private String codigoPostal;
	private String colonia;
	private String pais;
	private String estado;
	private String municipio;
}
