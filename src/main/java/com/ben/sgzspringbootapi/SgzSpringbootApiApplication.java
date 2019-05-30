package com.ben.sgzspringbootapi;

import com.github.pagehelper.PageHelper;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.ben.sgzspringbootapi.mapper")
@SpringBootApplication
@EnableScheduling
@EnableEncryptableProperties
public class SgzSpringbootApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SgzSpringbootApiApplication.class, args);
    }

}

