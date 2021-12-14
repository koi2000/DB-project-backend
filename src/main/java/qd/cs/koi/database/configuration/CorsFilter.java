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
        try {
            HttpServletRequest hreq = (HttpServletRequest) req;
            HttpServletResponse hresp = (HttpServletResponse) resp;
            //跨域
            hresp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            //跨域 Header
            hresp.setHeader("Access-Control-Allow-Methods", "*");
            hresp.setHeader("Access-Control-Allow-Headers", "*");
            hresp.setHeader("Access-Control-Allow-Headers","x-requested-with");
            hresp.setHeader("Access-Control-Allow-Credentials","true");
            // 浏览器是会先发一次options请求，如果请求通过，则继续发送正式的post请求

            // 配置options的请求返回
            if (hreq.getMethod().equals("OPTIONS")) {
                hresp.setStatus(HttpStatus.OK.value());

                // hresp.setContentLength(0);
                hresp.getWriter().write("OPTIONS returns OK");
                return;
            }
            // Filter 只是链式处理，请求依然转发到目的地址。
            chain.doFilter(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }
}