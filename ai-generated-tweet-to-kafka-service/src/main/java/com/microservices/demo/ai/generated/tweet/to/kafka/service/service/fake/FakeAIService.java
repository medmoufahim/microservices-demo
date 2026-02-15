package com.microservices.demo.ai.generated.tweet.to.kafka.service.service.fake;

import com.microservices.demo.ai.generated.tweet.to.kafka.service.exception.AIGeneratedTweetToKafkaServiceException;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.service.AIService;
import com.microservices.demo.config.AIGeneratedTweetToKafkaServiceConfigData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "ai-generated-tweet-to-kafka-service.ai-service", havingValue = "FakeAI")
public class FakeAIService implements AIService {

    private final AIGeneratedTweetToKafkaServiceConfigData configData;
    private final Random random = new Random();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

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

        // Simulate some processing time (optional - to make it feel more realistic)
        try {
            Thread.sleep(500 + random.nextInt(1500)); // Random delay between 0.5-2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Fake service interrupted");
        }

        String keyword = getRandomKeyword();
        long tweetId = Math.abs(random.nextLong());
        long userId = Math.abs(random.nextLong());
        String createdAt = ZonedDateTime.now().format(dateFormatter);
        String tweetText = generateRandomTweetText(keyword);

        String fakeResponse = String.format(
            "{\"createdAt\":\"%s\",\"id\":%d,\"text\":\"%s\",\"user\":{\"id\":%d}}",
            createdAt, tweetId, tweetText, userId
        );

        log.info("Fake tweet generated successfully!");
        log.info("Tweet content: {}", fakeResponse);
        return fakeResponse;
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

