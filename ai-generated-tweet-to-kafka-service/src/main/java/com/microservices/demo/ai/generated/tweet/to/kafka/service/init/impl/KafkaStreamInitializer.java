package com.microservices.demo.ai.generated.tweet.to.kafka.service.init.impl;

import com.microservices.demo.ai.generated.tweet.to.kafka.service.init.StreamInitializer;
import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.kafka.admin.client.KafkaAdminClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaStreamInitializer implements StreamInitializer {

    private final KafkaConfigData kafkaConfigData;

    private final KafkaAdminClient kafkaAdminClient;

    public KafkaStreamInitializer(KafkaConfigData configData, KafkaAdminClient adminClient) {
        this.kafkaConfigData = configData;
        this.kafkaAdminClient = adminClient;
    }

    @Override
    public boolean init() {
        kafkaAdminClient.createTopics();
        kafkaAdminClient.checkSchemaRegistry();
        log.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
        return true;
    }
}
