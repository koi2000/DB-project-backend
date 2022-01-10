package qd.cs.koi.database.configuration;


import org.springframework.stereotype.Component;
import qd.cs.koi.database.utils.web.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*",filterName = "corsFilter")
public class CorsFilter implements Filter {

    final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CorsFilter.class);

    @Override

    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

            req.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
            HttpServletResponse response = (HttpServletResponse) resp;
            response.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) req).getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Content-Disposition,Origin, X-Requested-With, Content-Type, Accept,Authorization,id_token");
            response.setHeader("Access-Control-Allow-Credentials","true");
            response.setHeader("Content-Security-Policy", "default-src 'self' 'unsafe-inline'; script-src 'self'; frame-ancestors 'self'; object-src 'none'");
            response.setHeader("X-Content-Type-Options", "nosniff");
            response.setHeader("X-XSS-Protection", "1; mode=block");

            if(((HttpServletRequest) req).getMethod().equals("OPTIONS")) {
                response.setStatus(HttpStatus.OK.value());

                // hresp.setContentLength(0);
                response.getWriter().write("OPTIONS returns OK");

                return;
            }

            chain.doFilter(req, response);
//        try {
//            HttpServletRequest hreq = (HttpServletRequest) req;
//            HttpServletResponse hresp = (HttpServletResponse) resp;
//            //跨域
//            hresp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
//            //跨域 Header
//            hresp.setHeader("Access-Control-Allow-Methods", "*");
//            hresp.setHeader("Access-Control-Allow-Headers", "*");
//            hresp.setHeader("Access-Control-Allow-Headers","x-requested-with");
//
//            hresp.setHeader("Access-Control-Allow-Credentials","true");
//            // 浏览器是会先发一次options请求，如果请求通过，则继续发送正式的post请求
//
//            // 配置options的请求返回
//            if (hreq.getMethod().equals("OPTIONS")) {
//                hresp.setStatus(HttpStatus.OK.value());
//
//                // hresp.setContentLength(0);
//                hresp.getWriter().write("OPTIONS returns OK");
//
//                return;
//            }
//            // Filter 只是链式处理，请求依然转发到目的地址。
//            chain.doFilter(req, resp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }
}