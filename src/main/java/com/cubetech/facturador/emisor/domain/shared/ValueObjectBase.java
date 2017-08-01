package com.cubetech.facturador.emisor.domain.shared;

public abstract  class ValueObjectBase {
	
	protected boolean compara(String a, String b){
		boolean ret = true;
		
		ret = (a == null && b == null);
		ret = ret || (!((a != null && b == null) || (b!= null && a == null)) && a.equals(b));
		
		return ret;
	}
}
