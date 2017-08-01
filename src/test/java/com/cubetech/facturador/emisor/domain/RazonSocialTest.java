package com.cubetech.facturador.emisor.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cubetech.facturador.emisor.domain.common.RazonSocial;

public class RazonSocialTest {

	@Test
	public void valido() {
		//fail("Not yet implemented");
		
		assertEquals(true, RazonSocial.Valido("CUBETECHNOLOGIC SAS DE CV"));
		assertEquals(false, RazonSocial.Valido("|CUBETECHNOLOGIC SAS DE CV"));
		assertEquals(true, RazonSocial.Valido("CUBE Ññ!\"%&'´-:;<=>@_,{}`~áéíóúÁÉÍÓÚüÜTECHNOLOGIC SAS DE CV"));
		assertEquals(false, RazonSocial.Valido(".CUBETECHNOLOGIC SAS DE CV"));
		assertEquals(true, RazonSocial.Valido("CUBETECHNOLOGIC SAS DE CVksmfklsdmfangkjdnfgmdfngmjsnfdnsjglrnvfjdngdfgbsrjkn,,dfnfjdngsrjngk;rtsntgirengdfngkjrengrengnzdfkgnarel-ngadfkgndz,:mngz-kjdnv  zdsjrengarenjengjerg mdngjdfngkjdzfngkjdfngjznv kzdfgnzdmdhjsndfkjsdfkjdnfdgbjfddbfdbgfdbgfdmnfbfd"));
		assertEquals(false, RazonSocial.Valido("CUBETECHNOLOGIC SAS DE CVksmfklsdmfangkjdnfgmdfngmjsnfdnsjglrnvfjdngdfgbsrjkn,,dfnfjdngsrjngk;rtsntgirengdfngkjrengrengnzdfkgnarel-ngadfkgndz,:mngz-kjdnv  zdsjrengarenjengjerg mdngjdfngkjdzfngkjdfngjznv kzdfgnzdmdhjsndfkjsdfkjdnfdgbjfddbfdbgfdbgfdmnfbfdab"));
	}
	
	@Test( expected = IllegalArgumentException.class)
	public void constructorException(){
		new RazonSocial(".CUBETECHNOLOGIC SAS DE CV");
	}
	
	@Test
	public void constructorOK(){
		new RazonSocial("CUBETECHNOLOGIC SAS DE CV");
	}

}
