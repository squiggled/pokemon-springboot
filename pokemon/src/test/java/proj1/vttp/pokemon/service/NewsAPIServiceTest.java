package proj1.vttp.pokemon.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import proj1.vttp.pokemon.model.Article;
import proj1.vttp.pokemon.model.Question;

@SpringBootTest
public class NewsAPIServiceTest {

    @Mock
    private RestTemplate template;

    @InjectMocks
    private NewsAPIService newsAPIService;

    private String mockResponse = "{"
            + "\"status\": \"ok\","
            + "\"totalResults\": 1,"
            + "\"articles\": ["
            + "  {"
            + "    \"source\": { \"id\": null, \"name\": \"sample source\" },"
            + "    \"author\": \"sample author\","
            + "    \"title\": \"sample title\","
            + "    \"description\": \"sample description.\","
            + "    \"url\": \"http://sample.com/article\","
            + "    \"urlToImage\": \"http://sample.com/image.jpg\","
            + "    \"publishedAt\": \"2023-11-28T10:10:34Z\","
            + "    \"content\": \"sample content.\""
            + "  }"
            + "]"
            + "}";

    @BeforeEach // @Beforeeach -> makes sure method is executed before each test method in thecurrent class
    public void setup() {
        when(template.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse); // return mockResponse if the conditions are met
    }

    @Test
    public void newsNotNull() {
        List<Article> result = newsAPIService.getNews();
        assertTrue(!result.isEmpty());
    }

     @Test
    public void testResponseJsonParse() {
        List<Article> result = newsAPIService.getNews();
        for (Article article : result){
            assertEquals("http://sample.com/article", article.getArticleUrl());
            assertEquals("sample description.", article.getDescription());
            assertEquals("sample title", article.getTitle());
            // assertEquals("http://pokemontrivia-1-c0774976.deta.app/assets/gen4/skuntank.png", result.getImageUrl());
        }   
        
    }

}
