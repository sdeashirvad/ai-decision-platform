package com.ashirvad.platform.chat.controller;

import com.ashirvad.platform.chat.service.TelegramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telegram")
@Tag(name = "GenAI APIs", description = "Endpoints for Telegram Bot integration")
public class TelegramController {

    @Autowired
    private TelegramService telegramService;

    @Operation(summary = "Handle Telegram Webhook", description = "Receives and processes updates from Telegram Webhook.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed update")
    })
    @PostMapping("/webhook")
    public void handleWebhook(@RequestBody String update) {
        try {
            JSONObject updateJson = new JSONObject(update);
            telegramService.processUpdate(updateJson);
        } catch (Exception e) {
            System.err.println("Error processing webhook update: " + e.getMessage());
        }
    }
}
