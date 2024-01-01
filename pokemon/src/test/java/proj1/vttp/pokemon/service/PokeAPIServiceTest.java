package proj1.vttp.pokemon.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import proj1.vttp.pokemon.model.Pokemon;
import proj1.vttp.pokemon.model.PokemonType;

@SpringBootTest
public class PokeAPIServiceTest {
    
    @Mock
    private RestTemplate template;

    @InjectMocks
    private PokeAPIService pokeAPIService;

    private String mockPikachuResponse = "{"
            + "\"name\": \"pikachu\","
            + "\"id\": 25,"
            + "\"abilities\": [{\"ability\": {\"name\": \"static\"}}],"
            + "\"sprites\": {\"front_default\": \"http://pikachutestsample.com\"},"
            + "\"stats\": ["
            + "    {\"base_stat\": 35, \"effort\": 0, \"stat\": {\"name\": \"hp\", \"url\": \"https://pokeapi.co/api/v2/stat/1/\"}},"
            + "    {\"base_stat\": 55, \"effort\": 0, \"stat\": {\"name\": \"attack\", \"url\": \"https://pokeapi.co/api/v2/stat/2/\"}},"
            + "    {\"base_stat\": 40, \"effort\": 0, \"stat\": {\"name\": \"defense\", \"url\": \"https://pokeapi.co/api/v2/stat/3/\"}},"
            + "    {\"base_stat\": 50, \"effort\": 0, \"stat\": {\"name\": \"special-attack\", \"url\": \"https://pokeapi.co/api/v2/stat/4/\"}},"
            + "    {\"base_stat\": 50, \"effort\": 0, \"stat\": {\"name\": \"special-defense\", \"url\": \"https://pokeapi.co/api/v2/stat/5/\"}},"
            + "    {\"base_stat\": 90, \"effort\": 2, \"stat\": {\"name\": \"speed\", \"url\": \"https://pokeapi.co/api/v2/stat/6/\"}}"
            + "],"
            + "\"types\": ["
            + "    {\"slot\": 1, \"type\": {\"name\": \"electric\", \"url\": \"https://pokeapi.co/api/v2/type/13/\"}}"
            + "]"
            + "}";

    @BeforeEach
    public void setup() {
        when(template.getForObject(anyString(), eq(String.class))).thenReturn(mockPikachuResponse);
    }

    @Test
    public void testGetOnePokemon() {
        Integer pokemonId = 25; 
        Pokemon result = pokeAPIService.getOnePokemon(pokemonId);

        assertEquals("Pikachu", result.getName()); //test should reflect the data that has been modified by stringFormatter or you other methods
        assertEquals(25, result.getId());
        assertEquals("Static", result.getAbility());
        assertEquals("http://pikachutestsample.com", result.getImageUrl());
        assertEquals(PokemonType.ELECTRIC, result.getType1());
        assertNull(result.getType2(), "Type 2 should be null for Pikachu");
        assertEquals(35, result.getBaseHP());
        assertEquals(55, result.getBaseAtk());
        assertEquals(40, result.getBaseDef());
        assertEquals(50, result.getBaseSpA());
        assertEquals(50, result.getBaseSpD());
        assertEquals(90, result.getBaseSpe());
    }

}
