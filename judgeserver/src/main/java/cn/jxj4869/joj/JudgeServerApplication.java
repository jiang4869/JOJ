package cn.jxj4869.joj;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("cn.jxj4869.joj.mapper")
@EnableRabbit
@EnableScheduling
public class JudgeServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JudgeServerApplication.class,args);
    }
}
