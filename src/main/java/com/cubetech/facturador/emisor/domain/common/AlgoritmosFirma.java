package com.cubetech.facturador.emisor.domain.common;

public enum AlgoritmosFirma {
	SHA1RSA("SHA1withRSA"),
	SHA256RSA("SHA256withRSA");
	
	private String text;
	
	AlgoritmosFirma(final String algoritmo){
		this.text = algoritmo;
	}
	@Override
	public String toString(){
		return text;
	}
	
}
