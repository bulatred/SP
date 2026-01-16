// src/main/java/com/example/demo/service/TelegramService.java

package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.chat.id}")
    private String chatId;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String text) {
        if (botToken == null || chatId == null || botToken.trim().isEmpty() || chatId.trim().isEmpty()) {
            log.warn("Telegram не настроен");
            return;
        }

        if (text == null || text.trim().isEmpty()) {
            log.warn("Текст сообщения пустой — не отправляем");
            return;
        }

        try {
            String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

            // ✅ Используем Map → Spring сам сериализует в JSON
            Map<String, Object> payload = new HashMap<>();
            payload.put("chat_id", chatId);
            payload.put("text", text);
            payload.put("parse_mode", "HTML");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            restTemplate.postForObject(url, request, String.class);
            log.info("✅ Отправлено в Telegram: {}", text);

        } catch (Exception e) {
            log.error("❌ Ошибка отправки в Telegram", e);
        }
    }
}