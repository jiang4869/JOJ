package cn.jxj4869.joj.config;


import cn.jxj4869.joj.submitter.CodeForcesSubmitter;
import cn.jxj4869.joj.submitter.HDUSubmitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SubmitterConfig {

    @Bean
    public HDUSubmitter hduSubmitter1() throws Exception {
        return new HDUSubmitter("jiangxiaoju","sherlock");
    }



}
