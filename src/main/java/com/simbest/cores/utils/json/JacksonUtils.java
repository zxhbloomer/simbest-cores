package com.simbest.cores.utils.json;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.simbest.cores.exceptions.Exceptions;

/**
 * 
 * @author lishuyi
 *
 */
public class JacksonUtils {
	private static transient final Log log = LogFactory.getLog(JacksonUtils.class);
	
	public static ObjectMapper mapper = null;
	
	static{
		mapper = new ObjectMapper();
		//mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false); 不增加，避免key值为null，而避免节点消失
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);  
		
		//mapper.setSerializationInclusion(Include.NON_EMPTY); //对象转字符串时，只转化非空字段 zjs 需要占位
		
		SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new CustomJsonDateDeseralizer());
        // Add more here ...
        mapper.registerModule(module);
		
	}
	
	/**
	 * 将对象实体转换为Json字符串
	 * @param obj
	 * @return
	 */
	public static String writeValueAsString(Object obj){
		if(obj!=null){
			try {
				return mapper.writeValueAsString(obj);
			} catch (JsonProcessingException e) {
				log.error(String.format("Error 12001: Object source is %s", obj));
				log.error(Exceptions.getStackTraceAsString(e));
			}
		}
		return null;
	}
	
	/**
	 * 将Json字符串转换为对象实体
	 * @param jsonStr
	 * @param clazz
	 * @return
	 */
	public static <T> T readValue(String jsonStr, Class<T> clazz){
		try {
			return mapper.readValue(jsonStr, clazz);
		} catch (IOException e) {
			log.error(String.format("Error 12000: Json source is %s, translate class is %s", jsonStr, clazz));
			Exceptions.printException(e);
		}
		return null;
	} 
	
	/**
	 * 将Json字符串转换为对象实体列表
	 * @param jsonStr
	 * @param listType
	 * @return
	 */
	public static <T> T readListValue(String jsonStr, TypeReference<T> listType){
		T result = null;
		if(jsonStr!=null && listType!=null){
			try {
				result = mapper.readValue(jsonStr, listType);
			} catch (IOException e) {			
				log.error(String.format("Error 12000: failed translate Json source %s to list object", jsonStr));
				log.error(Exceptions.getStackTraceAsString(e));
			}
		}
		return result;
	}
	
	/**
	 * 将Json字符串转换为对象实体Map集合
	 * @param jsonStr
	 * @param mapType
	 * @return
	 */
	public static <T> T readMapValue(String jsonStr, TypeReference<T> mapType){
		T result = null;
		try {
			result = mapper.readValue(jsonStr, mapType);
		} catch (IOException e) {			
			log.error(String.format("Error 12000: failed translate Json source %s to map object", jsonStr));
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return result;
	}
	
	/**
	 * 构造泛型JavaType
	 * @param parametrized
	 * @param parameterClasses
	 * @return
	 */
	public static JavaType createGenericJavaType(Class<?> parametrized, Class<?>... parameterClasses){
		return mapper.getTypeFactory().constructParametrizedType(parametrized, parametrized, parameterClasses);
		//return mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
	}
	
	/**
	 * 自定义转换类型
	 * @param jsonStr
	 * @param valueType
	 * @return
	 */
    public static <T> T readValue(String jsonStr, JavaType valueType){
    	T result = null;
		if(jsonStr!=null && valueType!=null){
			try {
				result = mapper.readValue(jsonStr, valueType);
			} catch (IOException e) {			
				log.error(String.format("Error 12000: failed translate Json source %s to list object", jsonStr));
				log.error(Exceptions.getStackTraceAsString(e));
			}
		}
		return result;
    } 
    
    public static String formatJson(String jsonStr){
    	String result = null;
		try {
			Object json = mapper.readValue(jsonStr, Object.class);
			result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		} catch (IOException e) {
			Exceptions.printException(e);
		}	
    	return result;
    }
}
