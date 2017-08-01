package com.cubetech.facturador.emisor.interfaces.facade.dto;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude="password")
public class CertificadoDTO {
	@NotEmpty
	private String password;
	@NotEmpty
	private ArchivoDTO privado;
	@NotEmpty
	private ArchivoDTO Publico;
}
