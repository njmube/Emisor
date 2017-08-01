package com.cubetech.facturador.emisor.interfaces.facade.dto;

import lombok.Data;

@Data
public class RegistroEmisorDTO {
	private String numeroCertificado;
	private String fechaInicial;
	private String fechaFinal;
}
