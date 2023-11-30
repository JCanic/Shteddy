package com.example.shteddy.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAIService {
    /*
    @Value("${openai.apikey}")
    private String apiKey;

    public String categorizeDescription(String description) {
        RestTemplate restTemplate = new RestTemplate();
        String openAIUrl = "https://api.openai.com/v1/engines/davinci/completions";

        String prompt = String.format(
                "Given a transaction description, identify which of the following categories it belongs to: " +
                        "Groceries, Bills, Utilities, Rent/Mortgage, Insurance, Savings & Investments, Education, " +
                        "Transportation, Entertainment, Travel, Gifts & Donations, Personal Care, Pets, Kids, " +
                        "Miscellaneous, Health, Food, Car, Fun, Beauty, Sports.\n\n" +
                        "Transaction Description: \"%s\"\n\nThe category is:",
                description);

        Map<String, Object> body = new HashMap<>();
        body.put("prompt", prompt);
        body.put("max_tokens", 60);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(openAIUrl, request, String.class);
            // Process the response to extract the category
            return extractCategoryFromResponse(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    private String extractCategoryFromResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode choicesNode = rootNode.path("choices");
            if (!choicesNode.isArray() || choicesNode.isEmpty()) {
                return "Unknown";
            }
            String completionText = choicesNode.get(0).path("text").asText().trim();
            String[] words = completionText.split("\\s+");
            return words[words.length - 1];
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
*/

}