import java.util.Random;

public class Pokemon {

    // Declare attributes
    protected String name;
    protected int hp;
    protected String type;
    protected int attackPower;
    private int maxHp;
    // Pokemon database for name, hp, type, attack power
    private static final String[][] pokemonData = {
            { "Snorlax", "160", "NORMAL", "110" },
            { "Tauros", "75", "NORMAL", "100" },
            { "Porygon", "65", "NORMAL", "60" },
            { "Hariyama", "144", "FIGHTING", "120" },
            { "Throh", "120", "FIGHTING", "100" },
            { "Machamp", "90", "FIGHTING", "130" },
            { "Muk", "105", "POISON", "105" },
            { "Swalot", "100", "POISON", "73" },
            { "Garbodor", "80", "POISON", "95" },
            { "Sandshrew", "50", "GROUND", "75" },
            { "Sandslash", "75", "GROUND", "100" },
            { "Dugtrio", "35", "GROUND", "100" },
            { "Rookidee", "38", "FLYING", "47" },
            { "Corvisquire", "68", "FLYING", "67" },
            { "Doduo", "35", "FLYING", "85" }
    };

    // Declare constructor
    // Default constructor
    public Pokemon() {
    }

    public Pokemon(String name, int hp, String type, int attackPower) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp; // Set the max HP to the initial HP
        this.type = type;
        this.attackPower = attackPower;
    }

    public double calculateEffectiveness(Pokemon opponent) {
        return 1.0; // Default effectiveness
    }

    // Function to generate random pokemon and attach to subclasses
    public static Pokemon randomPokemon() {
        Random random = new Random();
        int randomIndex = random.nextInt(pokemonData.length);
        String[] data = pokemonData[randomIndex];
        String name = data[0];
        int hp = Integer.parseInt(data[1]);
        String type = data[2];
        int attackPower = Integer.parseInt(data[3]);

        if (type.equals("NORMAL")) {
            return new NormalPokemon(name, hp, type, attackPower);
        } else if (type.equals("FIGHTING")) {
            return new FightingPokemon(name, hp, type, attackPower);
        } else if (type.equals("POISON")) {
            return new PoisonPokemon(name, hp, type, attackPower);
        } else if (type.equals("GROUND")) {
            return new GroundPokemon(name, hp, type, attackPower);
        } else if (type.equals("FLYING")) {
            return new FlyingPokemon(name, hp, type, attackPower);
        } else {
            // Default case, create a normal Pokemon
            return new Pokemon(name, hp, type, attackPower) {
                public double calculateEffectiveness() {
                    return 1.0;
                }
            };
        }
    }

    // Method to perform an attack towards opponent
    public void attack(Pokemon opponent, double effectiveness) {
        System.out.println(this.name + " ⚔️ " + opponent.name + "!");
        int damage = (int) (attackPower * effectiveness);
        opponent.takeDamage(damage);
    }

    // Method to reduce HP based on incoming damage
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(this.name + " takes " + damage + " damage! Remaining HP: " + this.hp);
    }

    // Method to restore the Pokémon's HP to its maximum value
    public void revive() {
        this.hp = this.maxHp;
        System.out.println(this.name + " has been revived! HP is now: " + this.hp);
    }

    // Setter and getters
    public String getName() {
        return this.name;
    }

    public int getHp() {
        return Math.max(0, hp);
    }

    public String getType() {
        return this.type;
    }

    public String getDefenderType() {
        return this.type;
    }

    public int getAttackPower() {
        return this.attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setHpToMax() {
        this.hp = this.maxHp;
    }

    public boolean isDefeated() {
        return this.hp <= 0;
    }

    public double calculateEffectiveness(double effectiveness) {
        return 1.0;
    }
}
