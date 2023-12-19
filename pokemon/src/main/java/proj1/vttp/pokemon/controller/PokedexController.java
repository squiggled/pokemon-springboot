package proj1.vttp.pokemon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import proj1.vttp.pokemon.model.Pokemon;
import proj1.vttp.pokemon.model.SimplePokemon;
import proj1.vttp.pokemon.model.Party;
import proj1.vttp.pokemon.service.PokeAPIService;
import proj1.vttp.pokemon.service.UserService;
import proj1.vttp.pokemon.utils.UserUtils;

//for search and initial load
@Controller
public class PokedexController {

    @Autowired
    PokeAPIService pokeAPIService;
    @Autowired
    UserService userService;

    @GetMapping("/")
    public String intialLoad(Model model) {
        List<SimplePokemon> pokemonList = pokeAPIService.showIntialPokemon();
        model.addAttribute("pokemonList", pokemonList);
        return "index";
    }

    @GetMapping("/pokemon/{id}")
    public String pokedexEntry(@PathVariable Integer id, Model model) {
        System.out.println("id obtained " + id);
        Pokemon pokemon = pokeAPIService.getOnePokemon(id);
        model.addAttribute("pokemon", pokemon);
        return "poke-entry";
    }

    // render login page
    @GetMapping("/login")
    public String login(HttpSession session, Model model) {
        String username = (String) session.getAttribute(UserUtils.USER_SESSION);
        if (!session.getAttribute(UserUtils.USER_SESSION).equals("default")) {
            model.addAttribute("currentUser", username);
            return "login";  
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam(name = "login", required = false, defaultValue = "default") String login, HttpSession session, Model model) {
        List<Pokemon> userParty = userService.getParty(login); // find party from redis
        session.setAttribute(UserUtils.USER_SESSION, login); // set username as current session
        session.setAttribute(UserUtils.USER_PARTY, userParty); // set found party to current session
        model.addAttribute("userParty", userParty);

        return "redirect:/";
    }

}
