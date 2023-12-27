package proj1.vttp.pokemon.service;

import java.io.Reader;
import java.io.StringReader;
import java.util.Random;
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
    public static String[] URL_QUESTIONS = {
        "https://pokemontrivia-1-c0774976.deta.app/trivia?endpoint=gen1",
        "https://pokemontrivia-1-c0774976.deta.app/trivia?endpoint=gen2",
        "https://pokemontrivia-1-c0774976.deta.app/trivia?endpoint=gen3",
        "https://pokemontrivia-1-c0774976.deta.app/trivia?endpoint=gen4",
        "https://pokemontrivia-1-c0774976.deta.app/trivia?endpoint=gen5",
        "https://pokemontrivia-1-c0774976.deta.app/trivia?endpoint=gen6",
        "https://pokemontrivia-1-c0774976.deta.app/trivia?endpoint=gen7",
        "https://pokemontrivia-1-c0774976.deta.app/trivia?endpoint=bonus", 
        "https://pokemontrivia-1-c0774976.deta.app/trivia?endpoint=images"};

    public Question getQuestion(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(9);
        String response = template.getForObject(URL_QUESTIONS[randomNumber], String.class);
        logger.log(Level.INFO, "🟢 Pokemon Trivia API called: %s".formatted(URL_QUESTIONS[randomNumber]));
        Reader reader = new StringReader(response);
        JsonReader jReader = Json.createReader(reader);
        JsonObject jObj = jReader.readObject();
        Question question = new Question();
        
        JsonObject innerObj = jObj.getJsonObject("specific");
        question.setId(jObj.getInt("id"));
        question.setAnswer(innerObj.getString("word"));
        question.setQuestion(innerObj.getString("imageText"));
        question.setImageUrl(innerObj.getString("image"));
        return question;
    }

    public Object getRandom() {
        return null;
    }

}
