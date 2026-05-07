package com.contentfactory;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ContentGenerationService {

    private final OpenRouterClient openRouterClient;

    public ContentGenerationService(OpenRouterClient openRouterClient) {
        this.openRouterClient = openRouterClient;
    }

    public String generate(String sourceText) {
        try {
            // Fact check agent
            String FactCheckPrompt = "You are a Fact-Check Agent. Read the user's source text and extract the core product features, technical specs, and target audience. Output a structured JSON object representing the Fact-Sheet.DO NOT ADD PADDED TEXT OTHER THAN JSON. Format: {\"coreFeatures\": [], \"technicalSpecs\": [], \"targetAudience\": \"\", \"ambiguousStatements\": []}";
            String FactSheetString = openRouterClient.generate(FactCheckPrompt, sourceText);
            String FactSheetJson = JsonConvert(FactSheetString);

            // Copywrite agent
            String CopywritePrompt = "You are the Creative Copywriter Agent. Based on the Fact-Sheet JSON, produce: 1) A 500-word Blog Post with porfessional/business tone 2) An 5-post Twitter Thread. Use (x/5) to show how thread is divided 3) A one paaragraph Email Teaser. Output MUST be a strict JSON object. DO NOT ADD PADDED TEXT OTHER THAN JSON. {\"blog\": \"...\", \"social\": \"...\", \"email\": \"...\"}";
            String CopywriteString = openRouterClient.generate(CopywritePrompt,
                    "Fact-Sheet:\n" + FactSheetJson);
            String CopywriterJson = JsonConvert(CopywriteString);

            // Editor agent
            String FinalContentJson = CopywriterJson;
            boolean approved = false;
            int retries = 0;
            int retry_limit = 2;

            while (!approved && retries < retry_limit) {
                String EditorPrompt = "You are the Editor-in-Chief Agent. Compare the Copywriter's drafts against the Fact-Sheet. REJECT if there are hallucinations, invented features, or if tone is robotic. If rejected find ways to imporove the copywrite draft and provide them for feedback. Output a JSON object: {\"approved\": boolean, \"feedback\": \"Feedback here if rejected, else 'Approved'\"}";
                String UserPrompt = "FactSheet:\n" + FactSheetJson + "\n\nCopywriteDraft:\n" + FinalContentJson;

                String EditorResponse = openRouterClient.generate(EditorPrompt, UserPrompt);
                String EditorResponseJson = JsonConvert(EditorResponse);
                JSONObject EditorReview = new JSONObject(EditorResponseJson);

                approved = EditorReview.optBoolean("approved", false);
                String feedback = EditorReview.optString("feedback", "No feedback provided.");

                if (!approved) {
                    retries++;

                    if (retries < retry_limit) {
                        String retryprompt = "Your previous draft was rejected. Fact-Sheet:\n" + FactSheetJson
                                + "\n\nPrevious Draft:\n" + FinalContentJson + "\n\nEditor Feedback:\n" + feedback
                                + "\n\nRewrite all 3 pieces based on feedback. Output JSON ONLY: {\"blog\": \"...\", \"social\": \"...\", \"email\": \"...\"}";
                        String newcontent = openRouterClient.generate(CopywritePrompt, retryprompt);
                        FinalContentJson = JsonConvert(newcontent);
                    } else {
                        approved = true;
                    }
                }
            }

            return FinalContentJson;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate campaign: " + e.getMessage(), e);
        }
    }

    private String JsonConvert(String response) {
        int start = response.indexOf('{');
        int end = response.lastIndexOf('}');
        if (start != -1 && end != -1 && start <= end) {
            return response.substring(start, end + 1);
        }
        return response.trim();
    }
}
