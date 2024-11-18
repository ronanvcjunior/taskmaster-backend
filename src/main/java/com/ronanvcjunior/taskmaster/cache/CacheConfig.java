package com.ronanvcjunior.taskmaster.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    @Bean(name = "userLoginCache")
    public CacheStore<String, Integer> userCache() {
        return new CacheStore<>(900, TimeUnit.SECONDS);
    }
    @Bean(name = "usersLoginCache")
    public CacheStore<String, Integer> usersCache() {
        return new CacheStore<>(1900, TimeUnit.SECONDS);
    }
}