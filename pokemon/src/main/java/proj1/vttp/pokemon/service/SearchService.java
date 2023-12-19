package proj1.vttp.pokemon.service;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import proj1.vttp.pokemon.model.SimplePokemon;

@Service
public class SearchService {
    private Logger logger = Logger.getLogger(SearchService.class.getName()); 

    private List<String> allPokemonNames = new ArrayList<>();
    private List<SimplePokemon> foundPokemon = new ArrayList<>();
    private String URL_ALLPOKEMON = "https://pokeapi.co/api/v2/pokemon?limit=1017";
    private String URL_POKEDETAILS = "https://pokeapi.co/api/v2/pokemon/";
    RestTemplate template = new RestTemplate();


    @PostConstruct
    public void init() {
        String response = template.getForObject(URL_ALLPOKEMON, String.class);
        Reader reader = new StringReader(response);
        JsonReader jReader = Json.createReader(reader);
        JsonObject obj = jReader.readObject();
        JsonArray results = obj.getJsonArray("results");

        for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            String name = result.getString("name");
            allPokemonNames.add(name);
        }
    }

    public List<SimplePokemon> searchPokemon(String query) {
        logger.log(Level.INFO, "🟢 Searching for %s ".formatted(query));
        return allPokemonNames.stream()
                .filter(name -> name.toLowerCase().contains(query.toLowerCase()))
                .map(this::getPokemonDetails)
                .collect(Collectors.toList());
    }

    private SimplePokemon getPokemonDetails(String name) {
        String pokeDetails = template.getForObject(URL_POKEDETAILS+name, String.class);
        Reader reader2 = new StringReader(pokeDetails);
        JsonReader jsonReader = Json.createReader(reader2);
        JsonObject detailObj = jsonReader.readObject();

        // create pokemon obj
        SimplePokemon pokeObj = new SimplePokemon();
        // SET NAME
        pokeObj.setName(PokeAPIService.stringFormatter(detailObj.getString("name")));
        // System.out.println(pokeObj.getName()); //debug

        // SET ID
        pokeObj.setId(detailObj.getInt("id"));
        // System.out.println(pokeObj.getId());

        // SET IMAGE URL
        String frontDefaultSpriteUrl = detailObj.getJsonObject("sprites").getString("front_default");
        pokeObj.setImageUrl(frontDefaultSpriteUrl);
        // System.out.println(pokeObj.getImageUrl());

        // SET TYPE
        JsonArray typesArray = detailObj.getJsonArray("types"); // directly get the jsonarray from detailobj
        for (int i = 0; i < typesArray.size(); i++) {
            JsonObject typeObj = (JsonObject) typesArray.get(i);
            if (i == 0) {
                JsonObject type = typeObj.getJsonObject("type");
                String typeName = type.getString("name");
                pokeObj.setType1(PokeAPIService.mapTypeFromJson(typeName));
                // System.out.println("type1: " + pokeObj.getType1());
            } else {
                JsonObject type = typeObj.getJsonObject("type");
                String typeName = type.getString("name");
                pokeObj.setType2(PokeAPIService.mapTypeFromJson(typeName));
                // System.out.println("type2: " + pokeObj.getType2());
            }
        }
        return pokeObj;
    }
}
