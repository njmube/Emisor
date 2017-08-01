package com.cubetech.facturador.emisor.interfaces.facade.internal.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DatosJsonDeserializer extends JsonDeserializer<List<String>>{

	@Override
	public List<String> deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		 if (jp.getCurrentToken() == JsonToken.START_ARRAY) {
       List<String> permissions = new ArrayList<>();
       String tmp = "";
       while(jp.nextToken() != JsonToken.END_ARRAY) {
      	 if(jp.currentToken() == JsonToken.START_OBJECT){
      		 tmp = "{";
      	 }else if(jp.currentToken() == JsonToken.END_OBJECT){
      		 tmp = tmp + "}";
      		 permissions.add(tmp);
      	 }
      	 else if(jp.currentToken() == JsonToken.FIELD_NAME){
      		 tmp = tmp + jp.getValueAsString() + ":";
      	 }
       }
       return permissions;
   }
   throw ctxt.mappingException("Expected Permissions list");

	}
	


}
