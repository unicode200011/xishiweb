package com.stylefeng.guns.admin.core.util;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
	private String status;
	private String message;
	private T result;

	public Result(String status, String message, T result){
		this.status = status;
		this.message = message;
		this.result = result;
		if ("".equals(result)) this.result = (T) new JSONObject();
	}

	public Result(){

	}

	public static Result<Object> ofSuccess(){
		return new Result<>("200", "成功", "");
	}

	public static Result<Object> ofSuccess(Object data){
		return new Result<>("200", "成功", data);
	}

	public static Result<Object> ofFail(String message){
		return new Result("501", message, "");
	}
}
