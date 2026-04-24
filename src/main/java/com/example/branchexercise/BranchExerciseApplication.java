package com.example.branchexercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BranchExerciseApplication {

    static void main(String[] args) {
        SpringApplication.run(BranchExerciseApplication.class, args);
    }

}
