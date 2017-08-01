package com.cubetech.facturador.emisor.domain.common;


import java.util.Arrays;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.DatatypeConverter;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class Archivo {

	@Transient
	protected String nombre;
	@Transient
	protected String tipo;
	@Transient
	protected byte[] content;
	
	public static String toString(byte[] datos){
		return DatatypeConverter.printBase64Binary(datos);
	}
	
	public static byte[] toByteArray(String datos){
		return DatatypeConverter.parseBase64Binary(datos);
	}
	
	public boolean sameValueAs(Archivo other){
		return other != null && ((this.content == null && other.content == null) || (this.content!=null && other.content!= null && Arrays.equals(this.content , other.content)));
	}
	public void actualiza(Archivo other){
		this.nombre  = other.nombre;
		this.tipo		 = other.tipo;
		this.content = other.content;
	}

}
