package com.cubetech.facturador.emisor;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cubetech.facturador.emisor.interceptor.ComprobantesFiscalesInterceptor;
import com.cubetech.facturador.emisor.interfaces.facade.internal.assembler.CatalogosDTOAssembler;
import com.cubetech.facturador.emisor.interfaces.facade.internal.assembler.EmisorDtoTOEmisor;


@Configuration
public class AppConfig extends WebMvcConfigurerAdapter{
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new EmisorDtoTOEmisor());
	  return modelMapper;
	}
	
	@Bean
	public CatalogosDTOAssembler catalogosDTOAssembler(){
		CatalogosDTOAssembler catalogosDTOAssembler = new CatalogosDTOAssembler();
		return catalogosDTOAssembler;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry){
		registry.addInterceptor(new ComprobantesFiscalesInterceptor());
	}
	
}
