package com.microservices.demo.ai.generated.tweet.to.kafka.service.service.springai;

import com.microservices.demo.ai.generated.tweet.to.kafka.service.exception.AIGeneratedTweetToKafkaServiceException;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.service.AIService;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.service.springai.model.TweetResponse;
import com.microservices.demo.config.AIGeneratedTweetToKafkaServiceConfigData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@ConditionalOnProperty(name = "ai-generated-tweet-to-kafka-service.ai-service", havingValue = "SpringAI-DeepSeek")
@Service
public class SpringAIDeepSeekService implements AIService {

    private final ChatClient chatClient;
    private final AIGeneratedTweetToKafkaServiceConfigData configData;

    @Value("classpath:/templates/tweet-prompt.st")
    private Resource tweetPrompt;

    public static final String DEEP_SEEK_THINK_REGEX = "(?s)<think>.*?</think>";

    public SpringAIDeepSeekService(@Qualifier("ollamaChatClient") ChatClient chatClient, AIGeneratedTweetToKafkaServiceConfigData configData) {
        this.chatClient = chatClient;
        this.configData = configData;
    }

    @Override
    public String generateTweet() throws AIGeneratedTweetToKafkaServiceException {
        BeanOutputConverter<TweetResponse> converter = new BeanOutputConverter<>(TweetResponse.class);

        log.info("Converter format: {}", converter.getFormat());

        PromptTemplate promptTemplate = new PromptTemplate(tweetPrompt);
        Prompt prompt = promptTemplate.create(Map.of(
                configData.getKeywordsPlaceholder().replace("{", "").replace("}", ""),
                String.join(",", configData.getStreamingDataKeywords()),
                "format",
                converter.getFormat()
        ));

        ChatClientResponse chatClientResponse = chatClient.prompt(prompt)
                .call()
                .chatClientResponse();

        String modelResult = chatClientResponse.chatResponse().getResult().getOutput().getText();

        log.info("Model result from deepseek: {} with model: {}", modelResult,
                chatClientResponse.chatResponse().getMetadata().getModel());

        return modelResult.replaceAll(DEEP_SEEK_THINK_REGEX, "").trim();
    }
}
