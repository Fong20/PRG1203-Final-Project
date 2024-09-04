import java.util.ArrayList;
import java.util.List;

//Controller class Game
public class Game {

	public void runGame() {
		// Create player database object
		PlayerDatabase playerDatabase = new PlayerDatabase();

		// Create player object
		Player player = playerDatabase.findPlayerDatabase();

		// Create score object
		Score score = new Score(playerDatabase);

		// Create a list of wild Pokemons
		List<Pokemon> wildPokemons = new ArrayList<>();
		wildPokemons.add(Pokemon.randomPokemon());
		wildPokemons.add(Pokemon.randomPokemon());
		wildPokemons.add(Pokemon.randomPokemon());

		// Main menu
		System.out.println("Welcome to the Pokémon Ga-Olé!");

		String art = "                                   ,'\\\n" +
				"    _.----.        ____         ,'  _\\   ___    ___     ____\n" +
				"_,-'       `.     |    |  /`.   \\,-'    |   \\  /   |   |    \\  |`.\n" +
				"\\      __    \\    '-.  | /   `.  ___    |    \\/    |   '-.   \\ |  |\n" +
				" \\.    \\ \\   |  __  |  |/    ,','_  `.  |          | __  |    \\|  |\n" +
				"   \\    \\/   /,' _`.|      ,' / / / /   |          ,' _`.|     |  |\n" +
				"    \\     ,-'/  /   \\    ,'   | \\/ / ,`.|         /  /   \\  |     |\n" +
				"     \\    \\ |   \\_/  |   `-.  \\    `'  /|  |    ||   \\_/  | |\\    |\n" +
				"      \\    \\ \\      /       `-.`.___,-' |  |\\  /| \\      /  | |   |\n" +
				"       \\    \\ `.__,'|  |`-._    `|      |__| \\/ |  `.__,'|  | |   |\n" +
				"        \\_.-'       |__|    `-._ |              '-.|     '-.| |   |\n" +
				"                                `'                            '-._|";

		System.out.println(art);

		// Start the PokemonRace game
		// Create a battle instance which accepts the player, wildPokemon, player
		// database, and score objects as arguments
		Battle battle = new Battle(player, wildPokemons, playerDatabase, score);

		battle.catchTime();
		battle.callPokemon();

		// Start the battle
		battle.startBattle();
	}

}
