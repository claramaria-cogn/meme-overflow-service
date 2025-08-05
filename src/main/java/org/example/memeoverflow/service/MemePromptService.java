package org.example.memeoverflow.service;

import org.example.memeoverflow.model.MemeRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Service for generating meme prompts based on different input types
 */
@Service
public class MemePromptService {

  private final Random random = new Random();

  /**
   * Generate a meme prompt based on the input and type
   */
  public String generateMemePrompt(MemeRequest request) {
    String input = request.getInput();
    String msg = input.toLowerCase();

    return switch (request.getType()) {
      case CODE -> generateCodeMemePrompt(input, msg);
      case CUSTOM -> generateCustomMemePrompt(input, msg);
      case COMMIT -> generateCommitMemePrompt(input, msg);
    };
  }

  /**
   * Split meme text into top and bottom parts for overlay
   */
  public String[] splitMemeText(String memePrompt) {
    if (memePrompt.contains("–")) {
      return memePrompt.split("–", 2);
    } else if (memePrompt.contains(":")) {
      return memePrompt.split(":", 2);
    }

    int mid = memePrompt.length() / 2;
    int spaceIndex = memePrompt.indexOf(" ", mid);
    if (spaceIndex != -1) {
      return new String[] {
          memePrompt.substring(0, spaceIndex),
          memePrompt.substring(spaceIndex + 1)
      };
    }

    return new String[] { memePrompt };
  }

  private String generateCommitMemePrompt(String input, String msg) {
    // Enhanced prompts with more variety using random selection
    if (msg.contains("fix") && msg.contains("bug")) {
      return getRandomResponse(new String[] {
          "Fixes a bug – Introduces three more",
          "Bug fixed – Feature broken",
          "Fixed the bug – Broke the build"
      });
    }

    if (msg.contains("fix") && msg.contains("issue")) {
      return getRandomResponse(new String[] {
          "Fixes an issue – Accidentally opens five new ones",
          "Issue resolved – New mystery appears",
          "Fixed it – Nobody knows how"
      });
    }

    if (msg.contains("refactor")) {
      return getRandomResponse(new String[] {
          "Refactored everything – Except the mess",
          "Clean code achieved – Functionality unclear",
          "Refactored – Still spaghetti",
          "Code cleaner – Developer more confused"
      });
    }

    if (msg.contains("test")) {
      return getRandomResponse(new String[] {
          "Writes tests – Deletes them when they fail",
          "100% test coverage – 0% confidence",
          "Tests passing – App still crashing",
          "Unit tests added – Integration still broken"
      });
    }

    if (msg.contains("deploy") || msg.contains("deployment")) {
      return getRandomResponse(new String[] {
          "Deployed to production – Panic ensues",
          "Deployment successful – Users disagree",
          "Pushed to prod – Pull requests from reality"
      });
    }

    if (msg.contains("hotfix") || msg.contains("bugfix")) {
      return getRandomResponse(new String[] {
          "Hotfix deployed – Server now hotter than ever",
          "Quick fix – Nothing quick about the chaos",
          "Hotfix applied – Fire intensifies"
      });
    }

    if (msg.contains("merge")) {
      return getRandomResponse(new String[] {
          "Merged branch – Unleashed merge conflict hell",
          "Merge complete – Sanity lost",
          "Branch merged – Team scattered"
      });
    }

    if (msg.contains("docker") || msg.contains("container")) {
      return getRandomResponse(new String[] {
          "Docker build fixed – Image still cursed",
          "Containerized app – Problems still leak out",
          "Works on my container – Doesn't work on yours"
      });
    }

    if (msg.contains("api")) {
      return getRandomResponse(new String[] {
          "API improved – Docs still lying",
          "REST API fixed – Now it never rests",
          "API documented – Documentation outdated"
      });
    }

    if (msg.contains("database") || msg.contains("db")) {
      return getRandomResponse(new String[] {
          "Database optimized – For failure",
          "DB migration successful – Data migrated to /dev/null",
          "Query optimized – Database still slow"
      });
    }

    if (msg.contains("performance") || msg.contains("optimize")) {
      return getRandomResponse(new String[] {
          "Performance boosted – Server died faster",
          "Optimization complete – Code now unreadable",
          "Faster code – Slower debugging"
      });
    }

    if (msg.contains("security") || msg.contains("auth")) {
      return getRandomResponse(new String[] {
          "Security improved – Nobody can access anything",
          "Authentication fixed – Admin locked out",
          "Security patch applied – New vulnerabilities unlocked"
      });
    }

    if (msg.contains("feature") || msg.contains("feat")) {
      return getRandomResponse(new String[] {
          "New feature added – Old features removed",
          "Feature complete – User expectations incomplete",
          "Amazing feature – Nobody asked for it"
      });
    }

    if (msg.contains("ui") || msg.contains("frontend")) {
      return getRandomResponse(new String[] {
          "UI improved – UX destroyed",
          "Frontend fixed – Backend revenge",
          "User interface enhanced – User confusion maximized"
      });
    }

    if (msg.contains("mobile") || msg.contains("responsive")) {
      return getRandomResponse(new String[] {
          "Mobile responsive – Desktop users cry",
          "Works on mobile – Breaks on tablet",
          "Responsive design – Responds with errors"
      });
    }

    // Default fallback with variety
    return capitalizeFirst(input.trim()) + " – " + getRandomResponse(new String[] {
        "Sounds legit, breaks everything anyway",
        "Code works in mysterious ways",
        "It's not a bug, it's a feature",
        "Works on my machine",
        "Should be fine... probably",
        "TODO: Fix this properly later"
    });
  }

