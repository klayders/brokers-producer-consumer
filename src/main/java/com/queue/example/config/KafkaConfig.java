package com.queue.example.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {


  private static final String BOOTSTRAP_SERVERS = "localhost:9092";
  public static final String TOPIC = "demo-topic";


  @Bean
  ReceiverOptions<Integer, String> receiverOptions() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ConsumerConfig.CLIENT_ID_CONFIG, "sample-consumer");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-group");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return ReceiverOptions.create(props);
  }

  @Bean
  ReceiverOptions<Integer, String> options(ReceiverOptions<Integer, String> receiverOptions) {
    return receiverOptions.subscription(Collections.singleton(TOPIC))
        .addAssignListener(partitions -> log.info("onPartitionsAssigned {}", partitions))
        .addRevokeListener(partitions -> log.info("onPartitionsRevoked {}", partitions));
  }

  @Bean
  KafkaSender<Integer, String> sender() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "sample-producer");
    props.put(ProducerConfig.ACKS_CONFIG, "all");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    SenderOptions<Integer, String> senderOptions = SenderOptions.create(props);

    return KafkaSender.create(senderOptions);
  }

}
