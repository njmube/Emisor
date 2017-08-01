package com.cubetech.facturador.emisor.domain.common;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.ValidationException;

import com.cubetech.facturador.emisor.domain.catalogo.RegimenFiscal;

import lombok.Data;

@Data
@Embeddable
public class PersonaFiscal{
	
	@Transient
	private EnumPersonaFiscal tipo;
	
	private Rfc rfc;
	private RazonSocial razonSocial;
	
	public boolean sameValueAs(PersonaFiscal other) {
		boolean ret = true;
		 ret = rfc.canEqual(other.rfc);
		 ret = ret && ((razonSocial == null && other.razonSocial == null)  || (razonSocial != null && razonSocial.sameValueAs(other.razonSocial)));
		 return ret;
	}
	
	public void validaRegimenFiscal(RegimenFiscal regimenFisal) throws ValidationException{
		tipo = this.rfc.tipoPersonaFiscal();
		
		regimenFisal.valida(tipo);
		
	}
	
	public void setRfc(Rfc rfc){		
		this.rfc = rfc;
		//tipo = rfc.tipoPersonaFiscal();
	}
	
}
