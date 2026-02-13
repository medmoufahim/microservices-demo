package com.microservices.demo.ai.generated.tweet.to.kafka.service.service.fake;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.exception.AIGeneratedTweetToKafkaServiceException;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.service.AIService;
import com.microservices.demo.config.AIGeneratedTweetToKafkaServiceConfigData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "ai-generated-tweet-to-kafka-service.ai-service", havingValue = "FakeAI")
public class FakeAIService implements AIService {

    private final AIGeneratedTweetToKafkaServiceConfigData configData;
    private final Random random = new Random();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String[] TWEET_TEMPLATES = {
        "Just discovered an amazing feature in %s! This is going to revolutionize how we code. #%s #coding",
        "Working on a new project with %s today. Loving the productivity boost! #%s #development",
        "Pro tip: When using %s, always remember to optimize your code for performance. #%s #bestpractices",
        "Can't believe how powerful %s is! Just implemented a complex feature in minutes. #%s #programming",
        "Learning %s has been an incredible journey. The community support is amazing! #%s #tech",
        "Hot take: %s is the future of software development. Change my mind! #%s #developers",
        "Just finished a tutorial on %s. Mind = blown! #%s #learning",
        "If you're not using %s yet, you're missing out on some serious productivity gains! #%s #devtools"
    };

    @Override
    public String generateTweet() throws AIGeneratedTweetToKafkaServiceException {
        log.info("Generating FAKE tweet response for testing...");
        try {
            String keyword = getRandomKeyword();
            long tweetId = Math.abs(random.nextLong());
            long userId = Math.abs(random.nextLong());
            long createdAt = Instant.now().toEpochMilli();
            String tweetText = generateRandomTweetText(keyword);

            ObjectNode userNode = objectMapper.createObjectNode();
            userNode.put("id", userId);

            ObjectNode tweetNode = objectMapper.createObjectNode();
            tweetNode.put("createdAt", createdAt);
            tweetNode.put("id", tweetId);
            tweetNode.put("text", tweetText);
            tweetNode.set("user", userNode);

            String fakeResponse = objectMapper.writeValueAsString(tweetNode);
            log.info("Fake tweet generated successfully! Tweet content: {}", fakeResponse);
            return fakeResponse;
        } catch (Exception e) {
            throw new AIGeneratedTweetToKafkaServiceException("Failed to generate fake tweet", e);
        }
    }

    private String getRandomKeyword() {
        var keywords = configData.getStreamingDataKeywords();
        if (keywords == null || keywords.isEmpty()) {
            return "Technology";
        }
        return keywords.get(random.nextInt(keywords.size()));
    }

    private String generateRandomTweetText(String keyword) {
        String template = TWEET_TEMPLATES[random.nextInt(TWEET_TEMPLATES.length)];
        return String.format(template, keyword, keyword);
    }
}

