package qd.cs.koi.database.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    LoginInterceptor loginInterceptor;

    @Resource
    UserSessionMethodArgumentResolver userSessionMethodArgumentResolver;

    @Resource
    ManageInterceptor manageInterceptor;
    //新增拦截器
    //越早添加的拦截器优先级越高
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(tokenInterceptor).addPathPatterns("/manage/**");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/user/register", "/imserver/**", "/files/**", "/alipay/**",
                        "/doc.html", "/webjars/**", "/swagger-resources/**");

        registry.addInterceptor(manageInterceptor).addPathPatterns("/manage/**");
    }

    //此处可以新增
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userSessionMethodArgumentResolver);
    }


    //配置跨域
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                //.allowedHeaders("custom-header")   //允许前端携带的请求头,有自定义请求头就写进去
                .allowedHeaders("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")               //允许前端请求的方法，"*"表示所有
                .allowCredentials(true)			   //是否允许附带身份凭证和cookies
                .maxAge(1800)					   //预见请求有效时间
                .exposedHeaders("custom-header");  //允许浏览器访问的响应头,有自定义响应头就写进去
    }

}
