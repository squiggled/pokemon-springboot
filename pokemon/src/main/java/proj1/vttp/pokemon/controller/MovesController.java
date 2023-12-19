package proj1.vttp.pokemon.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import proj1.vttp.pokemon.service.PokeAPIService;

@RestController
public class MovesController {
    @Autowired
    PokeAPIService pokeAPIService;

    // LOADING MOVES
    @GetMapping("/moves/{pokemonId}")
    public ResponseEntity<?> getMoves(@PathVariable Integer pokemonId) {
        List<String> movesList = pokeAPIService.getMoves(pokemonId);

        return ResponseEntity.ok(movesList);
    }
}
