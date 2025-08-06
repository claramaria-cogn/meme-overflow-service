package org.example.memeoverflow.service;

import org.example.memeoverflow.client.DeepAiClient;
import org.example.memeoverflow.model.MemeRequest;
import org.example.memeoverflow.model.MemeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Main service for meme generation orchestration
 */
@Service
public class MemeService {

  private static final Logger logger = LoggerFactory.getLogger(MemeService.class);

  private final MemePromptService memePromptService;
  private final DeepAiClient deepAiClient;

  public MemeService(MemePromptService memePromptService, DeepAiClient deepAiClient) {
    this.memePromptService = memePromptService;
    this.deepAiClient = deepAiClient;
  }

  /**
   * Generate a meme from the given request
   * 
   * @param request The meme generation request
   * @return The meme response with image URL and text
   */
  public MemeResponse generateMeme(MemeRequest request) {
    try {
      logger.info("Generating meme for input: '{}' of type: {}",
          request.getInput(), request.getType());

      // Generate the meme prompt
      String memePrompt = memePromptService.generateMemePrompt(request);
      logger.debug("Generated meme prompt: '{}'", memePrompt);

      // Generate image from the prompt
      String imageUrl = deepAiClient.generateImageFromText(memePrompt);
      logger.debug("Generated image URL: {}", imageUrl);

      // Split the meme text for overlay
      String[] textParts = memePromptService.splitMemeText(memePrompt);
      String topText = textParts[0].trim();
      String bottomText = textParts.length > 1 ? textParts[1].trim() : "";

      logger.info("Successfully generated meme for input: '{}'", request.getInput());

      return MemeResponse.success(
          request.getInput(),
          request.getType(),
          memePrompt,
          imageUrl,
          topText,
          bottomText);

    } catch (DeepAiClient.DeepAiException e) {
      logger.error("Failed to generate image via DeepAI: {}", e.getMessage(), e);
      return MemeResponse.error("Failed to generate meme image. Please try again!");

    } catch (Exception e) {
      logger.error("Unexpected error during meme generation: {}", e.getMessage(), e);
      return MemeResponse.error("An unexpected error occurred. Please try again!");
    }
  }

  /**
   * Validate the meme request
   * 
   * @param request The request to validate
   * @return true if valid, false otherwise
   */
  public boolean isValidRequest(MemeRequest request) {
    return request != null
        && request.getInput() != null
        && request.getInput().trim().length() >= 4
        && request.getType() != null;
  }
}
