package com.cubetech.facturador.emisor.domain.catalogo;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class Catalogo {
	@Column(name="clave")
	private String claveSat;
	
	@Transient
	private String descripcion;
	
	@Transient
	private boolean vigente;
	
	public boolean sameValueAs(Catalogo other){
		return this.claveSat.equals(other.claveSat);
	}
	
}
