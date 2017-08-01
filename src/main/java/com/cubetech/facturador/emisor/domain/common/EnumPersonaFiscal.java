package com.cubetech.facturador.emisor.domain.common;

public enum EnumPersonaFiscal {
	FISICA(1),
	MORAL(2);
	
	//true fisica
	private int valor;
	
	EnumPersonaFiscal(int valor){
		this.valor = valor;
	}
	
	public int getValue(){
		return this.valor;
	}
	
}
