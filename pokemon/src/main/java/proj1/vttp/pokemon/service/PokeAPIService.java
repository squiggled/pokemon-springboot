package proj1.vttp.pokemon.service;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import proj1.vttp.pokemon.model.Pokemon;
import proj1.vttp.pokemon.model.PokemonType;
import proj1.vttp.pokemon.model.SimplePokemon;

//for fetching data from external API
@Service
public class PokeAPIService {

    private Logger logger = Logger.getLogger(PokeAPIService.class.getName());

    String URL_POKEDETAILS = "https://pokeapi.co/api/v2/pokemon/";

    RestTemplate template = new RestTemplate();
    List<SimplePokemon> pokemonToShow = new LinkedList<>();

    public List<SimplePokemon> showIntialPokemon() {
        return pokemonToShow;
    }

    public void storeInitialLoad(String response) {
        Reader reader = new StringReader(response);
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject outerObj = jsonReader.readObject();

        JsonArray innerArray = outerObj.getJsonArray("results");
        // System.out.println(innerArray);

        fetchSimplePokemonData(innerArray);
    }

    //for loading 20 more pokemon
    public List<SimplePokemon> load20More(Integer offset){
        String URL_LOADMORE = "https://pokeapi.co/api/v2/pokemon/?offset="+offset+"&limit=20";
        logger.log(Level.INFO, "🟢 PokeAPI called: %s".formatted(URL_LOADMORE));
        System.out.println("offset:" +offset);
        String response = template.getForObject(URL_LOADMORE, String.class);
        Reader reader = new StringReader(response);
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject outerObj = jsonReader.readObject();

        JsonArray innerArray = outerObj.getJsonArray("results");

        return fetchSimplePokemonData(innerArray);
    }

