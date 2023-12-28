package proj1.vttp.pokemon.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proj1.vttp.pokemon.model.Party;
import proj1.vttp.pokemon.model.Pokemon;
import proj1.vttp.pokemon.model.User;
import proj1.vttp.pokemon.repo.RedisRepository;

@Service
public class UserService {

    private Logger logger = Logger.getLogger(UserService.class.getName());
    
    @Autowired
    RedisRepository redisRepository;

    private static final int[] BADGE_MILESTONES = {20, 50, 100, 150, 200, 300, 500, 1000};

    //LOGIN
    //login validation
    public Boolean isValidLogin(String username, String enteredPassword){
        User foundUser = redisRepository.findLogin(username);
        if (foundUser==null){
            logger.log(Level.INFO, "🔴 User %s does not exist in database".formatted(username));
            return false;
        }
        String foundPassword = foundUser.getPassword1();
        if (!foundPassword.equals(enteredPassword)){
            logger.log(Level.INFO, "🔴 Wrong password for user %s".formatted(username));
            return false;
        } else {
            logger.log(Level.INFO, "🟢 Successful login - user: %s".formatted(username));
            return true;
        }
    }

    //register
    public Boolean register(String username, User user){
        Boolean canRegister = redisRepository.register(username, user);
        return canRegister;
    }

    //PARTY
    //get party
     public List<Pokemon> getParty(String user){ 
        List<Pokemon> savedParty;
        Party userParty = redisRepository.getParty(user);
        if (userParty == null){
            savedParty = new LinkedList<>();
        } else {
            savedParty = userParty.getParty();
        }
        return savedParty;
    }
    
    //save party
    public void save(List<Pokemon> party, String username){
        Party userParty = new Party(party);
        redisRepository.saveParty(userParty, username);
    }

    //SCORES
    //get score
    public Integer getScore(String user){
        Integer userScore;
        Integer foundScore = redisRepository.getScore(user);
        if (foundScore == null){
            userScore=0;
        } else {
            userScore= foundScore;
        }
        return userScore;
    }

    //save score
    public void saveScore(Integer score, String username){
        redisRepository.saveScore(score, username);
    }

    //determine user badges - get a list of badge file names the user has
    public List<String> countBadges (Integer score){
        List<String> badges = new ArrayList<>();
        for (int milestone : BADGE_MILESTONES){
            if (score>= milestone){
                badges.add("badge-"+milestone);
            }
        }
        return badges;
    }

    //count points to next badge
    public Integer pointsToNextBadge(Integer currentScore) {
        for (int milestone : BADGE_MILESTONES) {
            if (currentScore < milestone) {
                return milestone - currentScore;
            }
        }
        return 0; //if all badges are attained
    }

    //determine next badge
    public String nextBadge(Integer currentScore){
        for (int milestone : BADGE_MILESTONES) {
            if (currentScore < milestone) {
                return "badge-"+milestone;
            }
        }
        return null; //if all badges are attained
    }
}
