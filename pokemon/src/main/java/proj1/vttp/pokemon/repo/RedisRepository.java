package proj1.vttp.pokemon.repo;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import proj1.vttp.pokemon.model.Party;
import proj1.vttp.pokemon.model.User;

@Repository
public class RedisRepository {
    
    private Logger logger = Logger.getLogger(RedisRepository.class.getName()); 

    @Autowired
    @Qualifier("myredis")
    private RedisTemplate<String, Object> template;

    //PARTY / key = 'username'
    //get party
    public Party getParty(String username){ 
        String partyKey = username+"-party";
        Optional<Object> opt = Optional.ofNullable(template.opsForValue().get(partyKey));
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
        String partyKey = redisKey+"-party";
        template.opsForValue().set(partyKey, party);
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

    //register
    public boolean register(String username, User user) {
        Optional<Object> opt = Optional.ofNullable(template.opsForValue().get(username));
        if (opt.isPresent()){
            logger.log(Level.INFO, "🔴 User %s is already registered".formatted(username));
            return false;
        }
        template.opsForValue().set(username, user);
        logger.log(Level.INFO, "🟢 Successfully registered user %s".formatted(user));
        return true;
    } 

    //find login credentials
    public User findLogin(String user){
        Optional<Object> opt = Optional.ofNullable(template.opsForValue().get(user));
        if (opt.isPresent()){
            User userCredentials = (User) opt.get();
            logger.log(Level.INFO, "🟢 User credentials for %s found in database".formatted(user));
            return userCredentials;
        } else {
            return null;
        }

    }

}
