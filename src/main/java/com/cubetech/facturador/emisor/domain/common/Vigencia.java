package com.cubetech.facturador.emisor.domain.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.cubetech.facturador.emisor.domain.shared.ValueObject;

import lombok.Data;

@Data
@Embeddable
public class Vigencia implements ValueObject<Vigencia> {

	@Column(name = "inicial")
	private Date fechaInicial;
	
	@Column(name="final")
	private Date fechaFinal;
	
	public Vigencia(Date ini, Date fin){
		this.fechaInicial = ini;
		this.fechaFinal = fin;
	}
	public Vigencia(Date ini){
		this.fechaInicial = ini;
		this.fechaFinal = null;
	}
	public Vigencia(){
		this.fechaInicial = null;
		this.fechaFinal = null;
	}
	
	@Override
	public boolean sameValueAs(Vigencia other) {
		boolean ret = false;

		ret = (this.fechaInicial.equals(other.fechaInicial) && this.fechaFinal.equals(other.fechaFinal));

		return ret;
	}

	public boolean vigente() {
		boolean ret = false;
		Date ahora = new Date();
		
		ret = this.vigenteEn(ahora);
		
		return ret;
	}

	public boolean vigenteEn(Date fecha) {
		boolean ret = false;

		if (this.fechaInicial != null)
			ret = this.fechaInicial.before(fecha);

		if (fechaFinal != null)
			ret = ret && this.fechaFinal.after(fecha);

		return ret;
	}

	public String fechaLegible(){
		String ret;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		ret = "Vigencia:[" + formatter.format(fechaInicial) + " - " + formatter.format(fechaFinal) + "]";
		
		return ret;
	}
}
