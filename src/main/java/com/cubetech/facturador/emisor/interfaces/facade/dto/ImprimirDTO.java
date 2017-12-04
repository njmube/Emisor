package com.cubetech.facturador.emisor.interfaces.facade.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ImprimirDTO extends DireccionDTO {
	private String logo;
}
