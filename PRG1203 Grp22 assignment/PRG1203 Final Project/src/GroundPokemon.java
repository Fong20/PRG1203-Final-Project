// Subclasses for Ground type of PokemonGround
class GroundPokemon extends Pokemon {

    // GroundPokemon constructor
    public GroundPokemon(String name, int hp, String type, int attackPower) {
        super(name, hp, type, attackPower);
    }

    // Override method to calculate effectiveness against an opponent
    @Override
    public double calculateEffectiveness(Pokemon opponent) {
        if (opponent.getDefenderType().equals("POISON")) {
            return 2.0; // Ground is super effective against Poison
        } else if (opponent.getDefenderType().equals("FLYING")) {
            return 0.0; // Ground has no effect on Flying
        }
        return 1.0; // Default effectiveness
    }
}
