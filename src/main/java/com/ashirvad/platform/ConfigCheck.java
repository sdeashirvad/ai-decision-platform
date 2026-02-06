package com.ashirvad.platform;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigCheck implements CommandLineRunner {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=========================================");
        System.out.println("CONFIGURATION CHECK");
        System.out.println("=========================================");
        System.out.println("Datasource URL: " + dbUrl);
        System.out.println("Datasource User: " + dbUser);
        System.out.println("DDL Auto: " + ddlAuto);
        System.out.println("=========================================");
    }
}
