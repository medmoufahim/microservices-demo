package com.microservices.demo.ai.generated.tweet.to.kafka.service.transformer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.exception.AIGeneratedTweetToKafkaServiceException;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import org.springframework.stereotype.Component;

@Component
public class TwitterStatusToAvroTransformer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public TwitterAvroModel getTwitterAvroModelFromTweet(String tweetJson) {
        try {
            JsonNode root = objectMapper.readTree(tweetJson);
            long id = root.get("id").asLong();
            long userId = root.get("user").get("id").asLong();
            String text = root.get("text").asText();
            long createdAt = root.get("createdAt").asLong();

            return TwitterAvroModel
                    .newBuilder()
                    .setId(id)
                    .setUserId(userId)
                    .setText(text)
                    .setCreatedAt(createdAt)
                    .build();
        } catch (Exception e) {
            throw new AIGeneratedTweetToKafkaServiceException(
                    "Error transforming tweet JSON to TwitterAvroModel: " + e.getMessage(), e);
        }
    }
}