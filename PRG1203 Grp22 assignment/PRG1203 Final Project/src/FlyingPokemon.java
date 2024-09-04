// Subclass for Flying type of Pokemon
class FlyingPokemon extends Pokemon {

    // Constructor for FlyingPokemon
    public FlyingPokemon(String name, int hp, String type, int attackPower) {
        super(name, hp, type, attackPower);
    }

    // Override method to calculate effectiveness against an opponent
    @Override
    public double calculateEffectiveness(Pokemon opponent) {
        if (opponent.getDefenderType().equals("FIGHTING")) {
            return 2.0; // Flying is super effective against Fighting
        }
        return 1.0; // Default effectiveness
    }
}
