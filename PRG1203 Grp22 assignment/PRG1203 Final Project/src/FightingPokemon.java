// Subclass for Fighting type of Pokemon
class FightingPokemon extends Pokemon {

    // Constructor for FightingPokemon
    public FightingPokemon(String name, int hp, String type, int attackPower) {
        super(name, hp, type, attackPower);
    }

    // Override method to calculate effectiveness against an opponent
    @Override
    public double calculateEffectiveness(Pokemon opponent) {
        if (opponent.getDefenderType().equals("NORMAL")) {
            return 2.0; // Fighting is super effective against Normal
        } else if (opponent.getDefenderType().equals("POISON") || opponent.getDefenderType().equals("FLYING")) {
            return 0.5; // Fighting is not very effective against Poison and Flying
        }
        return 1.0; // Default effectiveness
    }
}
