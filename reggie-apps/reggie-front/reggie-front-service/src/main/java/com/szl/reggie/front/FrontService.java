package com.szl.reggie.front;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
public class FrontService {
    public static void main(String[] args) {
        SpringApplication.run(FrontService.class,args);
        log.info("项目启动成功...");
    }
}
