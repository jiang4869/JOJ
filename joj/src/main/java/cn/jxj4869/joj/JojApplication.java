package cn.jxj4869.joj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.jxj4869.joj.mapper")
@EnableRabbit
public class JojApplication {
    public static void main(String[] args) {
        SpringApplication.run(JojApplication.class,args);
    }
}
