package com.queue.example.config;

import com.queue.example.config.props.redis.RedisProperties;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RedisConfiguration {

  @Bean
  RedisReactiveCommands<String, String> redisReactiveCommands(RedisProperties redisProperties) {
    log.info("redisReactiveCommands: init redis with props={}", redisProperties);
    return RedisClient.create(
        RedisURI.builder()
            .withHost(redisProperties.getHost())
            .withPort(redisProperties.getPort())
            .build()
    )
        .connect()
        .reactive();
  }


}
