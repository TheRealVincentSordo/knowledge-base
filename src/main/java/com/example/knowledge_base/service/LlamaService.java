package com.example.knowledge_base.service;

import com.example.knowledge_base.dto.LlamaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LlamaService {

    @Autowired
    private RestTemplate restTemplate;

    // Method to enhance the document content by calling the local LLaMA3 API
    public String enhanceDocumentContent(String content) {
        try {
            System.out.println("Content: " + content);
            // URL of the local Ollama LLaMA3 API
            String url = "http://localhost:11434/api/generate";

            // Create the JSON payload to send to the LLaMA3 model API
            Map<String, Object> requestPayload = new HashMap<>();
            requestPayload.put("model", "llama3");
            requestPayload.put("prompt", "Enhance this document: " + content);
            requestPayload.put("stream", false);

            // Create the headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Wrap the payload and headers into an HttpEntity
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestPayload, headers);

            // Make the POST request and get the response
            ResponseEntity<LlamaResponse> response = restTemplate.postForEntity(url, request, LlamaResponse.class);

            // Return the body of the response
            return Objects.requireNonNull(response.getBody()).getResponse();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error enhancing document with LLaMA3";
        }
    }
}