  private String generateCodeMemePrompt(String input, String msg) {
    // Code-specific meme generation
    if (msg.contains("if") && msg.contains("else")) {
      return getRandomResponse(new String[] {
          "Elegant if-else logic – Edge cases not included",
          "Conditional statements – Conditions may apply",
          "If this works – Else we're doomed"
      });
    }

    if (msg.contains("try") && msg.contains("catch")) {
      return getRandomResponse(new String[] {
          "Try-catch block added – Catches everything except bugs",
          "Exception handled – Expectation not met",
          "Try to catch errors – Catch feelings instead"
      });
    }

    if (msg.contains("loop") || msg.contains("for") || msg.contains("while")) {
      return getRandomResponse(new String[] {
          "Infinite loop detected – Feature not bug",
          "Loop optimized – Still runs forever",
          "While loop works – While developer cries"
      });
    }

    if (msg.contains("null") || msg.contains("undefined")) {
      return getRandomResponse(new String[] {
          "Null check added – Everything is null anyway",
          "Undefined behavior – Now defined as broken",
          "Null pointer exception – Points to problem"
      });
    }

    if (msg.contains("function") || msg.contains("method")) {
      return getRandomResponse(new String[] {
          "Function works perfectly – In theory",
          "Method implemented – Madness included",
          "Clean function – Dirty secrets inside"
      });
    }

    // Default for code snippets
    return "Code snippet analyzed – " + getRandomResponse(new String[] {
        "Complexity level: Expert",
        "Readability: Questionable",
        "Functionality: Mysterious",
        "Debug difficulty: Impossible"
    });
  }

  private String generateCustomMemePrompt(String input, String msg) {
    // Custom text gets programming twist
    return capitalizeFirst(input.trim()) + " – " + getRandomResponse(new String[] {
        "In programming terms: Undefined",
        "Compile error: Logic not found",
        "Runtime exception: Reality.exe has stopped",
        "Memory leak detected in brain.exe",
        "Stack overflow in human.brain",
        "404: Sanity not found"
    });
  }

  private String getRandomResponse(String[] responses) {
    return responses[random.nextInt(responses.length)];
  }

  private String capitalizeFirst(String text) {
    if (text == null || text.isEmpty())
      return "";
    return text.substring(0, 1).toUpperCase() + text.substring(1);
  }
}
