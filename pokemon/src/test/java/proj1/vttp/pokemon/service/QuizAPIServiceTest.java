package proj1.vttp.pokemon.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import proj1.vttp.pokemon.model.Question;

@SpringBootTest
public class QuizAPIServiceTest {
    
    @Mock
    RestTemplate template;

    @InjectMocks
    QuizAPIService quizAPIService;

    String mockResponse = "{" +
        "\"id\": 72," +
        "\"label\": \"72\"," +
        "\"specific\": {" +
        "    \"hints\": [" +
        "        \"it sprays a vile-smelling fluid from the tip of its tail to attack\"," +
        "        \"its range is over 160 feet\"" +
        "    ]," +
        "    \"image\": \"http://pokemontrivia-1-c0774976.deta.app/assets/gen4/skuntank.png\"," +
        "    \"imageText\": \"Name this Pokemon\"," +
        "    \"word\": \"skuntank\"" +
        "}" +
    "}";

    @BeforeEach 
    public void setup() {
        when(template.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse); // return mockResponse if the conditions are met
    }

    @Test
    public void getNewsTest() {
        Question result = quizAPIService.getQuestion();
        assertNotNull(result);
    }
}

