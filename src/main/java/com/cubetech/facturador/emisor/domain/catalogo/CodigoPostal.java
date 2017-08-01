package com.cubetech.facturador.emisor.domain.catalogo;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import com.cubetech.facturador.emisor.domain.shared.ValueObject;

import lombok.Data;

@Embeddable
@Data
public class CodigoPostal extends Catalogo implements ValueObject<CodigoPostal>{
	
	@Transient
	private String estado;
	@Transient
	private String municipio;
	@Transient
	private String localidad;
	@Override
	public boolean sameValueAs(CodigoPostal other) {
		// TODO Auto-generated method stub
		return super.sameValueAs(other);
	}

}
