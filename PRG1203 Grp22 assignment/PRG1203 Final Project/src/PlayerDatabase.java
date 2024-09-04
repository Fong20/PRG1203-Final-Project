import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerDatabase {
    private List<Player> players; // List to store players
    private static final String filePath = "C:\\Users\\weitz\\eclipse-workspace\\PRG1203 Final Project\\src\\player_database.txt"; //Please change the filepath of the player database txt file according to the location which the file is stored

    // Constructor to initialize player list and load from file
    public PlayerDatabase() {
        this.players = new ArrayList<>();
        loadFromFile();
    }

    // Method to save player database to file
    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Player player : players) {
                // write player details
                writer.write("Player Name: " + player.getName());
                writer.newLine();

                // Write caught Pokémon details
                writer.write("Caught Pokémons:");
                writer.newLine();
                for (Pokemon pokemon : player.getCaughtPokemons()) {
                    writer.write("- " + pokemon.getName() + " (HP: " + pokemon.getHp() +
                            ", Attack: " + pokemon.getAttackPower() + ", Type: " + pokemon.getType() + ")");
                    writer.newLine();
                }
                // Write player score
                writer.write("Score: " + player.getScore());
                writer.newLine();
                writer.newLine(); // Blank line for separation between players
            }
            System.out.println("Player database saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving player database to file: " + e.getMessage());
        }
    }

    // Method to load player database from file
    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Player player = null;

            players.clear();

            while ((line = reader.readLine()) != null) {
                // System.out.println("Loading line: " + line); // Debugging line

                if (line.startsWith("Player Name: ")) {
                    if (player != null) {
                        players.add(player);
                    }
                    String playerName = line.substring("Player Name: ".length()).trim();
                    player = new Player(playerName);
                } else if (line.startsWith("Caught Pokémons:") && player != null) {
                    continue;
                } else if (line.startsWith("- ") && player != null) {
                    String[] parts = line.substring(2).split("\\(|,|\\)");
                    String name = parts[0].trim();
                    int hp = Integer.parseInt(parts[1].split(": ")[1].trim());
                    int attack = Integer.parseInt(parts[2].split(": ")[1].trim());
                    String type = parts[3].split(": ")[1].trim();
                    Pokemon pokemon = new Pokemon(name, hp, type, attack);
                    player.addPokemonToCaughtPokemons(pokemon);
                } else if (line.startsWith("Score: ") && player != null) {
                    int score = Integer.parseInt(line.substring("Score: ".length()).trim());
                    player.setScore(score);
                }
            }
            if (player != null) {
                players.add(player);
            }
            System.out.println("Player database loaded from " + filePath);
        } catch (IOException e) {
            System.err.println("Error loading player database from file: " + e.getMessage());
        }
    }

    // Getter for file path
    public String getFilePath() {
        return filePath;
    }

    // Method to get a player by name
    public Player getPlayer(String name) {
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    // Method to get a player's caught Pokémon list
    public List<Pokemon> getPlayerPokemon(String playerName) {
        Player player = getPlayer(playerName);
        if (player != null) {
            return new ArrayList<>(player.getCaughtPokemons());
        }
        return new ArrayList<>();
    }

    // Method to display a player's database
    public void displayPlayerDatabase(String name) {
        Player player = getPlayer(name);
        if (player != null) {
            System.out.println("Loading player...");
            System.out.println("\nPlayer found in the database.");
            System.out.println("------------------------------------------------");
            System.out.println(player.getName() + "'s Database");
            System.out.println("------------------------------------------------");
            player.showCaughtPokemons();
            player.showScore();
        } else {
            System.out.println("Player not found in the database.");
        }
    }

    // Method to find a player in the database or add a new player
    public Player findPlayerDatabase() {
        Scanner scanner = new Scanner(System.in);
        String playerName;

        // prompt the user to enter a vlid player name
        do {
            System.out.print("Enter player name: ");
            playerName = scanner.nextLine().trim();

            if (playerName.isEmpty()) {
                System.out.println("Player name cannot be empty.");
            } else if (playerName.chars().anyMatch(Character::isDigit)) {
                System.out.println("Please enter a valid player name. (cannot contain numbers)");
            }
        } while (playerName.isEmpty() || playerName.chars().anyMatch(Character::isDigit));

        Player player = getPlayer(playerName);
        if (player != null) {
            displayPlayerDatabase(playerName);
        } else {
            System.out.println("\nPlayer not found in database.");
            System.out.println("\nAdding new player...");
            addPlayer(playerName); // Add a new player if not found
            player = getPlayer(playerName); // Re-fetch the player after adding
        }
        return player;

    }

    // Method to add a new player to the database
    public Player addPlayer(String name) {
        Player existingPlayer = getPlayer(name);
        if (existingPlayer != null) {
            displayPlayerDatabase(name); // Display the player's details if already exists
            return existingPlayer; // Exit if player already exists
        }

        Scanner scanner = new Scanner(System.in);
        String capitalizedPlayerName = capitalizeFirstLetter(name);
        Player player = new Player(capitalizedPlayerName); // Create a new player with the given name

        System.out.println("Press ENTER to save player to the database.");
        scanner.nextLine(); // Wait for user to press ENTER

        players.add(player); // Add the new player to the list
        saveToFile(); // Save the updated player database to the file
        System.out.println("New player saved to the database.");

        return player;
    }

    // Helper method to capitalize the first letter of a name
    private String capitalizeFirstLetter(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    // Method to display top N scores
    public void displayTopScores(int topN) {
        List<Player> playerList = new ArrayList<>(players);
        playerList.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        System.out.println("Top " + topN + " Scores:");
        System.out.println("---------------------------");
        for (int i = 0; i < Math.min(topN, playerList.size()); i++) {
            Player player = playerList.get(i);
            System.out.println((i + 1) + ". " + player.getName() + " - Score: " + player.getScore());
        }
    }

}
