package com.cubetech.facturador.emisor.interfaces.facade.internal.assembler;

import org.modelmapper.PropertyMap;

import com.cubetech.facturador.emisor.domain.cuenta.Emisor;
import com.cubetech.facturador.emisor.interfaces.facade.dto.EmisorDTO;


public class EmisorDtoTOEmisor extends PropertyMap<EmisorDTO, Emisor>{	

	
	@Override
	protected void configure() {
		map().getLogo().setContent(source.getLogo().toBytes());
		map().getCertificado().getPrivado().setContent(source.getCertificado().getPrivado().toBytes());
		map().getCertificado().getPublico().setContent(source.getCertificado().getPublico().toBytes());
		map().getEmisor().getRazonSocial().setNombre(source.getNombre());
	}

}
