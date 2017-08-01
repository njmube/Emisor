package com.cubetech.facturador.emisor.domain.cuenta;

import java.util.UUID;

import com.cubetech.facturador.emisor.domain.common.Archivo;
import com.cubetech.facturador.emisor.domain.shared.ValueObject;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ArchivoCuenta extends Archivo implements ValueObject<ArchivoCuenta>{

	private String correlacion;
	
	@Override
	public boolean sameValueAs(ArchivoCuenta other) {
		return super.sameValueAs(other);
	}
	
	public void correlacion(){
		if(this.correlacion == null || this.correlacion.isEmpty())
			this.correlacion = UUID.randomUUID().toString();
	}
	
	public void correlacion(ArchivoCuenta other){
		if(other!= null && other.correlacion != null && !other.correlacion.isEmpty()){
			this.correlacion = other.correlacion;
		}else{
			correlacion();
		}
	}

	public void actualiza(ArchivoCuenta other){
		super.actualiza(other);
	}
}
