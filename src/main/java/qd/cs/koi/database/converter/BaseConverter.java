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

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@org.mapstruct.MapperConfig
public interface BaseConverter<S, T> {

    /** 基础 DO、DTO 转换方法 **/

    /**
     * 映射同名属性
     */
    T to(S source);

    /**
     * 反向，映射同名属性
     */
    @org.mapstruct.InheritInverseConfiguration(name = "to")
    S from(T target);

    /**
     * 映射同名属性，集合形式
     */
    @org.mapstruct.InheritConfiguration(name = "to")
    List<T> to(List<S> source);

    /**
     * 反向，映射同名属性，集合形式
     */
    @org.mapstruct.InheritConfiguration(name = "from")
    List<S> from(List<T> target);

    /**
     * 映射同名属性，集合流形式
     */
    List<T> to(Stream<S> sourceStream);

    /**
     * 反向，映射同名属性，集合流形式
     */
    List<S> from(Stream<T> targetStream);


    /** 常用 DO、DTO 转换方法 **/

    default List<String> stringToList(String str) {
        return BaseConvertUtils.stringToList(str);
    }

    default String listToString(List<String> list) {
        return BaseConvertUtils.listToString(list);
    }

    default Map<String, String> stringToMap(String str) {
        return BaseConvertUtils.stringToMap(str);
    }

    default String mapToString(Map<String, String> map) {
        return BaseConvertUtils.mapToString(map);
    }
}