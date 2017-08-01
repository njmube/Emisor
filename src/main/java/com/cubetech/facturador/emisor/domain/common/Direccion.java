package com.cubetech.facturador.emisor.domain.common;

import javax.persistence.Embeddable;

import com.cubetech.facturador.emisor.domain.shared.ValueObject;
import com.cubetech.facturador.emisor.domain.shared.ValueObjectBase;

import lombok.Data;

@Data
@Embeddable
public class Direccion extends ValueObjectBase implements ValueObject<Direccion> {

	private String calle;
	private String numeroExterior;
	private String numeroInterior;
	private String codigoPostal;
	private String colonia;
	private String pais;
	private String estado;
	private String municipio;
	
	@Override
	public boolean sameValueAs(Direccion other) {
		
		return other != null && (
			compara(this.calle, other.calle) 									 && 
			compara(this.numeroExterior, other.numeroExterior) &&
			compara(this.numeroInterior, other.numeroInterior) && 
			compara(this.codigoPostal, other.codigoPostal)	   &&
			compara(this.colonia, other.colonia) 							 && 
			compara(this.pais, other.pais)									   &&
			compara(this.estado, other.estado)								 && 
			compara(this.municipio, other.municipio)
			)
			;
	}
}
