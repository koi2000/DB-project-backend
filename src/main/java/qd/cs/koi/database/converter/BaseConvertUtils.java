/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package qd.cs.koi.database.converter;


import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import org.springframework.util.CollectionUtils;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description DTO-DO 特殊转换方法的统一收口处
 * @author zhangt2333
 **/
public class BaseConvertUtils {

    /** 常用 DO、DTO 转换方法 **/

    public static List<String> stringToList(String str) {
        return StringUtils.isBlank(str) ? null : Arrays.stream(str.split(",")).collect(Collectors.toList());
    }

    public static Set<String> stringToSet(String str) {
        return StringUtils.isBlank(str) ? null : Arrays.stream(str.split(",")).collect(Collectors.toSet());
    }

    public static String listToString(List<String> list) {
        return collectionToString(list);
    }

    public static String setToString(Set<String> list) {
        return collectionToString(list);
    }

    private static String collectionToString(Collection<String> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }

        AssertUtils.isTrue(collection.stream().allMatch(o -> StringUtils.containsNone(o, ',')), ApiExceptionEnum.PARAMETER_ERROR,
                "字段中不能包含 , ");
        return StringUtils.join(collection, ',');
    }

    public static Map<String, String> stringToMap(String str) {
        return StringUtils.isBlank(str) ? null : Arrays.stream(str.split(";")).collect(Collectors.toMap(s -> s.substring(0, s.indexOf(":")), s -> s.substring(s.indexOf(":") + 1), (k1, k2) -> k1));
    }

    public static String mapToString(Map<String, String> map) {
        if (CollectionUtils.isEmpty(map)) {
            return null;
        }
        AssertUtils.isTrue(map.entrySet().stream().allMatch(o ->
            StringUtils.containsNone(o.getKey(), ';', ':') && StringUtils.containsNone(o.getValue(), ';', ':')
        ), ApiExceptionEnum.PARAMETER_ERROR, "字段中不能包含 ';', ':' ");
        return StringUtils.join(map.entrySet().stream().map(entry -> entry.getKey() + ":" + entry.getValue()).collect(Collectors.toList()), ";");
    }

    public static List<Long> bytesToLongList(byte[] bytes) {
        int size = bytes != null ? bytes.length : 0;
        if (size == 0 || size % 8 != 0) {
            return Lists.newArrayList();
        }
        ByteBuffer wrap = ByteBuffer.wrap(bytes);
        List<Long> idList = new ArrayList<>(size / 8);
        for (int i = 0; i < size; i += 8) {
            idList.add(wrap.getLong(i));
        }
        return idList;
    }


}