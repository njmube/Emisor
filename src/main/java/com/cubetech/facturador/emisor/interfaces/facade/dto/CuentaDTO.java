package com.cubetech.facturador.emisor.interfaces.facade.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CuentaDTO {
	private String correlacion;
	private List<EmisorDTO> emisores = new ArrayList<EmisorDTO>();
}
