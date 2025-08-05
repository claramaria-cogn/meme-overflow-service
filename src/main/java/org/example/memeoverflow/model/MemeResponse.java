package org.example.memeoverflow.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a generated meme response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemeResponse {
  private String originalInput;
  private MemeRequest.MemeType type;
  private String memePrompt;
  private String imageUrl;
  private String topText;
  private String bottomText;
  private boolean successful;
  private String errorMessage;

  public static MemeResponse success(String originalInput, MemeRequest.MemeType type,
      String memePrompt, String imageUrl, String topText, String bottomText) {
    return MemeResponse.builder()
        .originalInput(originalInput)
        .type(type)
        .memePrompt(memePrompt)
        .imageUrl(imageUrl)
        .topText(topText)
        .bottomText(bottomText)
        .successful(true)
        .build();
  }

  public static MemeResponse error(String errorMessage) {
    return MemeResponse.builder()
        .successful(false)
        .errorMessage(errorMessage)
        .build();
  }
}
