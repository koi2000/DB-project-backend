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
import lombok.Getter;

/**
 * @ClassName ExceptionEnum
 * @Description API异常枚举类，需要客户端处理
 * @Author zhangt2333
 * @Date 2020/2/26 11:29
 * @Version V1.0
 **/

@Getter
@AllArgsConstructor
public enum ApiExceptionEnum {
    PARAMETER_ERROR(400, "请求参数错误"),

    UNKNOWN_ERROR(500, "服务器出错"),
    INTERNAL_ERROR(500, "内部服务出错"),
    SERVER_BUSY(500, "服务器正忙，请重试"),

    GATEWAY_ERROR(500, "网关错误"),
    GET_IP_ERROR(500, "获取不到IP"),

    USER_NOT_FOUND(400, "该用户不存在"),
    USER_NOT_LOGIN(403, "该用户未登录"),
    USER_NOT_MATCHING(403, "用户权限不足"),

    GROUP_NOT_FOUND(400, "该用户组不存在"),
    STATUS_NOT_FOUND(400, "申请状态不存在"),
    GROUP_IS_PRIVATE(400, "该用户组是私有的"),
    USER_IS_OWNER(400, "用户是组长"),

    PASSWORD_NOT_MATCHING(400, "该用户账号或密码错误"), // 不能直接提示密码错误哦
    NEWPASSWORD_LENGTH_ERROR(400, "新密码长度不在4~32位间"),

    CONTENT_IS_BLANK(500, "请求文件内容为空"),
    FILE_WRITE_ERROR(500, "文件写入错误"),
    FILE_READ_ERROR(500, "文件读出错误"),
    FILE_NOT_DOUBLE(400, "文件不配对"),
    FILE_NOT_EXISTS(400, "文件不存在"),
    FILE_FORMAT_ERROR(400, "文件格式错误"),
    FILE_IS_BLANK(400, "上传内容为空"),
    FILE_MD5_EXISTS(400, "文件md5已存在"),
    FILE_TOO_LARGE(400, "文件过大，无法预览，请选择下载"),
    FILE_NOT_MATCH(500, "文件md5相同但大小不相同，请联系管理员"),

    TOKEN_EXPIRE(400, "验证令牌过期或不存在"),

    CAPTCHA_NOT_FOUND(400, "验证码不存在"),
    CAPTCHA_NOT_MATCHING(400, "验证码不匹配"),

    USER_EXIST(400, "用户名已存在或邮箱已被使用"),
    EMAIL_EXIST(400, "邮箱已存在"),

    HISTORY_IS_NULL(400,"借阅记录不存在"),
    REBORROW_TIME_ERROR(400,"续借时间出错"),
    USER_NOT_MATCH(400,"续借时间出错"),
    HISTORY_NOT_FOUND(400,"借书记录不存在"),
    TIME_TOO_LONG(400,"借书时间过长"),
    BOOK_NOT_ENOUGH(400,"书籍数量不足"),
    BOOK_NOT_FOUND(400,"书籍不存在");


    public int code;
    public String message;
}