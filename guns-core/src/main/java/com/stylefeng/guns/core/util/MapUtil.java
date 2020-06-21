package com.stylefeng.guns.core.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map工具类，链式调用生成map
 *
 * old style:
 *      Map map = new HashMap<>();
 *      map.put("", "");
 *      map.put("", "");
 *      map.putAll(otherMap);
 *
 * new style:
 *      MapUtil.build.put("", "").put("", "").putAll(otherMap).over();
 */
public class MapUtil {

    private Map map;

    private MapUtil() {

    }

    public static MapUtil build() {
        MapUtil instance = new MapUtil();
        instance.map = new HashMap<>();
        return instance;
    }

    public static MapUtil build(Map map) {
        MapUtil instance = new MapUtil();
        instance.map = map;
        return instance;
    }

    public static MapUtil buildLink() {
        MapUtil instance = new MapUtil();
        instance.map = new LinkedHashMap<>();
        return instance;
    }

    public MapUtil put(Object key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public MapUtil putAll(Map map) {
        this.map.putAll(map);
        return this;
    }

    public Map over() {
        return this.map;
    }

    public static void main(String[] args) {
        Map map = MapUtil.build().put("a", "11").put("b", "11").over();
        System.out.println(map);
    }

}
