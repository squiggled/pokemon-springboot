package proj1.vttp.pokemon.service;

import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import proj1.vttp.pokemon.model.Question;

@Service
public class QuizAPIService {
    
    private Logger logger = Logger.getLogger(QuizAPIService.class.getName());
    RestTemplate template = new RestTemplate();
    private String URL_QUESTION = "https://pokemontrivia-1-c0774976.deta.app/trivia?endpoint=images";

    public Question getQuestion(){
        String response = template.getForObject(URL_QUESTION, String.class);
        logger.log(Level.INFO, "🟢 Pokemon Trivia API called: %s".formatted(URL_QUESTION));
        Reader reader = new StringReader(response);
        JsonReader jReader = Json.createReader(reader);
        JsonObject jObj = jReader.readObject();
        Question question = new Question();
        
        JsonObject innerObj = jObj.getJsonObject("specific");
        question.setId(jObj.getInt("id"));
        question.setAnswer(innerObj.getString("word"));
        question.setQuestion(innerObj.getString("imageText"));
        question.setImageUrl(innerObj.getString("image"));
        System.out.println(question);
        return question;
    }

}
