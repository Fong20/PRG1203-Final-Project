// Subclass for Normal type of Pokemon
class NormalPokemon extends Pokemon {

    // Constructor for NormalPokemon
    public NormalPokemon(String name, int hp, String type, int attackPower) {
        super(name, hp, type, attackPower);
    }

    // Override method to calculate effectiveness against an opponent
    @Override
    public double calculateEffectiveness(Pokemon opponent) {
        return 1.0; // Default effectiveness for Normal type
    }
}
