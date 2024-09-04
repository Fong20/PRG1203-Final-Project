import java.io.*;
import java.util.*;

public class Score {
    private PlayerDatabase playerDatabase;

    public Score(PlayerDatabase playerDatabase) {
        this.playerDatabase = playerDatabase;
    }

    // Method to calculate the score based on battle
    public int calculateScore(Pokemon pokemon, Pokemon opponent) {
        double effectiveness = pokemon.calculateEffectiveness(opponent);
        int attackPower = pokemon.getAttackPower();
        int hp = pokemon.getHp();
        int score = (int) ((attackPower * 10000) + (effectiveness * 10000) + (hp * 10000));
        return score;
    }

    // Method to update the score to file
    public void updateScoreInFile(String playerName, int newScore) {
        List<String> lines = new ArrayList<>();
        boolean playerFound = false;
        boolean scoreUpdated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(playerDatabase.getFilePath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Player Name: " + playerName)) {
                    playerFound = true;
                    lines.add(line); // Add the player's name
                    lines.add(reader.readLine()); // Skip "Caught Pokémons:"

                    // Process player Pokémon list
                    while ((line = reader.readLine()) != null && !line.startsWith("Score: ")) {
                        lines.add(line);
                    }

                    // Update score if the new score is higher
                    if (line != null && line.startsWith("Score: ")) {
                        try {
                            int existingScore = Integer.parseInt(line.substring("Score: ".length()).trim());
                            if (newScore > existingScore) {
                                lines.add("Score: " + newScore);
                                scoreUpdated = true;
                                System.out.println("Higher Than Previous Score! New Score Is Updated!");
                            } else {
                                lines.add(line);
                                System.out.println("Lower Than Previous Score! Remaining.........");
                                // Keep the existing score
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing score: " + e.getMessage());
                            // Optionally handle malformed score lines here
                            lines.add(line);
                        }
                    } else {
                        // If no "Score: " line found, add the new score
                        lines.add("Score: " + newScore);
                        scoreUpdated = true;
                    }
                } else {
                    lines.add(line); // Add non-matching lines
                }
            }

            if (!playerFound) {
                // If the player was not found, add the new player entry
                lines.add("Player Name: " + playerName);
                lines.add("Caught Pokémons:"); // Add a placeholder for Pokémon list
                lines.add("Score: " + newScore);
            }
        } catch (IOException e) {
            System.err.println("Error reading player database file: " + e.getMessage());
        }

        // Write updated data to file only if the score was updated or if the player was
        // new
        if (scoreUpdated || !playerFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(playerDatabase.getFilePath()))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Error writing to player database file: " + e.getMessage());
            }
        }
    }

    // Display the total battle score of the player
    public void calculateAndUpdateScores(Player player, List<Pokemon> opponents) {
        int totalScore = 0;
        List<Pokemon> pokemonsForBattle = player.getPokemonsForBattle();

        for (Pokemon pokemon : player.getPokemonsForBattle()) {
            for (Pokemon opponent : opponents) {
                int score = calculateScore(pokemon, opponent);
                totalScore += score;
            }
        }

        System.out.println("Total Battle Score: " + totalScore);

        // Update the player's total score if the new total score is higher
        if (totalScore > player.getScore()) {
            player.updateScore(totalScore);
        }

        // Save the updated player score to the database
        updateScoreInFile(player.getName(), player.getScore());

        // Display top 5 scores
        playerDatabase.displayTopScores(5);
    }
}
