package com.queue.example.redis;

import com.queue.example.config.props.redis.RedisProperties.RedisConsumerProperties;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@AllArgsConstructor
public class RedisProducer {

  private final RedisReactiveCommands<String, String> redisReactiveCommands;
  private final RedisConsumerProperties redisConsumerProperties;

  @PostConstruct
  void produceMessages() {
    Flux.range(0, 10)
        .flatMap(integer -> redisReactiveCommands.xadd(redisConsumerProperties.getDestination(), generateRandomMap(integer)))
        .doOnNext(messageId -> {
          log.info("produceMessages: messageId={}", messageId);
        })
        .subscribe();
  }

  private Map<String, String> generateRandomMap(Integer integer) {
    var pojo = new HashMap<String, String>();
    pojo.put("number", String.valueOf(integer));
    pojo.put("date", String.valueOf(System.currentTimeMillis()));
    return pojo;
  }
}
