// Subclass for Poison type of Pokemon
class PoisonPokemon extends Pokemon {

    // Constructor for PoisonPokemon
    public PoisonPokemon(String name, int hp, String type, int attackPower) {
        super(name, hp, type, attackPower);
    }

    // Override method to calculate effectiveness against an opponent
    @Override
    public double calculateEffectiveness(Pokemon opponent) {
        if (opponent.getDefenderType().equals("POISON") || opponent.getDefenderType().equals("GROUND")) {
            return 0.5; // Poison is not very effective against Poison and Ground
        }
        return 1.0; // Default effectiveness
    }
}
