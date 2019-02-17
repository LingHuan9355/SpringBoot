package com.mmall.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : JsonData Created with IDEA
 * @author: CarlLing
 * @CreateDate : 2019-01-11 23:21
 * @Description : 请求返回格式
 */
@Getter
@Setter
public class JsonData {

  // 返回数据
  private boolean ret;

  // 返回信息提示
  private String msg;

  // 正常返回数据
  private Object data;

  public JsonData(boolean ret) {
    this.ret = ret;
  }

  public static JsonData success(Object object, String msg) {
    JsonData jsonData = new JsonData(true);
    jsonData.data = object;
    jsonData.msg = msg;
    return jsonData;
  }

  public static JsonData success(Object object) {
    JsonData jsonData = new JsonData(true);
    jsonData.data = object;
    return jsonData;
  }

  public static JsonData success() {
    return new JsonData(true);
  }

  public static JsonData fail(String msg) {
    JsonData jsonData = new JsonData(false);
    jsonData.msg = msg;
    return jsonData;
  }

  public Map<String, Object> toMap() {
    HashMap<String, Object> result = new HashMap<String, Object>();
    result.put("ret", ret);
    result.put("msg", msg);
    result.put("data", data);
    return result;
  }
}
