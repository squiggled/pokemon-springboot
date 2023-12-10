package proj1.vttp.pokemon.model;

public class Move {
    private String moveName;
    private PokemonType type;
    private Integer power;
    private Integer accuracy;
    private Integer pp;

    
    public Move() {}

    public Move(String moveName, PokemonType type, Integer power, Integer accuracy, Integer pp) {
        this.moveName = moveName;
        this.type = type;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = pp;
    }

    public String getMoveName() {
        return moveName;
    }
    public void setMoveName(String moveName) {
        this.moveName = moveName;
    }
    public PokemonType getType() {
        return type;
    }
    public void setType(PokemonType type) {
        this.type = type;
    }
    public Integer getPower() {
        return power;
    }
    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getPp() {
        return pp;
    }

    public void setPp(Integer pp) {
        this.pp = pp;
    }

    
}
