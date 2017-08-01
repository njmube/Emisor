package com.cubetech.facturador.emisor.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ComprobantesFiscalesInterceptor implements HandlerInterceptor{

	private final static Logger logger = LoggerFactory.getLogger(ComprobantesFiscalesInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
		// TODO Auto-generated method stub
		String cuenta;
		
		UUID idRequest = UUID.randomUUID();
		MDC.put("uuid", idRequest.toString());
		cuenta = request.getHeader("cuenta");
		if(cuenta != null)
			MDC.put("cta", cuenta);
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {
		MDC.clear();
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
