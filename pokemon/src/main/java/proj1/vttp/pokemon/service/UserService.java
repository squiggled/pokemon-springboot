package proj1.vttp.pokemon.service;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proj1.vttp.pokemon.model.Party;
import proj1.vttp.pokemon.model.Pokemon;
import proj1.vttp.pokemon.repo.RedisRepository;

@Service
public class UserService {
    private Logger logger = Logger.getLogger(UserService.class.getName());
    
    @Autowired
    RedisRepository redisRepository;

     public List<Pokemon> getParty(String user){ //logic to get the list<items> from cart
        List<Pokemon> savedParty;
        Party userParty = redisRepository.getParty(user);
        if (userParty == null){
            savedParty = new LinkedList<>();
        } else {
            savedParty = userParty.getParty();
        }
        return savedParty;
    }
    
    public void save(List<Pokemon> party, String username){
        Party userParty = new Party(party);
        redisRepository.saveParty(userParty, username);
        
    }
}
