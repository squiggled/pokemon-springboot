package proj1.vttp.pokemon.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proj1.vttp.pokemon.model.Party;
import proj1.vttp.pokemon.model.Pokemon;
import proj1.vttp.pokemon.repo.RedisRepository;

@Service
public class UserService {
    
    @Autowired
    RedisRepository redisRepository;

    private static final int[] BADGE_MILESTONES = {20, 50, 100, 150, 200, 300, 500, 1000};

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
}
