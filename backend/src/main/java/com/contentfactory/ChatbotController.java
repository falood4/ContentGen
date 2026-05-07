package com.contentfactory;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campaign")
@CrossOrigin(origins = "*")
public class ChatbotController {

    private final ContentGenerationService generationService;

    public ChatbotController(ContentGenerationService generationService) {
        this.generationService = generationService;
    }

    @PostMapping(value = "/generate", produces = "application/json")
    public String generate(@RequestBody String body) {
        try {
            JSONObject jsonbody = new JSONObject(body);
            String sourceText = jsonbody.getString("sourceText");
            return generationService.generate(sourceText);
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("error", e.getMessage());
            throw new RuntimeException(error.toString());
        }
    }
}
