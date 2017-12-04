package com.cubetech.facturador.emisor.interfaces;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cubetech.facturador.emisor.application.EmisorService;
import com.cubetech.facturador.emisor.interfaces.facade.dto.EmisorDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.EmitirDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.ImprimirDTO;
import com.cubetech.facturador.emisor.interfaces.facade.dto.RegistroEmisorDTO;

@RestController
public class EmisorController {
	

	@Autowired
	EmisorService emisorService;
	
	@RequestMapping("/Emisor")
	public List<EmisorDTO> consultaEmisores(@RequestHeader(value="cuenta")String cuenta){
		return emisorService.consulta(cuenta);
	}
	
	@RequestMapping("/Emitir/{id}")
	public EmitirDTO consultaDatosEmitir(@RequestHeader(value="cuenta")String cuenta, @PathVariable String id){
		return emisorService.consultaDatosEmitir(cuenta, id);
	}
	
	@RequestMapping("/Emisor/impresion/{id}")
	public ImprimirDTO consultaDatosImprimir(@RequestHeader(value="cuenta")String cuenta, @PathVariable String id){
		return this.emisorService.consultaDatosImprimir(cuenta, id);
	}
	
	@RequestMapping(value="/Emisor", method=RequestMethod.POST)
	public RegistroEmisorDTO registraEmisor(@RequestHeader(value="cuenta") String cuenta, @Valid @RequestBody EmisorDTO emisor){
		return emisorService.creaEmisor(cuenta, emisor);		
	}
	
	
}
