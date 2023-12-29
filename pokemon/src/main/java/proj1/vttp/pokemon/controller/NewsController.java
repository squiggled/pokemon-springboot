package proj1.vttp.pokemon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import proj1.vttp.pokemon.model.Article;
import proj1.vttp.pokemon.service.NewsAPIService;

@Controller
public class NewsController {
    
    @Autowired
    NewsAPIService newsAPIService;
    
    @GetMapping("/news")
    public String news(Model model){
        List<Article> newsList = newsAPIService.getNews();
        if (newsList==null){
            return "error";
        }
        model.addAttribute("newsList", newsList);
        return "news";
    }
}
