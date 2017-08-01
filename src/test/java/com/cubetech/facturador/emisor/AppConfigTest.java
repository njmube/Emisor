package com.cubetech.facturador.emisor;

import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cubetech.facturador.emisor.AppProperties;
import com.cubetech.facturador.emisor.interceptor.ComprobantesFiscalesInterceptor;
import com.cubetech.facturador.emisor.interfaces.facade.CatalogosService;
import com.cubetech.facturador.emisor.interfaces.facade.internal.CatalogosServiceImpl;
import com.cubetech.facturador.emisor.interfaces.facade.internal.assembler.CatalogosDTOAssembler;
import com.cubetech.facturador.emisor.interfaces.facade.internal.assembler.EmisorDtoTOEmisor;
import com.cubetech.facturador.emisor.interfaces.rest.archivos.ArchivoRepository;
import com.cubetech.facturador.emisor.interfaces.rest.archivos.impl.ArchivoRepositoryImpl;
import com.cubetech.facturador.emisor.interfaces.rest.client.CatalogosRepository;


@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class AppConfigTest extends WebMvcConfigurerAdapter{

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
	
	@Bean
	public CatalogosRepository restCatalogClient(){
		return new CatalogosRepository();
	}
	@Bean
	public CatalogosService catalogosService(){
		return new CatalogosServiceImpl();
	}
	@Bean
	public ArchivoRepository archivoRepository(){
		return new ArchivoRepositoryImpl();
	}
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry){
		registry.addInterceptor(new ComprobantesFiscalesInterceptor());
	}
	
}
