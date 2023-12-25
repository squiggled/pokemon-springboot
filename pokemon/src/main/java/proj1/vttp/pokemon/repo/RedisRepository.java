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

    //PARTY / key = 'username'
    //get party
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

    //save party
    public void saveParty(Party party, String redisKey){
        template.opsForValue().set(redisKey, party);
        logger.log(Level.INFO, "🟢 Party for %s saved to Redis".formatted(redisKey));
    }

    //SCORE / key = 'username-quiz'
    //get score
    public Integer getScore(String username){
        String userScoreKey = username+"-quiz";
        Optional<Object> opt = Optional.ofNullable(template.opsForValue().get(userScoreKey));
        if (opt.isPresent()){
            Integer userScore = (Integer) opt.get();
            logger.log(Level.INFO, "🟢 Score for %s found in database".formatted(userScoreKey));
            return userScore;
        }
        logger.log(Level.INFO, "🟡 Party for %s not found in database".formatted(userScoreKey));
        return null;
    }

    //save score
    public void saveScore(Integer score, String username) {
        String scoreKey = username+"-quiz";
        template.opsForValue().set(scoreKey, score);
        logger.log(Level.INFO, "🟢 Score for %s saved to Redis".formatted(scoreKey));
    } 

}
