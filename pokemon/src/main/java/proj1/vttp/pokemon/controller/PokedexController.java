package proj1.vttp.pokemon.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import proj1.vttp.pokemon.model.Party;
import proj1.vttp.pokemon.model.Pokemon;
import proj1.vttp.pokemon.model.SimplePokemon;
import proj1.vttp.pokemon.model.User;
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

    // LOAD POKEMON PAGE
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
    public String processLogin(@RequestParam(name = "login", required = false) String login, 
        @RequestParam(name = "password", required = false) String password,
        HttpSession session, Model model) {

        if ((login.length()<3) || (login==null)){
            String error = "Please enter a username";
            model.addAttribute("error", error);
            return "login";
        }
        if (password==null){
            String error = "Please enter a password";
            model.addAttribute("error", error);
            return "login";
        }

        //verify credentials
        Boolean isValidLogin = userService.isValidLogin(login, password);
        if (isValidLogin){
            List<Pokemon> userParty = userService.getParty(login); // find party from redis
            Integer userScore = userService.getScore(login); //find current game score from redis
            session.setAttribute(UserUtils.USER_SCORE, userScore); //set score for current session
            session.setAttribute(UserUtils.USER_SESSION, login); // set username for current session
            session.setAttribute(UserUtils.USER_PARTY, userParty); // set found party to current session
            session.setAttribute(UserUtils.IS_LOGGED_IN, true); //flag login as true
            model.addAttribute("userParty", userParty);
            return "redirect:/";

        } else {
            String error = "Wrong username or password";
            model.addAttribute("error", error);
            return "login";
        }
        
    }

    //load register page
    @GetMapping("/register")
    public String registerPage(HttpSession session, Model model){
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if ((isLoggedIn==null) || (!isLoggedIn)){
            model.addAttribute("user", new User());
            return "register";
        }
        String username = (String) session.getAttribute(UserUtils.USER_SESSION);
        model.addAttribute("currentUser", username);
        return "register";
    }

    //register new user
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute User user, BindingResult result, HttpSession session, Model model){
        if (result.hasErrors()){
            model.addAttribute("user", user);
            return "register";   
        } 
        String password1 = user.getPassword1();
        String password2 = user.getPassword2();

        if (!password1.equals(password2)){
            model.addAttribute("user", user);
            model.addAttribute("message", "Passwords do not match");
            return "register";
        }

        Boolean canRegister = userService.register(user.getUsername(), user);
        if (!canRegister){
            model.addAttribute("user", user);
            model.addAttribute("message", "This user already exists. Please login.");
            return "register";
        }
        session.setAttribute(UserUtils.IS_LOGGED_IN, true); //log the user in
        session.setAttribute(UserUtils.USER_SESSION, user.getUsername());
        return "redirect:/";
    }

    // logout
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // PARTY
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

    // load party page
    @GetMapping("/party")
    public String party(Model model, HttpSession session) {
        List<Party> party = (List<Party>) session.getAttribute(UserUtils.USER_PARTY);
        String username = (String) session.getAttribute(UserUtils.USER_SESSION);
        Integer userScore = userService.getScore(username);
        List<String> badges = userService.countBadges(userScore); //calculate badges

        //render tooltip for each badge
        Map<String, String> badgeDescriptions = new HashMap<>();
        badgeDescriptions.put("badge-20", "Achieved 20 points on the Quiz Challenge");
        badgeDescriptions.put("badge-50", "Achieved 50 points on the Quiz Challenge");
        badgeDescriptions.put("badge-100", "Achieved 100 points on the Quiz Challenge");
        badgeDescriptions.put("badge-150", "Achieved 150 points on the Quiz Challenge");
        badgeDescriptions.put("badge-200", "Achieved 200 points on the Quiz Challenge");
        badgeDescriptions.put("badge-300", "Achieved 300 points on the Quiz Challenge");
        badgeDescriptions.put("badge-500", "Achieved 500 points on the Quiz Challenge");
        badgeDescriptions.put("badge-1000", "Achieved 1000 points on the Quiz Challenge");
        model.addAttribute("badgeDescriptions", badgeDescriptions);

        model.addAttribute("badges", badges);
        model.addAttribute("party", party);
        model.addAttribute("username", username);
        model.addAttribute("userScore", userScore);
        return "party";
    }

    // save party to database
    @PostMapping("/party")
    public String saveParty(HttpSession session) {
        List<Pokemon> party = (List<Pokemon>) session.getAttribute(UserUtils.USER_PARTY);
        String username = (String) session.getAttribute(UserUtils.USER_SESSION);
        userService.save(party, username);
        return "redirect:/";
    }

    // delete pokemon from party
    @PostMapping("/party/delete")
    public String deletePokemon(@RequestParam("pokemonId") Integer pokemonId, HttpSession session) {
        List<Pokemon> currentParty = (List<Pokemon>) session.getAttribute(UserUtils.USER_PARTY);
        if (currentParty != null) {
            currentParty.removeIf(pokemon -> pokemon.getId().equals(pokemonId));
            session.setAttribute(UserUtils.USER_PARTY, currentParty);
        }
        return "redirect:/party";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }

}
