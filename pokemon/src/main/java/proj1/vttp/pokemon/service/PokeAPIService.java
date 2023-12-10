package proj1.vttp.pokemon.service;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import proj1.vttp.pokemon.model.Move;
import proj1.vttp.pokemon.model.Pokemon;
import proj1.vttp.pokemon.model.PokemonType;

//for fetching data from external API
@Service
public class PokeAPIService {

    String URL_POKEDETAILS;
    RestTemplate template = new RestTemplate();
    List<Pokemon> pokemonToShow = new LinkedList<>();

    public List<Pokemon> showIntialPokemon(){
        return pokemonToShow;
    }

    public void storeInitialLoad(String response) {
        Reader reader = new StringReader(response);
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject outerObj = jsonReader.readObject();

        JsonArray innerArray = outerObj.getJsonArray("results");
        // System.out.println(innerArray);

        fetchPokemonData(innerArray);
    }

    public void fetchPokemonData(JsonArray jsonArray) {
        for (JsonObject pokemon : jsonArray.getValuesAs(JsonObject.class)) {
            URL_POKEDETAILS = pokemon.getString("url");
            String pokeDetails = template.getForObject(URL_POKEDETAILS, String.class);
            Reader reader = new StringReader(pokeDetails);
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

            // SET MOVES
            JsonArray movesArray = detailObj.getJsonArray("moves");
            List<Move> movesList = new ArrayList<>();

            for (JsonObject moveObject : movesArray.getValuesAs(JsonObject.class)) {
                JsonObject move = moveObject.getJsonObject("move");

                // retrieve move details
                String moveName = move.getString("name");
                String moveUrl = move.getString("url");

                // fetch move details from moveUrl using http
                String moveDetails = template.getForObject(moveUrl, String.class);
                JsonReader moveJsonReader = Json.createReader(new StringReader(moveDetails));
                JsonObject moveDetailObj = moveJsonReader.readObject();

                Move pokemonMove = new Move();

                pokemonMove.setPower(moveDetailObj.isNull("power") ? 0 : moveDetailObj.getInt("power"));
                pokemonMove.setMoveName(stringFormatter(moveName));
                pokemonMove.setPp(moveDetailObj.isNull("pp") ? 0 : moveDetailObj.getInt("pp"));
                pokemonMove.setAccuracy(moveDetailObj.isNull("accuracy") ? 0 : moveDetailObj.getInt("accuracy"));

                // retrieve "type" object
                JsonObject typeObject = moveDetailObj.getJsonObject("type");

                // retrieve "name" field from the "type" object
                String typeName = typeObject.getString("name");
                pokemonMove.setType(mapTypeFromJson(typeName));
                // System.out.println(pokemonMove.getType());

                // add PokemonMove to the list
                movesList.add(pokemonMove);
            }
            // set the list of moves in the Pokemon object
            pokeObj.setMoves(movesList);

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
            pokemonToShow.add(pokeObj);
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

    public PokemonType mapTypeFromJson(String typeJson) {
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
