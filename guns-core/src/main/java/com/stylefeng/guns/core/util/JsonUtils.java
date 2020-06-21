package com.stylefeng.guns.core.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JsonUtils {
	private static ObjectMapper mapper;

	private static ObjectMapper notNullMappper;
	
	public static synchronized ObjectMapper getMapperInstance() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES ,false);
		}
		return mapper;
	}
	
	public static synchronized ObjectMapper getNotNullMappperInstance() {
		if (notNullMappper == null) {
			notNullMappper = new ObjectMapper();
			notNullMappper.setSerializationInclusion(Include.NON_NULL);
			notNullMappper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES ,false);
		}
		return notNullMappper;
	}

	/**
	 * bean转换为json串
	 * 
	 * @author Bill Chen
	 * @dateTime 2014年9月13日 下午2:24:33
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String beanToJson(Object obj){
		try {
			return getMapperInstance().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * bean转换为json串
	 * 
	 * @author Bill Chen
	 * @dateTime 2014年9月13日 下午2:24:33
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String beanToNotNullJson(Object obj) throws IOException {
		try {
			return getNotNullMappperInstance().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * json串转换为bean实体
	 * 
	 * @author Bill Chen
	 * @dateTime 2014年9月13日 下午2:25:43
	 * @param json
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public static <T> T jsonToBean(String json, Class<T> cls) throws Exception {
		return getMapperInstance().readValue(json, cls);
	}
	/**
	 * json对象转换为bean实体
	 * 
	 * @author Bill Chen
	 * @dateTime 2014年9月13日 下午2:25:43
	 * @param json
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> jsonToBean(JsonNode json, Class<T> cls) throws Exception {
		JavaType javaType = getCollectionType(ArrayList.class, cls);
		if(json.isPojo()){
			return getMapperInstance().convertValue(json, javaType);
		}
		return getMapperInstance().readValue(json.toString(), javaType); 
	}
	
	/**
	 * json串转换为bean实体
	 * 
	 * @author Bill Chen
	 * @dateTime 2014年9月13日 下午2:25:43
	 * @param json
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> jsonStrToBean(String json, Class<T> cls) throws Exception {
		JavaType javaType = getCollectionType(ArrayList.class, cls);
		return getMapperInstance().readValue(json, javaType); 
	}


	/**
	 * 字符串转换JSON对象
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static JsonNode stringToJson(String str) throws Exception {
		return getMapperInstance().readTree(str);
	}
	/**
	 * 字节数组转json
	* @author chenbiao
	* @date 2017年4月13日 下午11:05:34
	* 参数说明 
	* 
	* @param bytes
	* @return
	* @throws Exception
	 */
	public static JsonNode byteArrayToJson(byte[] bytes) throws Exception {
		return getMapperInstance().readTree(bytes);
	}

	/**
	 * 根据属性名称从json串当中获取属性值
	 * 
	 * @author Bill Chen
	 * @dateTime 2014年9月13日 下午2:34:51
	 * @param str json串
	 * @param property 属性名称
	 * @return
	 * @throws Exception
	 */
	public static JsonNode getValue(String str, String property) throws Exception {
		return getMapperInstance().readTree(str).get(property);
	}

	/**
	 * 根据下标从json串当中获取对象
	 * 
	 * @author Bill Chen
	 * @dateTime 2014年9月13日 下午2:35:29
	 * @param str json串
	 * @param index 下标
	 * @return
	 * @throws Exception
	 */
	public static JsonNode getValue(String str, int index) throws Exception {
		return getMapperInstance().readTree(str).get(index);
	}

	/**
	 * json串转换成一个jsonNode
	 * 
	 * @author Bill Chen
	 * @dateTime 2014年9月13日 下午4:46:35
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static JsonNode getValue(String str) throws Exception {
		return getMapperInstance().readTree(str);
	}

	/**
	 * 判断json串当中是否存在制定的KEY
	 * 
	 * @author Bill Chen
	 * @dateTime 2014年9月13日 下午3:43:37
	 * @param str
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static boolean containsKey(String str, String key) throws Exception {
		return getMapperInstance().readTree(str).has(key);
	}

	/**
	 * object covert arrayNode
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static ArrayNode objectToArrayNode(Object obj) throws Exception {
		return (ArrayNode) jsonToBean(beanToJson(obj), obj.getClass());
	}
	/**
	 * java数组转换成json数组
	* @author chenbiao
	* @date 2017年7月10日 下午2:55:38
	* 参数说明 
	* 
	* @param cartIds
	* @return
	* @throws Exception
	 */
	public static ArrayNode arrayToArrayNode(Integer[] cartIds) throws Exception {
		ArrayNode arrayNode = getMapperInstance().createArrayNode();
		for(Integer obj : cartIds){
			arrayNode.add(obj);
		}
		return arrayNode;
	}

	/** 
	 * string covert arrayNode
	 * 
	 * @author Jane Wu
	 * @date 2016年6月30日 下午8:15:48
	 * 参数说明 
	 * 
	 * @param str
	 * @return
	 * @throws Exception  
	 */
	public static ArrayNode stringToArrayNode(String str) throws Exception {
		JsonNode json = getMapperInstance().readTree(str);
		if(json.isArray()){
			return (ArrayNode) json;
		}
		return null;
	}

	public static Map<String,String> jsonToMap(JsonNode json) throws IOException, Exception{
		return getMapperInstance().readValue(beanToJson(json), new TypeReference<Map<String,String>>() {});
	}
	public static Map<String,String> jsonStrToMap(String json) throws IOException, Exception{
		return getMapperInstance().readValue(json, new TypeReference<Map<String,String>>() {});
	}
	
	private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return getMapperInstance().getTypeFactory().constructParametrizedType(collectionClass, collectionClass, elementClasses);
	}
}
