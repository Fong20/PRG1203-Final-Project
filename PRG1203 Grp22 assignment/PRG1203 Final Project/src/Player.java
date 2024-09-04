import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name; // Player's name
    private List<Pokemon> caughtPokemons; // List of caught Pokémon
    private List<Pokemon> pokemonsForBattle; // List of Pokémon for battle
    private int score; // Player's score

    // Constructor to initialize player with a name
    public Player(String name) {
        this.name = name;
        this.caughtPokemons = new ArrayList<>(); // Initialize caughtPokemons list
        this.pokemonsForBattle = new ArrayList<>(); // Initialize pokemonsForBattle list
        this.score = score;
    }

    // Getter for player's name
    public String getName() {
        return name;
    }

    // Getter for caught Pokémon list
    public List<Pokemon> getCaughtPokemons() {
        return new ArrayList<Pokemon>(caughtPokemons);
    }

    // Getter for Pokémon for battle list
    public List<Pokemon> getPokemonsForBattle() {
        return new ArrayList<Pokemon>(pokemonsForBattle);
    }

    // Getter for player's score
    public int getScore() {
        return score;
    }

    // Setter for player's score
    public void setScore(int score) {
        this.score = score;
    }

    // Method to update player's score
    public void updateScore(int newScore) {
        this.score = newScore;
    }

    // Method to add a Pokémon to the caughtPokemons list
    public void addPokemonToCaughtPokemons(Pokemon pokemon) {
        caughtPokemons.add(pokemon);
    }

    // Method to add a Pokémon to the pokemonsForBattle list
    public void addPokemonToBattle(Pokemon pokemon) {
        pokemonsForBattle.add(pokemon);
    }

    // Method to display player's score
    public void showScore() {
        System.out.println("Score: " + getScore());
    }

    // Method to display all caught Pokémon
    public void showCaughtPokemons() {
        System.out.println(name + "'s Pokémon Database:");
        if (caughtPokemons.isEmpty()) {
            System.out.println("No Pokémon caught yet.");
        } else {
            for (Pokemon pokemon : caughtPokemons) {
                System.out.println(pokemon.getName() + " (HP: " + pokemon.getHp() + ", Attack: "
                        + pokemon.getAttackPower() + ", Type: " + pokemon.getType() + ")");
            }
        }
    }

    // Static setter for caughtPokemons list
    public static void setCaughtPokemons(List<Pokemon> caughtPokemons) {
        caughtPokemons = new ArrayList<Pokemon>(caughtPokemons);
    }

    // Static setter for pokemonsForBattle list
    public static void setPokemonsForBattle(List<Pokemon> pokemonsForBattle) {
        pokemonsForBattle = new ArrayList<Pokemon>(pokemonsForBattle);
    }

}
