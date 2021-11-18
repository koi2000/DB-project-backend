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

@AllArgsConstructor
public enum AcceptedEnum {
    OK(0, "成功"),
    ERROR(1, "失败")  // 需要服务端处理的异常
    ;

    public int code;
    public String message;
}
