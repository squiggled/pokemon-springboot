package proj1.vttp.pokemon.utils;

import proj1.vttp.pokemon.model.PokemonType;

public class TypeUtils {

    public static String getTypeColourClass(PokemonType type) {
        if (type == null)
            return ""; // Handle null case

        switch (type) {
            case NORMAL:
                return "bg-slate-200 text-black";
            case FIRE:
                return "bg-red-500 text-white";
            case WATER:
                return "bg-blue-500 text-white";
            case ELECTRIC:
                return "bg-yellow-400 text-white";
            case GRASS:
                return "bg-green-500 text-white";
            case ICE:
                return "bg-cyan-200 text-white";
            case FIGHTING:
                return "bg-amber-700 text-white";
            case POISON:
                return "bg-purple-800 text-white";
            case GROUND:
                return "bg-yellow-950 text-white";
            case FLYING:
                return "bg-blue-200 text-white";
            case PSYCHIC:
                return "bg-pink-500 text-white";
            case BUG:
                return "bg-lime-500 text-white";
            case ROCK:
                return "bg-stone-600 text-white";
            case GHOST:
                return "bg-slate-900 text-white";
            case DRAGON:
                return "bg-fuschia-500 text-white";
            case DARK:
                return "bg-gray-950 text-white";
            case STEEL:
                return "bg-slate-400 text-white";
            case FAIRY:
                return "bg-pink-200 text-white";
            default:
                return "";
        }
    }

}
