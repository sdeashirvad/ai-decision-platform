package com.ashirvad.platform.chat.controller;

import com.ashirvad.platform.ai.AiService;
import com.ashirvad.platform.ai.SystemPromptType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/chat/api")
@Tag(name = "GenAI APIs", description = "Endpoints for General AI Chat operations")
public class ChatApiController {

    @Autowired
    private AiService aiService;

    private static final String LOG_FILE_PATH = "../chat_log.txt";

    @Operation(summary = "Process Chat Message", description = "Sends a user message to the Gemini service and returns the AI response.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed message",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class, example = "{\"geminiReply\": \"Hello! How can I help you?\"}")))
    })
    @PostMapping
    public Map<String, String> processMessage(@RequestBody Map<String, String> request) {
        String userMessage = request.get("userMessage");
        String geminiReply;
        try {
            geminiReply = aiService.run(SystemPromptType.GENAI_WRAPPER_CHAT, userMessage);
            logChat(userMessage, geminiReply);
        } catch (Exception e) {
            geminiReply = "Error while processing your message.";
            e.printStackTrace();
        }
        //printind User message & response:
        System.out.println("User Message: " + userMessage);
        System.out.println("Gemini Reply: " + geminiReply);
        Map<String, String> response = new HashMap<>();
        response.put("geminiReply", geminiReply);
        return response;
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