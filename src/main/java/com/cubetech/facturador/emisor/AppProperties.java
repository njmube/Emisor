package com.cubetech.facturador.emisor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix="app")
public class AppProperties {
	
	private String catalogourl;
	private String archivourl;
	private String emisorurl;
	private String timbreurl;
	
}
