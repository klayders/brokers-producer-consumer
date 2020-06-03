package com.queue.example.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MetricConfig {


  @Bean
  public MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer(
      @Value("${app.metrics.project}") String project,
      @Value("${app.metrics.service}") String service
  ) {
    log.info("meterRegistryCustomizer: project={}, service={}", project, service);
    return registry -> registry.config()
        .commonTags(
            List.of(
                Tag.of("project", project),
                Tag.of("service", service)
            )
        );
  }
//  @Bean
//  public TimedAspect timedAspect(MeterRegistry registry) {
//    return new TimedAspect(registry);
//  }
}
