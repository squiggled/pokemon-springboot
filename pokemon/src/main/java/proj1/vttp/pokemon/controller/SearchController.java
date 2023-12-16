package proj1.vttp.pokemon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import proj1.vttp.pokemon.model.SimplePokemon;
import proj1.vttp.pokemon.service.SearchService;

@RestController
public class SearchController {
    
    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<List<SimplePokemon>> search(@RequestParam String query) {
        return ResponseEntity.ok(searchService.searchPokemon(query));
    }
    
}