    //fetch simpler pokemon obj for load
    public List<SimplePokemon> fetchSimplePokemonData(JsonArray jsonArray) {
        pokemonToShow = new LinkedList<>(); //RESET the list in case we are loading 20 more pokemon
        for (JsonObject pokemon : jsonArray.getValuesAs(JsonObject.class)) {
            URL_POKEDETAILS = pokemon.getString("url");
            String pokeDetails = template.getForObject(URL_POKEDETAILS, String.class);
            Reader reader = new StringReader(pokeDetails);
            JsonReader jsonReader = Json.createReader(reader);
            JsonObject detailObj = jsonReader.readObject();

            // create pokemon obj
            SimplePokemon pokeObj = new SimplePokemon();
            // SET NAME
            pokeObj.setName(stringFormatter(detailObj.getString("name")));
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
                    pokeObj.setType1(mapTypeFromJson(typeName));
                    // System.out.println("type1: " + pokeObj.getType1());
                } else {
                    JsonObject type = typeObj.getJsonObject("type");
                    String typeName = type.getString("name");
                    pokeObj.setType2(mapTypeFromJson(typeName));
                    // System.out.println("type2: " + pokeObj.getType2());
                }
            }
            pokemonToShow.add(pokeObj);
        }
        return pokemonToShow;
    }

    //return 1 full Pokemon object
    public Pokemon getOnePokemon(Integer id) {
        try {
            String response = template.getForObject("https://pokeapi.co/api/v2/pokemon/ " + id.toString(), String.class);
            Reader reader = new StringReader(response);
            JsonReader jsonReader = Json.createReader(reader);
            JsonObject detailObj = jsonReader.readObject();

            // create pokemon obj
            Pokemon pokeObj = new Pokemon();
            // SET NAME
            pokeObj.setName(stringFormatter(detailObj.getString("name")));
            // System.out.println(pokeObj.getName()); //debug

            // SET ID
            pokeObj.setId(detailObj.getInt("id"));
            // System.out.println(pokeObj.getId());

            // SET ABILITY
            JsonArray abilitiesArray = detailObj.getJsonArray("abilities"); // directly get the jsonarray from detailobj
            for (JsonValue abilityValue : abilitiesArray) {
                JsonObject abilityObject = (JsonObject) abilityValue;
                JsonObject ability = abilityObject.getJsonObject("ability");
                String abilityName = ability.getString("name");
                pokeObj.setAbility(stringFormatter(abilityName));
                // System.out.println(pokeObj.getAbility());
            }

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
                    pokeObj.setType1(mapTypeFromJson(typeName));
                    // System.out.println("type1: " + pokeObj.getType1());
                } else {
                    JsonObject type = typeObj.getJsonObject("type");
                    String typeName = type.getString("name");
                    pokeObj.setType2(mapTypeFromJson(typeName));
                    // System.out.println("type2: " + pokeObj.getType2());
                }
            }

            // SET STATS
            JsonArray statsArray = detailObj.getJsonArray("stats");
            for (int i = 0; i < statsArray.size(); i++) {
                JsonObject statsObj = (JsonObject) statsArray.get(i);
                int baseStat = statsObj.getInt("base_stat");

                switch (i) {
                    case 0:
                        pokeObj.setBaseHP(baseStat);
                        break;
                    case 1:
                        pokeObj.setBaseAtk(baseStat);
                        break;
                    case 2:
                        pokeObj.setBaseDef(baseStat);
                        break;
                    case 3:
                        pokeObj.setBaseSpA(baseStat);
                        break;
                    case 4:
                        pokeObj.setBaseSpD(baseStat);
                        break;
                    case 5:
                        pokeObj.setBaseSpe(baseStat);
                        break;
                    default:
                }
            }
            logger.log(Level.INFO, "🟢 Pokemon: %s found".formatted(pokeObj.getName()));
            return pokeObj;

        } catch (Exception e) {
            logger.log(Level.WARNING, "🔴 Failed to fetch Pokemon with ID " + id + ": " + e.getMessage());
            return null; 
        }
        
    }

    //get moves
    public List<String> getMoves(Integer pokemonId) {
        try {
             String response = template.getForObject("https://pokeapi.co/api/v2/pokemon/ " + pokemonId.toString(),
                String.class);
            Reader reader = new StringReader(response);
            JsonReader jsonReader = Json.createReader(reader);
            JsonObject detailObj = jsonReader.readObject();
            JsonArray movesArray = detailObj.getJsonArray("moves");

            List<String> movesList = new ArrayList<>();
            for (JsonObject obj : movesArray.getValuesAs(JsonObject.class)) {
                JsonObject moveObj = obj.getJsonObject("move");
                String moveName = moveObj.getString("name");
                String moveUrl = moveObj.getString("url");

                // fetch more details from the move URL
                String moveResponse = template.getForObject(moveUrl, String.class);
                Reader moveReader = new StringReader(moveResponse);
                JsonReader moveJsonReader = Json.createReader(moveReader);
                JsonObject moveDetailObj = moveJsonReader.readObject();
                Integer movePower = moveDetailObj.getInt("power", 0);
                Integer moveAcc = moveDetailObj.getInt("accuracy", 0);
                Integer movePp = moveDetailObj.getInt("pp", 0);

                JsonObject typeObject = moveDetailObj.getJsonObject("type");
                // retrieve "name" field from the "type" object
                String typeName = typeObject.getString("name");
                PokemonType moveType = mapTypeFromJson(typeName);


                String moveDetails = stringFormatter(moveName) + "," + moveType + "," + movePower + ","+ moveAcc + "," + movePp;
                logger.log(Level.INFO, "🟢 Move added to table %s".formatted(moveDetails));
                movesList.add(moveDetails);
            }
            return movesList;

        } catch (Exception e){
            logger.log(Level.WARNING, "🔴 Failed to fetch moves for Pokémon ID " + pokemonId + ": " + e.getMessage());
            return Collections.emptyList();
        }
        
    }

    
    public static String stringFormatter(String input) {
        String[] words = input.split("-");
        StringBuilder formatted = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                word = word.substring(0, 1).toUpperCase() + word.substring(1);
            }
            formatted.append(word).append(" ");
        }
        return formatted.toString().trim();
    }

    public static PokemonType mapTypeFromJson(String typeJson) {
        typeJson = typeJson.toLowerCase();

        switch (typeJson) {
            case "normal":
                return PokemonType.NORMAL;
            case "fire":
                return PokemonType.FIRE;
            case "water":
                return PokemonType.WATER;
            case "electric":
                return PokemonType.ELECTRIC;
            case "grass":
                return PokemonType.GRASS;
            case "ice":
                return PokemonType.ICE;
            case "fighting":
                return PokemonType.FIGHTING;
            case "poison":
                return PokemonType.POISON;
            case "ground":
                return PokemonType.GROUND;
            case "flying":
                return PokemonType.FLYING;
            case "psychic":
                return PokemonType.PSYCHIC;
            case "bug":
                return PokemonType.BUG;
            case "rock":
                return PokemonType.ROCK;
            case "ghost":
                return PokemonType.GHOST;
            case "dragon":
                return PokemonType.DRAGON;
            case "dark":
                return PokemonType.DARK;
            case "steel":
                return PokemonType.STEEL;
            case "fairy":
                return PokemonType.FAIRY;

            default:
                throw new IllegalArgumentException("Invalid or unsupported Pokemon type: " + typeJson);
        }
    }
}
