package com.queue.example.kafka;

import com.queue.example.metrics.BrokerMetrics;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverOptions;

@Component
@AllArgsConstructor
public class KafkaConsumer {

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");

  private final ReceiverOptions<Integer, String> options;
  private final BrokerMetrics brokerMetrics;

  @PostConstruct
  void exampleConsumer() {

    KafkaReceiver.create(options)
        .receive()
        .doOnNext(ignore -> brokerMetrics.incrConsumer("kafka"))
        .subscribe(record -> {
          ReceiverOffset offset = record.receiverOffset();
//          System.out.printf("Received message: topic-partition=%s offset=%d timestamp=%s key=%d value=%s\n",
//                            offset.topicPartition(),
//                            offset.offset(),
//                            DATE_FORMAT.format(new Date(record.timestamp())),
//                            record.key(),
//                            record.value());
          offset.acknowledge();
        });
  }
}
