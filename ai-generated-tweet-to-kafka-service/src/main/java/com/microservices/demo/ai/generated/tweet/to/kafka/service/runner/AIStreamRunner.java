package com.microservices.demo.ai.generated.tweet.to.kafka.service.runner;

import com.microservices.demo.ai.generated.tweet.to.kafka.service.service.AIService;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.transformer.TwitterStatusToAvroTransformer;
import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import com.microservices.demo.kafka.producer.config.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AIStreamRunner implements Runnable {

    private final KafkaConfigData kafkaConfigData;

    private final AIService aiService;

    private final KafkaProducer<Long, TwitterAvroModel> kafkaProducer;

    private final TwitterStatusToAvroTransformer twitterStatusToAvroTransformer;

    public AIStreamRunner(KafkaConfigData kafkaConfigData, AIService aiService, KafkaProducer<Long, TwitterAvroModel> kafkaProducer, TwitterStatusToAvroTransformer twitterStatusToAvroTransformer) {
        this.kafkaConfigData = kafkaConfigData;
        this.aiService = aiService;
        this.kafkaProducer = kafkaProducer;
        this.twitterStatusToAvroTransformer = twitterStatusToAvroTransformer;
    }

    @Override
    public void run() {
        String generatedTweet = aiService.generateTweet();
        log.info("Generated Tweet: {}", generatedTweet);
        TwitterAvroModel twitterAvroModel = twitterStatusToAvroTransformer.getTwitterAvroModelFromTweet(generatedTweet);
        kafkaProducer.send(kafkaConfigData.getTopicName(), twitterAvroModel.getUserId(), twitterAvroModel);
    }
}
