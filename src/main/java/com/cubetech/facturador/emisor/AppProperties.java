package com.cubetech.facturador.emisor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix="app")
public class AppProperties {
	
	private Archivorep archivorep = new Archivorep();

	@Data
	public static class Archivorep{
		private String url;
		private String saveUri;
	}
}
