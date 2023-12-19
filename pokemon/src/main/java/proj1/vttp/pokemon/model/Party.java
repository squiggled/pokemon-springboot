package proj1.vttp.pokemon.model;

import java.util.List;

public class Party {
    private List<Pokemon> party;
    
    public Party() {
    }

    public Party(List<Pokemon> party) {
        this.party=party;
    }

    public List<Pokemon> getParty() {
        return party;
    }

    public void setParty(List<Pokemon> party) {
        this.party = party;
    }

    
}
