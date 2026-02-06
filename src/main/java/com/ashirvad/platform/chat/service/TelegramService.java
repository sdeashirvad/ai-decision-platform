package com.ashirvad.platform.chat.service;

import com.ashirvad.platform.ai.AiService;
import com.ashirvad.platform.ai.SystemPromptType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TelegramService {

    @Value("${TELEGRAM_BOT_TOKEN}")
    private String botToken;

    @Value("${TELEGRAM_URL}")
    private String telegramApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private final Map<Long, Long> lastHandledUpdates = new ConcurrentHashMap<>();

    private long lastUpdateId = -1;

    @Autowired
    private AiService aiService;

    private static final String LOG_FILE_PATH = "../chat_log.txt";

    public void processUpdate(JSONObject update) {
        if (!update.has("message")) return;

        JSONObject messageObj = update.getJSONObject("message");
        String text = messageObj.getString("text");
        long chatId = messageObj.getJSONObject("chat").getLong("id");

        if (isGreeting(text)) {
            sendMessage(chatId, "Hi! I'm your AI assistant. Ask me anything.");
        } else if (text.equalsIgnoreCase("help")) {
            sendMessage(chatId, "Send any question or message. Type 'Explain more' to follow up.");
        } else {
            handleAIResponse(chatId, text);
        }
    }

    private boolean isGreeting(String text) {
        return text.equalsIgnoreCase("start") || text.equalsIgnoreCase("hi") || text.equalsIgnoreCase("hello");
    }

    private void handleAIResponse(long chatId, String text) {
        try {
            String geminiReply = aiService.run(SystemPromptType.GENAI_WRAPPER_CHAT, text);
            logChat(text, geminiReply);
            sendMessageInChunks(chatId, geminiReply);
        } catch (Exception e) {
            sendMessage(chatId, "Error while processing your request.");
            e.printStackTrace();
        }
    }

    public void sendMessage(Long chatId, String message) {
        String url = telegramApiUrl + botToken + "/sendMessage";

        Map<String, String> body = Map.of(
                "chat_id", String.valueOf(chatId),
                "text", message
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessageInChunks(long chatId, String message) {
        int maxLength = 4096;
        int start = 0;

        while (start < message.length()) {
            int end = Math.min(start + maxLength, message.length());
            String chunk = message.substring(start, end);
            sendMessage(chatId, chunk);
            start = end;
        }
    }

    private void logChat(String userInput, String response) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write("User Input: " + userInput);
            writer.newLine();
            writer.write("Response: " + response);
            writer.newLine();
            writer.write("-----");
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to log chat: " + e.getMessage());
        }
    }

}