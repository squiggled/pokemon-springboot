package proj1.vttp.pokemon.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import proj1.vttp.pokemon.model.Party;
import proj1.vttp.pokemon.model.Pokemon;
import proj1.vttp.pokemon.model.SimplePokemon;
import proj1.vttp.pokemon.service.PokeAPIService;
import proj1.vttp.pokemon.service.UserService;
import proj1.vttp.pokemon.utils.UserUtils;

//for search and initial load
@Controller
public class PokedexController {

    private Logger logger = Logger.getLogger(PokedexController.class.getName());

    @Autowired
    PokeAPIService pokeAPIService;
    @Autowired
    UserService userService;

    //LOAD POKEMON PAGE
    // get landing page
    @GetMapping("/")
    public String intialLoad(Model model) {
        List<SimplePokemon> pokemonList = pokeAPIService.showIntialPokemon();
        model.addAttribute("pokemonList", pokemonList);
        return "index";
    }

    // get pokemon detail page
    @GetMapping("/pokemon/{id}")
    public String pokedexEntry(@PathVariable Integer id, Model model) {
        // System.out.println("id obtained " + id);
        Pokemon pokemon = pokeAPIService.getOnePokemon(id);
        model.addAttribute("pokemon", pokemon);
        return "poke-entry";
    }

    // LOGIN
    // render login page
    @GetMapping("/login")
    public String login(HttpSession session, Model model) {
        String username = (String) session.getAttribute(UserUtils.USER_SESSION);
        if (username != null && !username.equals("default")) {
            model.addAttribute("currentUser", username);
            return "login";
        }
        return "login";
    }

    // submit login
    @PostMapping("/login")
    public String processLogin(@RequestParam(name = "login", required = false, defaultValue = "default") String login,
            HttpSession session, Model model) {
        List<Pokemon> userParty = userService.getParty(login); // find party from redis
        session.setAttribute(UserUtils.USER_SESSION, login); // set username as current session
        session.setAttribute(UserUtils.USER_PARTY, userParty); // set found party to current session
        model.addAttribute("userParty", userParty);

        return "redirect:/";
    }

    // logout
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // PARTY ADDING
    // add to party
    @PostMapping("/addToTeam")
    public String addToTeam(@RequestParam("pokemonId") Integer pokemonId, HttpSession session) {
        // fetch the team from the session
        List<Pokemon> currentParty = (List<Pokemon>) session.getAttribute(UserUtils.USER_PARTY);
        if (currentParty == null) {
            currentParty = new LinkedList<>();
        }
        // find the Pokémon
        Pokemon pokemon = pokeAPIService.getOnePokemon(pokemonId);
        // add to party list
        currentParty.add(pokemon);
        logger.log(Level.INFO, "🟢 Pokemon with Id %d added to party".formatted(pokemonId));
        // update the session
        session.setAttribute(UserUtils.USER_PARTY, currentParty);

        return "redirect:/";
    }

    //load party page
    @GetMapping("/party")
    public String party(Model model, HttpSession session){
        List<Party> party = (List<Party>) session.getAttribute(UserUtils.USER_PARTY);
        model.addAttribute("party", party);
        return "party";
    }

    //save party to database
    @PostMapping("/party")
    public String saveParty(HttpSession session){
        List<Pokemon> party = (List<Pokemon>) session.getAttribute(UserUtils.USER_PARTY);
        String username = (String) session.getAttribute(UserUtils.USER_SESSION);
        userService.save(party, username);
        return "redirect:/";

    }
    //delete pokemon from party
    @PostMapping("/party/delete")
    public String deletePokemon(@RequestParam("pokemonId") Integer pokemonId, HttpSession session){
        List<Pokemon> currentParty = (List<Pokemon>) session.getAttribute(UserUtils.USER_PARTY);
        if (currentParty != null) {
            currentParty.removeIf(pokemon -> pokemon.getId().equals(pokemonId));
            session.setAttribute(UserUtils.USER_PARTY, currentParty);
        }
        return "redirect:/party";
    }

    

}
