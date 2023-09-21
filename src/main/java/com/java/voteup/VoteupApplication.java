package com.java.voteup;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.io.IOException;

@SpringBootApplication()
@EntityScan("com.java.voteup.domain")
public class VoteupApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(VoteupApplication.class, args);
    }
}
