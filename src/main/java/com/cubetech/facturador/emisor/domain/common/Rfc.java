package com.cubetech.facturador.emisor.domain.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.ValidationException;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cubetech.facturador.emisor.domain.shared.ValueObject;

import lombok.Data;

@Data
@Embeddable
public class Rfc implements ValueObject<Rfc> {

	private final static Logger logger = LoggerFactory.getLogger(Rfc.class.getClass());
	
	private static final String expRFC =  "^([A-Z&Ñ]{3,4})(\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01]))([A-Z\\d]{2})([A\\d])$";
	private static final String expFisicaRFC =  "^([A-Z&Ñ]{4})(\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01]))([A-Z\\d]{2})([A\\d])$";
	private static final String expMoralRFC =  "^([A-Z&Ñ]{3})(\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01]))([A-Z\\d]{2})([A\\d])$";
	
	@Column(nullable = false)
	private String rfc; 
	
	public Rfc(String rfc){
		rfc = rfc.toUpperCase();
		Validate.notNull(rfc);
		Validate.notEmpty(rfc);
		if(!Valido(rfc)){
			throw new IllegalArgumentException("Formato de RFC no Valido");
		}
		this.rfc = rfc;
	}
	
	@Override
	public boolean sameValueAs(Rfc other) {
		// TODO Auto-generated method stub
		return rfc.equals(other.rfc);
	}
	
	static public boolean esPersonaFisica(String rfc){
		return Pattern.matches(expFisicaRFC, rfc);
	}
	static public boolean esPersonaMoral(String rfc){
		return Pattern.matches(expMoralRFC, rfc);
	}
	
	static public boolean Valido(String rfc){
		boolean ret = false;
		Pattern patron = Pattern.compile(expRFC);
		Matcher matcher = patron.matcher(rfc);
		ret = matcher.matches();
		ret = ret && ValidaFecha(matcher.group(2));
		return ret;
	}
	public boolean valido(){
		boolean ret = false;
		ret = Rfc.Valido(this.rfc);
		return ret;
	}
	
	static public boolean ValidaFecha(String fecha){
		boolean ret = false;
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
		Date currentDate = new Date();
		
		try{
			Date date = formatter.parse(fecha);
			ret = date.before(currentDate);
		}catch (ParseException|NullPointerException e){
			e.printStackTrace();
		}
		return ret;
	}
	
	public String rfc(){
		return rfc;
	}
	
	public Rfc(){
		
	}
	
	public EnumPersonaFiscal tipoPersonaFiscal(){
		
		if(Rfc.esPersonaFisica(this.rfc))
			return EnumPersonaFiscal.FISICA;
		else if(Rfc.esPersonaMoral(this.rfc))
			return EnumPersonaFiscal.MORAL;
		throw new ValidationException("RFC no valido: " + this.toString());
	}

}
