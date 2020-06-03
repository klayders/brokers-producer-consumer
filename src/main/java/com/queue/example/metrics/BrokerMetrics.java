package com.queue.example.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class BrokerMetrics {


  private final MeterRegistry meterRegistry;

  public void incrProducer(String brokerName) {
    var tags = makeTags(brokerName);
    meterRegistry.counter("producer", tags).increment();
  }


  public void incrConsumer(String brokerName) {
    var tags = makeTags(brokerName);
    meterRegistry.counter("consumer", tags).increment();
  }


  private List<Tag> makeTags(String broker) {
    return List.of(
        Tag.of("broker", broker)
    );
  }


}
