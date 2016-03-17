/**
 * 
 */
package com.simbest.cores.utils.json;

import java.util.Date;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * @author lishuyi
 *
 */
public class JacksonObjectMapperConfig extends ObjectMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5665435614319670909L;

	public JacksonObjectMapperConfig() {
		super();
		
		this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);		
		//this.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false); 不增加，避免key值为null，而避免节点消失
		this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true); 
		
		//this.setSerializationInclusion(Include.NON_EMPTY); //对象转字符串时，只转化非空字段 zjs 需要占位
		
		SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new CustomJsonDateDeseralizer());
        // Add more here ...
        registerModule(module);
		
	}

}
