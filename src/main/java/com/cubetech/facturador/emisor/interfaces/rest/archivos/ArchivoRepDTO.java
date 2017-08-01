package com.cubetech.facturador.emisor.interfaces.rest.archivos;

import lombok.Data;

@Data
public class ArchivoRepDTO {
	private String correlacion;
	private String nombre;
	private String content;
	private String tipo;
	private String id;
}
