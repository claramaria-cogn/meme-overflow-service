package org.example.memeoverflow.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.memeoverflow.config.MemeOverflowProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Client for interacting with DeepAI API
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeepAiClient {

  private final RestTemplate restTemplate;
  private final MemeOverflowProperties properties;

  /**
   * Generate an image from text using DeepAI's text2img API
   * 
   * @param prompt The text prompt for image generation
   * @return The URL of the generated image
   * @throws DeepAiException if the API call fails
   */
  @SuppressWarnings("rawtypes")
  public String generateImageFromText(String prompt) throws DeepAiException {
    try {
      HttpHeaders headers = createHeaders();
      MultiValueMap<String, Object> body = createRequestBody(prompt);
      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      ResponseEntity<Map> response = restTemplate.postForEntity(
          properties.getDeepAi().getFullTextToImageUrl(),
          requestEntity,
          Map.class);

      return extractImageUrl(response);

    } catch (RestClientException e) {
      throw new DeepAiException("Failed to call DeepAI API: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new DeepAiException("Unexpected error during image generation: " + e.getMessage(), e);
    }
  }

  private HttpHeaders createHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Api-Key", properties.getDeepAi().getApiKey());
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    return headers;
  }

  private MultiValueMap<String, Object> createRequestBody(String prompt) {
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("text", prompt);
    return body;
  }

  @SuppressWarnings("rawtypes")
  private String extractImageUrl(ResponseEntity<Map> response) throws DeepAiException {
    log.debug("DeepAI API Response Status: {}", response.getStatusCode());
    log.debug("DeepAI API Response Body: {}", response.getBody());

    if (response.getBody() == null) {
      throw new DeepAiException("Received empty response from DeepAI API");
    }

    Object outputUrl = response.getBody().get("output_url");
    if (outputUrl == null) {
      log.debug("Available keys in response: {}", response.getBody().keySet());
      throw new DeepAiException("No output_url found in DeepAI API response");
    }

    return outputUrl.toString();
  }

  /**
   * Custom exception for DeepAI API errors
   */
  public static class DeepAiException extends Exception {
    public DeepAiException(String message) {
      super(message);
    }

    public DeepAiException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
