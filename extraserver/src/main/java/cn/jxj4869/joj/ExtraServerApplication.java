package cn.jxj4869.joj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author JiangXiaoju
 * @date 2020-06-10 16:26
 */
@SpringBootApplication
@MapperScan("cn.jxj4869.joj.mapper")
@EnableScheduling
public class ExtraServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExtraServerApplication.class,args);
    }
}
