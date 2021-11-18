package qd.cs.koi.database.configuration;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import qd.cs.koi.database.utils.entity.UserSessionDTO;
import qd.cs.koi.database.utils.util.EncrypDES;
import qd.cs.koi.database.utils.web.ApiExceptionEnum;
import qd.cs.koi.database.utils.web.AssertUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Optional;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    EncrypDES encrypDES;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        /* 地址过滤 */
        //String bodyString = HttpHelper.getBodyString(request);


        String uri = HttpHelper.getUri(request);
        if (uri.contains("/login") || uri.contains("/register")) {
            return true;
        }

        Cookie[] cookies = request.getCookies();
        AssertUtils.notNull(cookies, ApiExceptionEnum.USER_NOT_LOGIN);
        String encodeCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(UserSessionDTO.HEADER_KEY)) {
                encodeCookie = cookie.getValue();
            }
        }
        AssertUtils.notNull(encodeCookie, ApiExceptionEnum.USER_NOT_LOGIN);

        String decrypt = encrypDES.decrypt(encodeCookie);
        String decode = URLDecoder.decode(decrypt, "utf-8");

        UserSessionDTO userSessionDTO = JSON.parseObject(decode, new TypeReference<UserSessionDTO>() {
        });

        AssertUtils.notNull(userSessionDTO, ApiExceptionEnum.USER_NOT_LOGIN);
        //bodyString = HttpHelper.getBodyString(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
