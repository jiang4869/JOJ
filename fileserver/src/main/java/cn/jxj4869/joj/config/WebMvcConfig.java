package cn.jxj4869.joj.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author JiangXiaoju
 * @date 2020-06-9 9:10
 *
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${file.upload.save-path}")
    private String savePath;
    @Value("${file.upload.url.mapping}")
    private String url;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler(url).addResourceLocations("file:"+savePath);
    }
}
