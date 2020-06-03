package com.queue.example.redis;

import com.queue.example.config.props.redis.RedisProperties.RedisConsumerProperties;
import com.queue.example.metrics.BrokerMetrics;
import io.lettuce.core.Consumer;
import io.lettuce.core.XReadArgs;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class RedisConsumer {

  private final RedisReactiveCommands<String, String> redisReactiveCommands;
  private final RedisConsumerProperties redisConsumerProperties;
  private final BrokerMetrics brokerMetrics;

//  @PostConstruct
  void exampleConsumer() {
    createGroupIfNotExist()
        .thenMany(readMessages())
        .doOnNext(ignore -> brokerMetrics.incrConsumer("redis"))
        .subscribe();
  }

  private Mono<String> createGroupIfNotExist() {
    log.info("createGroupIfNotExist: start creat group");

    return redisReactiveCommands
        .xgroupCreate(
            XReadArgs.StreamOffset.from(
                redisConsumerProperties.getDestination(),
                redisConsumerProperties.getOffset()
            ),
            redisConsumerProperties.getGroupName()
        )
        .onErrorResume(e -> {
//          log.info("exampleConsumer: Group={} already exist", redisConsumerProperties.getGroupName());
          return Mono.just("already exist");
        });
  }

  private Flux<Long> readMessages() {
    return redisReactiveCommands.xreadgroup(
        Consumer.from(redisConsumerProperties.getGroupName(), "consumer_1"),
        XReadArgs.StreamOffset.lastConsumed(redisConsumerProperties.getDestination())
    )
        .repeatWhen(Flux::repeat)
//        .doOnNext(messages -> log.info("exampleConsumer: messageId={}, body={}", messages.getId(), messages.getBody()))
        //  confirm done message
        .flatMap(
            message -> redisReactiveCommands.xack(
                redisConsumerProperties.getDestination(),
                redisConsumerProperties.getGroupName(),
                message.getId()
            )
        );
  }

}
