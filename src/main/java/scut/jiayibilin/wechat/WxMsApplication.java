package scut.jiayibilin.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author Jie
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
public class WxMsApplication {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar = Calendar.getInstance();
        System.out.println("目前时间"+calendar.getTime());

    }

    public static void main(String[] args) {
        SpringApplication.run(WxMsApplication.class, args);
    }
}
