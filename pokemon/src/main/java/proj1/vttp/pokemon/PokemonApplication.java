package proj1.vttp.pokemon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import proj1.vttp.pokemon.service.PokeAPIService;

@SpringBootApplication
public class PokemonApplication implements CommandLineRunner{

	@Autowired
	PokeAPIService pokeAPIService;

	private String URL_INITIALLOAD = "https://pokeapi.co/api/v2/pokemon/?offset=0&limit=20";

	RestTemplate template = new RestTemplate();

	public static void main(String[] args) {
		SpringApplication.run(PokemonApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String response = template.getForObject(URL_INITIALLOAD, String.class);

		pokeAPIService.storeInitialLoad(response);
	}

}
