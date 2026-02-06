package com.ashirvad.platform.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@Tag(name = "GenAI APIs", description = "Utility endpoints for GenAI module")
public class UtilityController {
    @Value("${LOG_FILE_PATH}")
    private String logFilePath;
    private final Logger log = LoggerFactory.getLogger(UtilityController.class);
    private final AtomicInteger downloadCount = new AtomicInteger(0);

    @Operation(summary = "Download Resume", description = "Downloads the resume PDF file.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully downloaded resume",
                    content = @Content(mediaType = "application/pdf")),
            @ApiResponse(responseCode = "404", description = "Resume file not found")
    })
    @GetMapping("/download/resume")
    public ResponseEntity<Resource> downloadResume() throws IOException {
        Resource resource = new ClassPathResource("static/files/Resume_Ashirvad_Kumar_Pandey.pdf");
        if (!resource.exists()) {
            log.warn("Resume file not found");
            return ResponseEntity.notFound().build();
        }

        // increment and log
        int count = downloadCount.incrementAndGet();
        log.info("Resume downloaded. totalDownloads={}", count);

        String filename = "Resume_Ashirvad_Kumar_Pandey.pdf";
        String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded)
                .body(resource);
    }

    @Operation(summary = "Download Chat Log", description = "Downloads the chat log file.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully downloaded chat log",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Chat log file not found")
    })
    @GetMapping("/api/getChat")
    public ResponseEntity<FileSystemResource> downloadChatLog() {
        File logFile = new File(logFilePath);

        if (!logFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        FileSystemResource resource = new FileSystemResource(logFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=chat_log.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }

    @Operation(summary = "Get Resume Download Count", description = "Returns the number of times the resume has been downloaded.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved count",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class)))
    })
    @GetMapping("/download/resume/count")
    public ResponseEntity<Integer> resumeCount() {
        return ResponseEntity.ok(downloadCount.get());
    }

    @Operation(summary = "Delete Chat Log", description = "Deletes the chat log file.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted chat log"),
            @ApiResponse(responseCode = "404", description = "Chat log file not found"),
            @ApiResponse(responseCode = "500", description = "Failed to delete chat log")
    })
    @DeleteMapping("/api/deleteChat")
    public ResponseEntity<String> deleteChatLog() {
        File logFile = new File(logFilePath);

        if (!logFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        if (logFile.delete()) {
            return ResponseEntity.ok("Chat log deleted successfully.");
        } else {
            return ResponseEntity.status(500).body("Failed to delete chat log.");
        }
    }
}
