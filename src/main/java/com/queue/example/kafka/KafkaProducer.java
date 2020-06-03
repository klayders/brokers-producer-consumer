package com.queue.example.kafka;

import static com.queue.example.config.KafkaConfig.TOPIC;

import com.queue.example.metrics.BrokerMetrics;
import java.text.SimpleDateFormat;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaProducer {

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");

  private final KafkaSender<Integer, String> sender;
  private final BrokerMetrics brokerMetrics;

  @PostConstruct
  void exampleProducer() {
    Flux.range(1, 1_000_000)
        .flatMap(integer ->
                     sender.send(
                         Mono.just(SenderRecord.create(new ProducerRecord<>(TOPIC, integer, "Message_" + integer), integer))
                     )
        )
        .doOnNext(ignore -> brokerMetrics.incrProducer("kafka"))
        .doOnError(e -> log.error("exampleProducer: Send failed", e))
        .subscribe();
    //        .subscribe(r -> {
    //          RecordMetadata metadata = r.recordMetadata();
    //          System.out.printf("Message %d sent successfully, topic-partition=%s-%d offset=%d timestamp=%s\n",
    //                            r.correlationMetadata(),
    //                            metadata.topic(),
    //                            metadata.partition(),
    //                            metadata.offset(),
    //                            DATE_FORMAT.format(new Date(metadata.timestamp())));
    //        });
  }


}
