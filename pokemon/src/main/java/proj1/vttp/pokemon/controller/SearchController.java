package proj1.vttp.pokemon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import proj1.vttp.pokemon.model.SimplePokemon;
import proj1.vttp.pokemon.service.PokeAPIService;
import proj1.vttp.pokemon.service.SearchService;

@RestController
public class SearchController {

    @Autowired
    SearchService searchService;
    @Autowired
    PokeAPIService pokeAPIService;

    //search
    @GetMapping("/search")
    public ResponseEntity<List<SimplePokemon>> search(@RequestParam String query) {
        return ResponseEntity.ok(searchService.searchPokemon(query));
    }

    // load 20 more
    @GetMapping("/load20more")
    public ResponseEntity<List<SimplePokemon>> get20Pokemon(Model model,
            @RequestParam(name = "offset", defaultValue = "40") int offset) {
        List<SimplePokemon> pokemonList = pokeAPIService.load20More(offset);
        return ResponseEntity.ok(pokemonList);
    }

}
