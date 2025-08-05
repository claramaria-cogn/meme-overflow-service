package org.example.memeoverflow.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for the application
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "meme-overflow")
public class MemeOverflowProperties {

  private DeepAi deepAi = new DeepAi();

  @Data
  public static class DeepAi {
    private String apiKey = "b6f33ba6-37f9-48b4-90f0-e75448586042";
    private String baseUrl = "https://api.deepai.org";
    private String textToImageEndpoint = "/api/text2img";
    private int timeoutSeconds = 30;

    public String getFullTextToImageUrl() {
      return baseUrl + textToImageEndpoint;
    }
  }
}
