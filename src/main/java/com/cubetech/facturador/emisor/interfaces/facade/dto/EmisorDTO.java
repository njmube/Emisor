package com.cubetech.facturador.emisor.interfaces.facade.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
public class EmisorDTO {
	@NotEmpty
	private String correlacion;
	@NotEmpty
	@Length(max=14)
	private String rfc;
	private String nombre;
	@NotEmpty
	private String regimenFiscal;
	@NotEmpty
	private String LugarExpedicion;
	private ArchivoDTO logo;
	private DireccionDTO direccion;
	
	private CertificadoDTO certificado;	
}
