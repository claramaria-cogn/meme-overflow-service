package org.example.memeoverflow.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request to generate a meme
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemeRequest {
  private String input;
  private MemeType type;

  public enum MemeType {
    COMMIT,
    CODE,
    CUSTOM
  }
}
