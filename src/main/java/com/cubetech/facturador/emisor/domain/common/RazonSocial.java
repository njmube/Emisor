package com.cubetech.facturador.emisor.domain.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cubetech.facturador.emisor.domain.shared.ValueObject;
import com.cubetech.facturador.emisor.domain.shared.ValueObjectBase;

import lombok.Data;

@Data
@Embeddable
public class RazonSocial extends ValueObjectBase implements ValueObject<RazonSocial> {

	private final static Logger logger = LoggerFactory.getLogger(RazonSocial.class.getClass());
	private final static String expRazonSocial = "^([A-Z]|[a-z]|[0-9]|\\s|Ñ|ñ|!|\"|%|&|'|´|\\-|:|;|>|=|<|@|_|,|\\{|\\}|`|~|á|é|í|ó|ú|Á|É|Í|Ó|Ú|ü|Ü){1,254}$";
	
	private String nombre;
	
	public RazonSocial(){
		nombre = null;
	}
	public RazonSocial(String nombre){
		if(!Valido(nombre))
			throw new IllegalArgumentException("Formato de Razon Social no Valido");
		this.nombre = nombre;
	}
	
	static public boolean Valido(String nombre){
		boolean ret = false;
		Pattern patron = Pattern.compile(expRazonSocial);
		Matcher matcher = patron.matcher(nombre);
		ret = matcher.matches();
		return ret;
	}
	
	@Override
	public boolean sameValueAs(RazonSocial other) {
		return other != null && compara(this.nombre, other.nombre);
	}
}
