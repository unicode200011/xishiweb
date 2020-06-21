package com.stylefeng.guns.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 集合链式调用类;
 * @author: LEIYU
 */
public class ListUtil {

    private List list;

    private ListUtil() {
    }

    public static ListUtil build() {
        ListUtil instance = new ListUtil();
        instance.list = new ArrayList();
        return instance;
    }

    public ListUtil add(Object value) {
        this.list.add(value);
        return this;
    }
    public ListUtil addAll(List collection) {
        this.list.addAll(collection);
        return this;
    }

    public List over() {
        return this.list;
    }
}
