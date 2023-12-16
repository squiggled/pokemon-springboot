package proj1.vttp.pokemon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import proj1.vttp.pokemon.model.Pokemon;
import proj1.vttp.pokemon.model.SimplePokemon;
import proj1.vttp.pokemon.service.PokeAPIService;

//for search and initial load
@Controller
public class PokedexController {

    @Autowired
    PokeAPIService pokeAPIService;

    @GetMapping("/")
    public String intialLoad(Model model){
        List<SimplePokemon> pokemonList = pokeAPIService.showIntialPokemon();
        model.addAttribute("pokemonList", pokemonList);
        return "index";
    }

    @GetMapping("/pokemon/{id}")
    public String pokedexEntry(@PathVariable Integer id, Model model){
        System.out.println("id obtained "+ id);
        Pokemon pokemon = pokeAPIService.getOnePokemon(id);
        model.addAttribute("pokemon", pokemon);
        return "poke-entry";
    }
}
