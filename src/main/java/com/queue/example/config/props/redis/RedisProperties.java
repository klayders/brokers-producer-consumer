package com.queue.example.config.props.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@ConstructorBinding
@ConfigurationProperties(prefix = "app.redis")
public class RedisProperties {

  private final String host;
  private final int port;


  @Data
  @ConstructorBinding
  @ConfigurationProperties(prefix = "app.redis.consumer")
  public static class RedisConsumerProperties {
    private final String groupName;
    private final String destination;
    private final String offset;

  }
}
