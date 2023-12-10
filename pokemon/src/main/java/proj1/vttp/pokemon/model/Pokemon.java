package proj1.vttp.pokemon.model;

import java.util.List;

public class Pokemon {
    private String name;
    private Integer id;
    private String ability;
    private List<Move> moves;
    private PokemonType type1;
    private PokemonType type2 = null;
    private String imageUrl;
    private Integer baseHP;
    private Integer baseAtk;
    private Integer baseDef;
    private Integer baseSpA;
    private Integer baseSpD;
    private Integer baseSpe;

    public Pokemon() { }

    public Pokemon(String name, Integer id, String ability, List<Move> moves, PokemonType type1, PokemonType type2,
            String imageUrl, Integer baseHP, Integer baseAtk, Integer baseDef, Integer baseSpA, Integer baseSpD,
            Integer baseSpe) {
        this.name = name;
        this.id = id;
        this.ability = ability;
        this.moves = moves;
        this.type1 = type1;
        this.type2 = type2;
        this.imageUrl = imageUrl;
        this.baseHP = baseHP;
        this.baseAtk = baseAtk;
        this.baseDef = baseDef;
        this.baseSpA = baseSpA;
        this.baseSpD = baseSpD;
        this.baseSpe = baseSpe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public PokemonType getType1() {
        return type1;
    }

    public void setType1(PokemonType type1) {
        this.type1 = type1;
    }

    public PokemonType getType2() {
        return type2;
    }

    public void setType2(PokemonType type2) {
        this.type2 = type2;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getBaseHP() {
        return baseHP;
    }

    public void setBaseHP(Integer baseHP) {
        this.baseHP = baseHP;
    }

    public Integer getBaseAtk() {
        return baseAtk;
    }

    public void setBaseAtk(Integer baseAtk) {
        this.baseAtk = baseAtk;
    }

    public Integer getBaseDef() {
        return baseDef;
    }

    public void setBaseDef(Integer baseDef) {
        this.baseDef = baseDef;
    }

    public Integer getBaseSpA() {
        return baseSpA;
    }

    public void setBaseSpA(Integer baseSpA) {
        this.baseSpA = baseSpA;
    }

    public Integer getBaseSpD() {
        return baseSpD;
    }

    public void setBaseSpD(Integer baseSpD) {
        this.baseSpD = baseSpD;
    }

    public Integer getBaseSpe() {
        return baseSpe;
    }

    public void setBaseSpe(Integer baseSpe) {
        this.baseSpe = baseSpe;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pokemon{");
        sb.append("id=").append(id);
        sb.append(", ability='").append(ability).append('\'');
        sb.append(", moves=").append(moves);
        sb.append(", type1=").append(type1);
        sb.append(", type2=").append(type2);
        sb.append(", imageUrl='").append(imageUrl).append('\'');
        sb.append(", baseHP=").append(baseHP);
        sb.append(", baseAtk=").append(baseAtk);
        sb.append(", baseDef=").append(baseDef);
        sb.append(", baseSpA=").append(baseSpA);
        sb.append(", baseSpD=").append(baseSpD);
        sb.append(", baseSpe=").append(baseSpe);
        sb.append('}');
        return sb.toString();
    }
}
