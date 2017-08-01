package com.cubetech.facturador.emisor.application;

import java.util.List;

import com.cubetech.facturador.emisor.interfaces.facade.dto.EmisorDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.RegistroEmisorDTO;

public interface EmisorService {
	public RegistroEmisorDTO creaEmisor(String cuenta, EmisorDTO emisor);
	public List<EmisorDTO> consulta(String cuenta);
	
	//public boolean validaCuenta(String cuenta);
}
