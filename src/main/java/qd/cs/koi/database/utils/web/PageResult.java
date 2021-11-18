/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package qd.cs.koi.database.utils.web;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName PageResult
 * @Description 页结果类
 * @Author zhangt2333
 * @Date 2020/2/26 11:29
 * @Version V1.0
 **/

@Data
@AllArgsConstructor
public class PageResult<T> {
    private long total;
    private long totalPage;
    private List<T> rows;

    public PageResult() {
        this.rows = new ArrayList<>();
    }

    public PageResult(Long totalPage, List<T> rows) {
        this.rows = rows;
        this.totalPage = Optional.ofNullable(totalPage).orElse(0L);
        this.total = Optional.ofNullable(rows).map(List::size).orElse(0);
    }

    public PageResult(Integer totalPage, List<T> rows) {
        this((long) Optional.ofNullable(totalPage).orElse(0), rows);
    }
}