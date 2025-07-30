package org.example.memeoverflow;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class ImageUiController {

    private static final String API_KEY = "b6f33ba6-37f9-48b4-90f0-e75448586042";

    @GetMapping("/api/generate")
    public String generate(@RequestParam(required = false) String input, Model model) {
        if (input != null && input.trim().length() >= 4) {
            String memePrompt = generateMemePrompt(input);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Api-Key", API_KEY);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("text", memePrompt);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.deepai.org/api/text2img",
                    requestEntity,
                    Map.class
            );

            Object outputUrl = response.getBody().get("output_url");

            String[] split = splitMemeText(memePrompt);

            model.addAttribute("input", input);
            model.addAttribute("prompt", memePrompt);
            model.addAttribute("url", outputUrl);
            model.addAttribute("topText", split[0].trim());
            model.addAttribute("bottomText", split.length > 1 ? split[1].trim() : "");
        }

        return "image";
    }

    private String generateMemePrompt(String input) {
        String msg = input.toLowerCase();

        if (msg.contains("fix") && msg.contains("bug")) return "Fixes a bug – Introduces three more";
        if (msg.contains("fix") && msg.contains("issue")) return "Fixes an issue – Accidentally opens five new ones";
        if (msg.contains("fix") && msg.contains("error")) return "Fixes an error – Breaks unrelated feature";
        if (msg.contains("bugfix") && msg.contains("hotfix")) return "Hotfix deployed – Server now hotter than ever";
        if (msg.contains("refactor")) return "Refactored everything – Except the mess";
        if (msg.contains("refactored")) return "Refactored the code – Still unreadable";
        if (msg.contains("refactoring")) return "Massive refactoring – Just renamed stuff";
        if (msg.contains("typo")) return "Fixes a typo – Deploys chaos";
        if (msg.contains("spelling")) return "Corrects spelling – Grammar police still not satisfied";
        if (msg.contains("temp")) return "Adds temp code – Forgets to remove it";
        if (msg.contains("debug")) return "Added debug logs – Now drowning in console output";
        if (msg.contains("auth")) return "Fixed login – Now no one can log in";
        if (msg.contains("login")) return "Login fixed – Admin locked out";
        if (msg.contains("signup")) return "Signup works – Unless you're human";
        if (msg.contains("log")) return "Logging everything – Still can’t find the bug";
        if (msg.contains("test")) return "Writes tests – Deletes them when they fail";
        if (msg.contains("tests")) return "100% test coverage – 0% confidence";
        if (msg.contains("unit test")) return "Unit tests pass – App still crashes";
        if (msg.contains("docker")) return "Docker build fixed – Image still cursed";
        if (msg.contains("deploy")) return "Deployed to production – Panic ensues";
        if (msg.contains("ci") && msg.contains("cd")) return "CI/CD working – Constantly Interrupted/Consistently Down";
        if (msg.contains("readme")) return "Updated README – Now even less helpful";
        if (msg.contains("docs")) return "Improved docs – Nobody reads them";
        if (msg.contains("documentation")) return "Documented everything – Still asked questions";
        if (msg.contains("rename")) return "Renamed file – Forgot all references";
        if (msg.contains("remove")) return "Removed unused code – Broke everything";
        if (msg.contains("delete")) return "Deleted a file – It was important";
        if (msg.contains("cleanup")) return "Cleanup commit – Nothing works now";
        if (msg.contains("format")) return "Formatted code – Changed no logic, broke all PRs";
        if (msg.contains("indent")) return "Fixed indentation – Rage triggered";
        if (msg.contains("feature")) return "Added new feature – Created 5 new bugs";
        if (msg.contains("feature") && msg.contains("complete")) return "Feature complete – Except everything users want";
        if (msg.contains("ui")) return "Improved UI – Made UX worse";
        if (msg.contains("ux")) return "Improved UX – Confused everyone";
        if (msg.contains("design")) return "Updated design – Designer disapproves";
        if (msg.contains("comment")) return "Added comments – Made things less clear";
        if (msg.contains("note")) return "Left a note – No one understands it";
        if (msg.contains("todo")) return "Left a TODO – Forever ignored";
        if (msg.contains("migration")) return "Ran migration – Lost half the data";
        if (msg.contains("database")) return "Database optimized – For failure";
        if (msg.contains("schema")) return "Changed schema – Who needs backward compatibility?";
        if (msg.contains("email")) return "Fixed email sending – Now sends 100 times";
        if (msg.contains("notification")) return "Notifications improved – Now it's spam";
        if (msg.contains("thread")) return "Fixed threading issue – Introduced deadlock";
        if (msg.contains("async")) return "Made it async – Now unpredictable";
        if (msg.contains("performance")) return "Performance boosted – Server died faster";
        if (msg.contains("optimize")) return "Optimization complete – Code now unreadable";
        if (msg.contains("analytics")) return "Added analytics – Still guessing";
        if (msg.contains("tracking")) return "Tracking enabled – Even the bugs";
        if (msg.contains("crash")) return "Fixed crash – Crashed harder";
        if (msg.contains("error handling")) return "Error handling added – All errors ignored";
        if (msg.contains("mobile")) return "Improved mobile layout – On desktop";
        if (msg.contains("responsive")) return "Made site responsive – It now panics";
        if (msg.contains("favicon")) return "Updated favicon – Most important task of the sprint";
        if (msg.contains("version")) return "Version bump – Nothing else changed";
        if (msg.contains("sso")) return "Fixed SSO – Now Sucks So Often";
        if (msg.contains("api")) return "API improved – Docs still lying";
        if (msg.contains("merge")) return "Merged branch – Unleashed merge conflict hell";
        if (msg.contains("conflict")) return "Resolved conflict – Created war";
        if (msg.contains("rollback")) return "Rolled back – But nothing came back";
        if (msg.contains("hotfix")) return "Hotfix pushed – Fire intensifies";
        if (msg.contains("payment")) return "Fixed payment flow – No one can pay now";
        if (msg.contains("proxy")) return "Configured proxy – Blocked everything";
        if (msg.contains("timeout")) return "Fixed timeout – Now it times out faster";
        if (msg.contains("experiment")) return "New experiment – Nobody survived";
        if (msg.contains("monitoring")) return "Monitoring enabled – Still missed the crash";

        return capitalizeFirst(input.trim()) + " – Sounds legit, breaks everything anyway";
    }


    private String[] splitMemeText(String memePrompt) {
        if (memePrompt.contains("–")) {
            return memePrompt.split("–", 2);
        } else if (memePrompt.contains(":")) {
            return memePrompt.split(":", 2);
        }

        int mid = memePrompt.length() / 2;
        int spaceIndex = memePrompt.indexOf(" ", mid);
        if (spaceIndex != -1) {
            return new String[]{
                    memePrompt.substring(0, spaceIndex),
                    memePrompt.substring(spaceIndex + 1)
            };
        }

        return new String[]{memePrompt};
    }

    private String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) return "";
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
