package com.tasksync;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tasksync.mapper")
public class TaskSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskSyncApplication.class, args);
    }
}
