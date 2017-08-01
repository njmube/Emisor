package com.cubetech.facturador.emisor;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.cubetech.facturador.emisor.AppProperties;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=AppConfigTest.class)

@TestPropertySource("/application.properties")
public class AppPropertiesTest {

	@Autowired
	AppProperties properties;
	
	@Test
	public void test() {
		int i = 0;
		if(i == 1){
			
		}
	}

}
