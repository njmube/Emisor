package com.cubetech.facturador.emisor.interfaces;

import java.util.List;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cubetech.facturador.emisor.application.EmisorService;
import com.cubetech.facturador.emisor.interfaces.facade.dto.EmisorDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.RegistroEmisorDTO;

@RestController
public class EmisorController {
	
	private final static Logger logger = LoggerFactory.getLogger(EmisorController.class);

	@Autowired
	EmisorService emisorService;
	
	@RequestMapping("/Emisor")
	public List<EmisorDTO> consultaEmisores(@RequestHeader(value="cuenta")String cuenta){
		return emisorService.consulta(cuenta);
	}
	
	@RequestMapping(value="/Emisor", method=RequestMethod.POST)
	public RegistroEmisorDTO registraEmisor(@RequestHeader(value="cuenta") String cuenta, @Valid @RequestBody EmisorDTO emisor){
		return emisorService.creaEmisor(cuenta, emisor);		
	}
	
	
}
