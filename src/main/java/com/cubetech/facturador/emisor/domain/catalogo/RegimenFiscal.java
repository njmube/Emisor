package com.cubetech.facturador.emisor.domain.catalogo;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.ValidationException;

import com.cubetech.facturador.emisor.domain.common.EnumPersonaFiscal;
import com.cubetech.facturador.emisor.domain.common.Rfc;
import com.cubetech.facturador.emisor.domain.shared.ValueObject;

import lombok.Data;

@Data
@Embeddable
public class RegimenFiscal extends Catalogo implements ValueObject<RegimenFiscal>{
	
	@Transient
	private boolean fisica;
	@Transient
	private boolean moral;
	
	@Override
	public boolean sameValueAs(RegimenFiscal other) {
		return super.sameValueAs(other);
	}
	
	public void valida(EnumPersonaFiscal e){
		if(!this.isVigente())
			throw new ValidationException("El regimen fiscal: " + this.getDescripcion() + " no se encutra vigente");
		if(e == EnumPersonaFiscal.FISICA){
			if(!this.fisica)
				throw new ValidationException("El RFC no corresponde con el regimen fiscal " + this.toString());
		}else if(e == EnumPersonaFiscal.MORAL){
			if(!this.moral)
				throw new ValidationException("El RFC no corresponde con el regimen fiscal " + this.toString());
		}
	}

}
