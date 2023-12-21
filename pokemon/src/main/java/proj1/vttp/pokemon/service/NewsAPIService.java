package proj1.vttp.pokemon.service;

import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import proj1.vttp.pokemon.model.Article;

@Service
public class NewsAPIService {

    @Value("${newsapi.key}")
    private String newsAPIKey;

    RestTemplate template = new RestTemplate();
    private String URL_NEWSAPI = "https://newsapi.org/v2/everything?q=pokemon&apiKey=";
    List<Article> articles;

    public List<Article> getNews(){
        String response = template.getForObject(URL_NEWSAPI+newsAPIKey, String.class);
        Reader reader = new StringReader(response);
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject outer = jsonReader.readObject();
        JsonArray data = outer.getJsonArray("articles");
        articles = new LinkedList<>();
        for (JsonObject articleObj : data.getValuesAs(JsonObject.class)){
            Article article = new Article();
            article.setTitle(articleObj.getString("title"), "");
            article.setArticleUrl(articleObj.getString("url", ""));
            article.setDescription(articleObj.getString("description", ""));
            article.setTimestamp(articleObj.getString("publishedAt", ""));
            article.setImageUrl(articleObj.getString("urlToImage", "https://www.svg.com/img/gallery/pokemon-scarlet-violet-where-to-find-jigglypuffs-line-how-to-catch-them/intro-1683194987.webp"));
            articles.add(article);
        }
        return articles;

    }

    
}
