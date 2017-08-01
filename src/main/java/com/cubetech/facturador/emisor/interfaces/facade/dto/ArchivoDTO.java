package com.cubetech.facturador.emisor.interfaces.facade.dto;

import javax.xml.bind.DatatypeConverter;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
public class ArchivoDTO {
	@NotEmpty
	private String nombre;
	@NotEmpty
	private String tipo;
	@NotEmpty
	private String content;
	
	public byte[] toBytes(){
		return DatatypeConverter.parseBase64Binary(content);
	}
}
