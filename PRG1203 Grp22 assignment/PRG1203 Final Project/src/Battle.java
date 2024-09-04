import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Battle {
    // Static variables to hold player, Pokémon lists, and other necessary objects
    private static Player player;
    private static List<Pokemon> wildPokemons;
    private static List<Pokemon> opponentPokemons;
    private static List<Pokemon> savedOpponentPokemons; // Variable to save opponent Pokémon state
    private static Random random;
    private static Scanner scanner;
    private static boolean battleHandled = false;
    private static PlayerDatabase playerDatabase;
    private static Score score;

    // Constructor to initialize the battle
    public Battle(Player player, List<Pokemon> wildPokemons, PlayerDatabase playerDatabase, Score score) {
        Battle.player = player;
        Battle.wildPokemons = wildPokemons;
        Battle.playerDatabase = playerDatabase;
        Battle.score = score;
        random = new Random();
        scanner = new Scanner(System.in);
    }

    // Method to handle the Pokémon catching phase
    public void catchTime() {
        frameText("Catch Time!");

        int catchAttempt = 1;

        // Loop to catch wild Pokémon
        while (!wildPokemons.isEmpty()) {

            if (catchAttempt == 1) {
                System.out.println("Three wild Pokémon appeared!");
            } else if (catchAttempt == 2) {
                System.out.println("Two wild Pokémon left!");
            } else if (catchAttempt == 3) {
                System.out.println("One wild Pokémon left!");
            }

            catchAttempt += 1;

            // Display the three wild Pokémon
            for (int i = 0; i < wildPokemons.size(); i++) {
                Pokemon wildPokemon = wildPokemons.get(i);
                System.out.println((i + 1) + ". " + wildPokemon.getName() + " (HP: " + wildPokemon.getHp() +
                        ", Attack: " + wildPokemon.getAttackPower() + ", Type: " + wildPokemon.getType() + ")");
            }

            // Prompt the player to choose which Pokémon to catch
            System.out.println("Please choose a wild Pokémon to catch (enter number):");
            int pokemonChoice;
            do {
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input! Please enter a number.");
                    scanner.next(); // Consume the invalid input
                }
                pokemonChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                if (pokemonChoice <= 0 || pokemonChoice > wildPokemons.size()) {
                    System.out.println(
                            "Invalid choice. Please enter a number between 1 and " + wildPokemons.size() + ".");
                }
            } while (pokemonChoice <= 0 || pokemonChoice > wildPokemons.size());

            // Generate random types of pokeball
            Pokeball.setBallType(random.nextInt(4));
            System.out.println("You've chosen a " + Pokeball.getBallType() + " to catch a Pokémon!");
            Pokeball.catchRate(); // Set the the ball's catch rate

            Pokemon chosenWildPokemon = wildPokemons.remove(pokemonChoice - 1);

            // If the ball catch rate is larger than the probability of catching the
            // pokemon, add the pokemon to the player's obtained pokemon list
            if (Pokeball.getBallCatchRate() > random.nextDouble(1)) { // Generate a random double value from 0 to 1

                System.out.println("Congratulations! You caught " + chosenWildPokemon.getName() + "!");
                player.addPokemonToCaughtPokemons(chosenWildPokemon);

            } else { // If the ball catch rate is samller than the probability of catching the
                     // pokemon, display a message that the pokemon was failed to be caught
                System.out.println("Oh no! The Pokémon broke free!");
            }

            // Prompt the player if he/she wants to catch another wild Pokémon
            String catchMore;

            if (catchAttempt < 3) {
                do {
                    System.out.println("Do you want to catch another wild Pokémon? (enter 'Y' for yes or 'N' for no):");
                    catchMore = scanner.nextLine();
                } while (!catchMore.equalsIgnoreCase("Y") && !catchMore.equalsIgnoreCase("N"));

                if (catchMore.equalsIgnoreCase("N")) {
                    break; // Exit the loop if player chooses not to catch more Pokémon
                }
            }
        }

        // Save the caught wild pokemons to the player's database
        playerDatabase.saveToFile();
        System.out.println("Catch time ended!");
    }

    // Method to call Pokémon
    public void callPokemon() {
        frameText("Roar and call Pokémon!");

        System.out.println(
                "Press Enter and you'll be directed to a pop-up window where you can charge your Pokémon's spirit!");

        // Wait for Enter key press
        scanner.nextLine();

        // Open the pop-up window
        ButtonMashGame.startButtonMashGame();

        System.out.println("Two wild Pokémon appeared!");
        opponentPokemons = generateTwoRandomPokemons();

        // Display the two wild Pokémon
        for (int j = 0; j < opponentPokemons.size(); j++) {
            Pokemon opponentPokemon = opponentPokemons.get(j);
            System.out.println((j + 1) + ". " + opponentPokemon.getName() + " (HP: " + opponentPokemon.getHp() +
                    ", Attack: " + opponentPokemon.getAttackPower() + ", Type: " + opponentPokemon.getType() + ")");
        }

        // Prompt player to choose which Pokémon to battle
        frameText("Send out your Pokémon!");
        promptInsertOrRent();
    }

    // Method to prompt the player to either use their own Pokémon or rent one
    private void promptInsertOrRent() {
        int choice;
        do {
            System.out.println("Do you want to:");
            System.out.println("1. Select your owned Pokémon");
            System.out.println("2. Rent a random Pokémon");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume the invalid input
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            if (choice != 1 && choice != 2) {
                System.out.println("Invalid choice! Please enter 1 or 2.");
            }
        } while (choice != 1 && choice != 2);

        // Handle the player's choice
        switch (choice) {
            case 1:
                insertDisk();
                break;
            case 2:
                rentPokemon();
                break;
        }
    }

    // Method to allow the player to select their own Pokémon
    private void insertDisk() {
        frameText("Select your owned Pokémon");

        List<Pokemon> caughtPokemons = playerDatabase.getPlayerPokemon(player.getName());
        if (caughtPokemons.isEmpty()) {
            System.out.println("You have no caught Pokémon. Proceeding with renting a Pokémon.");
            rentPokemon();
        } else {
            while (player.getPokemonsForBattle().size() < 2) {
                System.out.println("Your caught Pokémon:");
                for (int i = 0; i < caughtPokemons.size(); i++) {
                    Pokemon caughtPokemon = caughtPokemons.get(i);
                    System.out.println((i + 1) + ". " + caughtPokemon.getName() + " (HP: " + caughtPokemon.getHp() +
                            ", Attack: " + caughtPokemon.getAttackPower() + ", Type: " + caughtPokemon.getType() + ")");
                }

                System.out.println("Enter the number of the Pokémon to battle or 'r' to rent a Pokémon:");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("r")) {
                    rentPokemon();
                } else {
                    try {
                        int pokemonChoice = Integer.parseInt(input);

                        if (pokemonChoice <= 0 || pokemonChoice > caughtPokemons.size()) {
                            System.out.println("Invalid choice. Please enter a number between 1 and "
                                    + caughtPokemons.size() + ".");
                        } else {
                            Pokemon chosenPokemon = caughtPokemons.get(pokemonChoice - 1);
                            player.addPokemonToBattle(chosenPokemon);
                            caughtPokemons.remove(pokemonChoice - 1); // Remove chosen Pokemon from list
                            System.out.println("You chose " + chosenPokemon.getName() + " for the battle.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter a number or 'r' to rent.");
                    }
                }
            }
        }
    }

    // Method to rent a random Pokémon
    private void rentPokemon() {
        frameText("Renting a Pokémon...");
        // Logic for renting a Pokémon
        Pokemon rentedPokemon = Pokemon.randomPokemon(); // Use the existing randomPokemon method

        System.out.println("You rented " + rentedPokemon.getName() + "!");

        // Add the Pokémon to player's PokemonsForBattle list
        player.addPokemonToBattle(rentedPokemon);

    }

    // Method to start the battle process
    public void startBattle() {
        while (player.getPokemonsForBattle().size() < 2) {
            System.out.println("You need to send out 2 Pokémon to battle. Choose a Pokémon to send out:");
            promptInsertOrRent();
        }

        frameText("Battle Starts!");

        // Start a PokemonRace game
        PokemonRace.startPokemonRace();

        // Prevent handleBattles() from being called
        battleHandled = true;

        System.out.println("Press Enter to proceed to the next stage.");
        scanner.nextLine();

        // Ready to call handleBattle()
        battleHandled = false;

        // Retrieve the allyWon value from PokemonRace
        boolean allyWon = PokemonRace.isAllyWon();

        handleBattle(allyWon); // Decide attack order based on race result
    }

    public static List<Pokemon> getOpponentPokemons() {
        return opponentPokemons;
    }

    // Method to handle the battle and its outcome
    public static void handleBattle(boolean allyWon) {
        if (!battleHandled) {
            List<Pokemon> allyPokemons = player.getPokemonsForBattle();
            List<Pokemon> opponentPokemons = Battle.opponentPokemons; // Directly use the opponentPokemons variable

            if (allyWon) {
                System.out.println("Ally Pokémon attacks first in the battle!");
                battleLoop(allyPokemons, opponentPokemons);
            } else {
                System.out.println("Opponent Pokémon attacks first in the battle!");
                battleLoop(opponentPokemons, allyPokemons);
            }

            battleHandled = true; // Mark that battle handling has occurred

            // After battle ends, check and display results
            if (checkBattleEnd(opponentPokemons)) {
                System.out.println("All opponent Pokémon have fainted! Time to catch them all!");
                promptCatch();
            } else if (checkBattleEnd(allyPokemons)) {
                System.out.println("Oh no! All ally Pokémon have fainted!");

                // Save the state of opponent Pokémon
                savedOpponentPokemons = new ArrayList<>();
                for (Pokemon pokemon : opponentPokemons) {
                    savedOpponentPokemons.add(new Pokemon(pokemon.getName(), pokemon.getHp(), pokemon.getType(),
                            pokemon.getAttackPower()));
                }

                ReviveGame.startReviveGame();

            } else {
                System.out.println("The battle ended.");
                score.calculateAndUpdateScores(player, opponentPokemons);
            }
        }
    }

    private static void battleLoop(List<Pokemon> firstAttackers, List<Pokemon> secondAttackers) {
        boolean isBattleOver = false;

        while (!isBattleOver) {
            for (Pokemon attacker : firstAttackers) {
                for (Pokemon defender : secondAttackers) {
                    if (!attacker.isDefeated() && !defender.isDefeated()) {
                        double effectiveness = attacker.calculateEffectiveness(defender);
                        attacker.attack(defender, effectiveness);

                        if (defender.isDefeated()) {
                            System.out.println(defender.getName() + " has fainted!");
                        }

                        if (checkBattleEnd(firstAttackers) || checkBattleEnd(secondAttackers)) {
                            isBattleOver = true;
                            break;
                        }
                    }
                }
                if (isBattleOver) {
                    break;
                }
            }
        }
    }

    private static boolean checkBattleEnd(List<Pokemon> pokemons) {
        Scanner scanner = new Scanner(System.in);

        for (Pokemon pokemon : pokemons) {
            if (!pokemon.isDefeated()) {
                return false;
            }
        }
        return true;
    }

    public static void continueBattleWithRevival(boolean gameWon) {
        if (gameWon) {
            System.out.println("You've revived your Pokémon! Continuing the battle...");

            // Revive the fainted Pokémon
            List<Pokemon> revivedPokemons = new ArrayList<>();
            for (Pokemon pokemon : player.getPokemonsForBattle()) {
                if (pokemon.isDefeated()) {
                    pokemon.revive(); // Restore Pokémon's HP
                    revivedPokemons.add(pokemon);
                }
            }

            if (revivedPokemons.isEmpty()) {
                System.out.println("No Pokémon available to continue the battle.");
                score.calculateAndUpdateScores(player, opponentPokemons);
                return;
            }

            frameText("Battle Continues!");

            // Continue the battle with revived Pokémon attacking saved opponent Pokémon
            for (Pokemon revivedPokemon : revivedPokemons) {
                for (Pokemon opponent : savedOpponentPokemons) {
                    if (!revivedPokemon.isDefeated() && !opponent.isDefeated()) {
                        double effectiveness = opponent.calculateEffectiveness(revivedPokemon);
                        revivedPokemon.attack(opponent, effectiveness);

                        if (opponent.isDefeated()) {
                            System.out.println(opponent.getName() + " has fainted!");
                        }

                        if (checkBattleEnd(savedOpponentPokemons)) {
                            System.out.println(
                                    "All opponent Pokémon have fainted! Time to catch them! \nPress Enter to start catching!");

                            new Scanner(System.in).nextLine();

                            promptCatch();
                            return;
                        }
                    }
                }
            }

            // Continue battle with remaining opponent Pokémon if any
            for (Pokemon opponent : savedOpponentPokemons) {
                if (!opponent.isDefeated()) {
                    for (Pokemon revivedPokemon : revivedPokemons) {
                        if (!revivedPokemon.isDefeated()) {
                            double effectiveness = opponent.calculateEffectiveness(revivedPokemon);
                            opponent.attack(revivedPokemon, effectiveness);

                            if (revivedPokemon.isDefeated()) {
                                System.out.println(revivedPokemon.getName() + " has fainted!");
                            }

                            if (checkBattleEnd(revivedPokemons)) {
                                System.out.println("All ally Pokémon have fainted! You lose.");
                                return;
                            }
                        }
                    }
                }
            }

            System.out.println("The battle continues...");

        } else {
            System.out.println("You lost the revival game. Battle ends.");

            // Set HP to max for all Pokémon before saving
            for (Pokemon pokemon : player.getPokemonsForBattle()) {
                pokemon.setHpToMax();
            }

            List<Pokemon> revivedOpponentPokemons = getOpponentPokemons();
            score.calculateAndUpdateScores(player, revivedOpponentPokemons);
            // End the battle or handle accordingly
        }
    }

    // Method to prompt the player to catch a Pokémon
    public static void promptCatch() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Generate a random Pokéball
        int ballProbability = random.nextInt(4); // Randomly choose an index from 0 to 3
        Pokeball.setBallType(ballProbability); // Set the Pokéball type
        System.out.println("You've chosen a " + Pokeball.getBallType() + " to catch a Pokémon!");

        // Attempt to catch the first opponent Pokémon
        Pokemon firstOpponent = opponentPokemons.get(0);
        System.out.println("Attempting to catch " + firstOpponent.getName() + "...");
        Pokeball.catchRate(); // Set the the ball's catch rate
        if (Pokeball.getBallCatchRate() > random.nextDouble(1)) { // Generate a random double value from 0 to 1
            System.out.println("Congratulations! You caught " + firstOpponent.getName() + "!");
            player.addPokemonToCaughtPokemons(firstOpponent);
            firstOpponent.setHpToMax();
            playerDatabase.saveToFile(); // save caught pokemon to the player's database

            if (!opponentPokemons.isEmpty()) {
                System.out.println("Do you want to catch the remaining Pokémon? (Y/N):");
                String choice;
                do {
                    choice = scanner.nextLine();
                } while (!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("N"));

                if (choice.equalsIgnoreCase("Y")) {
                    // Try to catch the remaining Pokémon
                    Pokemon secondOpponent = opponentPokemons.get(1);
                    System.out.println("Attempting to catch " + secondOpponent.getName() + "...");
                    Pokeball.catchRate(); // Perform the catch attempt

                    if (Pokeball.getBallCatchRate() > random.nextDouble(1)) { // Generate a random double value from 0
                                                                              // to 1
                        System.out.println("Congratulations! You caught " + secondOpponent.getName() + "!");
                        opponentPokemons.remove(0); // Remove the caught Pokémon from the list
                        score.calculateAndUpdateScores(player, opponentPokemons);
                        player.addPokemonToCaughtPokemons(secondOpponent);
                        secondOpponent.setHpToMax();
                        playerDatabase.saveToFile(); // save caught pokemon to the player's database
                    } else {
                        System.out.println("Oh no! The Pokémon broke free!");
                        score.calculateAndUpdateScores(player, opponentPokemons);
                    }
                } else {
                    System.out.println("You chose not to catch the remaining Pokémon.");
                    score.calculateAndUpdateScores(player, opponentPokemons);
                }
            }
        } else {
            System.out.println("Oh no! The Pokémon broke free!");
            score.calculateAndUpdateScores(player, opponentPokemons);
        }

    }

    // Method to generate two randome Pokémon
    private List<Pokemon> generateTwoRandomPokemons() {
        List<Pokemon> randomPokemons = new ArrayList<Pokemon>();
        for (int i = 0; i < 2; i++) {
            randomPokemons.add(Pokemon.randomPokemon());
        }
        return randomPokemons;
    }

    // Method to put text in the frame
    private static void frameText(String text) {
        System.out.println();
        System.out.println("***** " + text + " *****");
        System.out.println();
    }
}
