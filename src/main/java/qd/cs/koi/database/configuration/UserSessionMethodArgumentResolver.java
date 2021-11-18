/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package qd.cs.koi.database.configuration;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import qd.cs.koi.database.utils.annations.UserSession;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.util.EncrypDES;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

@Slf4j
@Component
public class UserSessionMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    EncrypDES encrypDES;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(UserSession.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        UserSession parameterAnnotation = parameter.getParameterAnnotation(UserSession.class);
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Cookie[] cookies = request.getCookies();
        AssertUtils.notNull(cookies,ApiExceptionEnum.USER_NOT_LOGIN);
        String encodeCookie = null;
        for (Cookie cookie :cookies){
            if(cookie.getName().equals(UserSessionDTO.HEADER_KEY)){
                encodeCookie = cookie.getValue();
            }
        }
        AssertUtils.notNull(encodeCookie, ApiExceptionEnum.USER_NOT_LOGIN);

        String decrypt = encrypDES.decrypt(encodeCookie);
        String decode = URLDecoder.decode(decrypt, "utf-8");

        UserSessionDTO userSessionDTO = JSON.parseObject(decode, new TypeReference<UserSessionDTO>() {
        });

        AssertUtils.notNull(userSessionDTO, ApiExceptionEnum.USER_NOT_LOGIN);
        return userSessionDTO;
    }
}