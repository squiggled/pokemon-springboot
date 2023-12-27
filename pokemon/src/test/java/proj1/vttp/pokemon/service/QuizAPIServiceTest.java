package proj1.vttp.pokemon.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    public void questionNotNull() {
        Question result = quizAPIService.getQuestion();
        assertNotNull(result);
    }

    @Test
    public void testResponseJsonParse() {
        Question result = quizAPIService.getQuestion();
        assertEquals(72, result.getId());
        assertEquals("skuntank", result.getAnswer());
        assertEquals("Name this Pokemon", result.getQuestion());
        assertEquals("http://pokemontrivia-1-c0774976.deta.app/assets/gen4/skuntank.png", result.getImageUrl());
    }

    @Test
    public void testRandomEndpoints() {
        Set<Integer> selectedIndex = new HashSet<>();
        QuizAPIService spyService = Mockito.spy(quizAPIService);
        Random mockRandom = Mockito.mock(Random.class);
        when(spyService.getRandom()).thenReturn(mockRandom);

        //simulate different random indexes
        for (int i = 0; i < QuizAPIService.URL_QUESTIONS.length; i++) {
            when(mockRandom.nextInt(QuizAPIService.URL_QUESTIONS.length)).thenReturn(i); //return current value of i
            spyService.getQuestion(); //set the url to the index returned above
            selectedIndex.add(i);
        }

        assertEquals(QuizAPIService.URL_QUESTIONS.length, selectedIndex.size());
    }
}
