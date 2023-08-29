package com.reddev.algorithmcompare.adminmonitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class AdminMonitor {

    public static void main(String[] args) {
        SpringApplication.run(AdminMonitor.class, args);
    }

}
