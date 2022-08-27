package com.itblare.itools.collection;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.collection
 * ClassName:   StringMap
 * Author:   Blare
 * Date:     Created in 2021/4/17 23:42
 * Description:    字符串转MAP
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/17 23:42    1.0.0             字符串转MAP
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 字符串转MAP
 *
 * @author Blare
 * @create 2021/4/17 23:42
 * @since 1.0.0
 */
public class StringMap {

    private final Map<String, Object> map;

    public StringMap() {
        this(new HashMap<>());
    }

    public StringMap(Map<String, Object> map) {
        this.map = map;
    }

    public StringMap put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public StringMap putNotBlank(String key, String value) {
        if (Objects.nonNull(value) && !"".equals(value)) {
            map.put(key, value);
        }
        return this;
    }

    public StringMap putNotNull(String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
        return this;
    }


    public StringMap putWhen(String key, Object val, boolean when) {
        if (when) {
            map.put(key, val);
        }
        return this;
    }

    public StringMap putAll(Map<String, Object> map) {
        this.map.putAll(map);
        return this;
    }

    public StringMap putAll(StringMap map) {
        this.map.putAll(map.map);
        return this;
    }

    public int size() {
        return map.size();
    }

    public Map<String, Object> map() {
        return this.map;
    }

    public Object get(String key) {
        return map.get(key);
    }

    public Set<String> keySet() {
        return map.keySet();
    }
}
