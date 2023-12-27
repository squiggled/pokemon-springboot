package proj1.vttp.pokemon.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class PokeAPIServiceTest {
    
    @Mock
    private RestTemplate template;

    @InjectMocks
    private PokeAPIService pokeAPIService;

}
