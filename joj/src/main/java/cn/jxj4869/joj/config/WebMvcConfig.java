package cn.jxj4869.joj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author JiangXiaoju
 * @date 2020-06-13 17:30
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public FormContentFilter formContentFilter() {
        return new FormContentFilter();
    }

}
