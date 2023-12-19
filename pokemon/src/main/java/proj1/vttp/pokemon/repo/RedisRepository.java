package proj1.vttp.pokemon.repo;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import proj1.vttp.pokemon.model.Party;

@Repository
public class RedisRepository {
    
    private Logger logger = Logger.getLogger(RedisRepository.class.getName()); 

    @Autowired
    @Qualifier("myredis")
    private RedisTemplate<String, Object> template;


    public Party getParty(String username){ 
        Optional<Object> opt = Optional.ofNullable(template.opsForValue().get(username));
        if (opt.isPresent()){
            Party userParty = (Party) opt.get();
            logger.log(Level.INFO, "🟢 Party for %s found in database".formatted(username));
            return userParty;
        }
        logger.log(Level.INFO, "🟡 Party for %s not found in database".formatted(username));
        return null;
    }

    public void saveParty(Party party, String redisKey){
        template.opsForValue().set(redisKey, party);
        logger.log(Level.INFO, "🟢 Party for %s saved to Redis".formatted(redisKey));
    }
}
