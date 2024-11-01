package util.ai;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.VertexAI.Builder;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;

public class GeminiVertexAI {
    private static final String projectId = "sfm-ai"; // cloud project
    private static final String location = "us-central1";
    private static final String modelName = "gemini-1.5-flash-001";
    private static final Builder vertexBuilder = (new Builder()).setLocation(location).setProjectId(projectId);


    public static String generate(String textPrompt){
       try{
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(VertexAI.class.getResourceAsStream("/key/key.json"));
            credentials = credentials.createScoped("https://www.googleapis.com/auth/cloud-platform");
            credentials.refreshIfExpired();
            AccessToken token = credentials.refreshAccessToken();

            VertexAI vertexAI =vertexBuilder.setCredentials(credentials).build();

            GenerationConfig gc = GenerationConfig.newBuilder().setTemperature(0.75f).build();

            GenerativeModel model = (new GenerativeModel.Builder())
                    .setModelName(modelName)
                    .setVertexAi(vertexAI)
                    .setSystemInstruction(ContentMaker.fromString("Sie sind ein Finanzmanager, der gerne Witze macht."))
                    .setGenerationConfig(gc).build();
            GenerateContentResponse response = model.generateContent(textPrompt);

            return ResponseHandler.getText(response);

       }
       catch (Exception e)
       {
           return "Ups an error occurred  \uD83D\uDE22";
       }
    }
}