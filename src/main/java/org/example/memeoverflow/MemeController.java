package org.example.memeoverflow;

import org.example.memeoverflow.model.MemeRequest;
import org.example.memeoverflow.model.MemeResponse;
import org.example.memeoverflow.service.MemeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Web controller for meme generation UI
 */
@Controller
public class MemeController {

  private final MemeService memeService;

  public MemeController(MemeService memeService) {
    this.memeService = memeService;
  }

  @GetMapping("/")
  public String home() {
    return "history";
  }

  @GetMapping("/generator")
  public String generator() {
    return "meme-generator";
  }

  @GetMapping("/gallery")
  public String gallery() {
    return "gallery";
  }

  @GetMapping("/horror-stories")
  public String horrorStories() {
    return "horror-stories";
  }

  @GetMapping("/generate")
  public String generate(@RequestParam(required = false) String input,
      @RequestParam(defaultValue = "commit") String type,
      Model model) {

    if (input == null || input.trim().length() < 4) {
      return "meme-generator";
    }

    // Create request object
    MemeRequest request = new MemeRequest(input, parseType(type));

    // Validate request
    if (!memeService.isValidRequest(request)) {
      model.addAttribute("error", "Invalid request. Please provide valid input.");
      return "meme-generator";
    }

    // Generate meme
    MemeResponse response = memeService.generateMeme(request);

    // Add attributes to model
    if (response.isSuccessful()) {
      model.addAttribute("input", response.getOriginalInput());
      model.addAttribute("type", type);
      model.addAttribute("prompt", response.getMemePrompt());
      model.addAttribute("url", response.getImageUrl());
      model.addAttribute("topText", response.getTopText());
      model.addAttribute("bottomText", response.getBottomText());
    } else {
      model.addAttribute("error", response.getErrorMessage());
    }

    return "meme-generator";
  }

  private MemeRequest.MemeType parseType(String type) {
    return switch (type.toLowerCase()) {
      case "code" -> MemeRequest.MemeType.CODE;
      case "custom" -> MemeRequest.MemeType.CUSTOM;
      default -> MemeRequest.MemeType.COMMIT;
    };
  }
}
