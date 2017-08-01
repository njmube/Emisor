package com.cubetech.facturador.emisor.interfaces.facade.dto.catalogos;


import java.util.Map;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper=true)
public class RegimenFiscalDTO extends CatalogoDTO {
	
	private static final String NAME = "regimenFiscal";
	
	private boolean fisica;
	private boolean moral;
	
	@Override
	public String url() {
		return NAME + "=" + super.getClaveSat();
	}
	@Override
	public String getName(){
		return NAME;
	}
	
	public RegimenFiscalDTO(Map<?,?> map){
		super(map);
		setFisica((boolean) map.get("fisica"));
		setMoral((boolean) map.get("moral"));
	}
	public RegimenFiscalDTO(){
		
	}
}
