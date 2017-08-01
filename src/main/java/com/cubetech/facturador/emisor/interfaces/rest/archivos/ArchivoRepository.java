package com.cubetech.facturador.emisor.interfaces.rest.archivos;

import java.util.List;

public interface ArchivoRepository {

	public ArchivoRepDTO findbyCuentaCorrelacion(String cuenta, String correlation);
	public List<ArchivoRepDTO> save(String cuenta,List<ArchivoRepDTO> archivo);
	
}
