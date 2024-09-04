import java.util.Random;

public class Pokeball {
    // Declare attributes
    private static String ballType;
    private static double ballCatchRate;
    private final static String[] type = { "PokeBall", "GreatBall", "UltraBall", "MasterBall" };
    // private Pokemon pokemon;

    // Declare constructor
    // Default constructor
    public Pokeball() {
        Pokeball.ballType = "Pokeball";
        Pokeball.ballCatchRate = 0.28;
    }

    // Setter and getters
    public static void setBallType(int ballProbability) {

        switch (ballProbability) {

            // If the probability is 0, the obtained ball type is Pokeball
            case 0:
                Pokeball.ballType = type[ballProbability];

                // If the probability is 1, the obtained ball type is GreatBall
            case 1:
                Pokeball.ballType = type[ballProbability];
                break;

            // If the probability is 2, the obtained ball type is UltraBall
            case 2:
                Pokeball.ballType = type[ballProbability];
                break;

            // If the probability is 3, the obtained ball type is MasterBall
            case 3:
                Pokeball.ballType = type[ballProbability];
                break;
        }
    }

    // Getter to get the type of ball
    public static String getBallType() {
        return ballType;
    }

    // Setter which is used to set the ball's catch rate
    public static void setBallCatchRate(double ballCatchRate) {
        Pokeball.ballCatchRate = ballCatchRate;

    }

    // Getter which is used to get the ball's catch rate
    public static double getBallCatchRate() {
        return ballCatchRate;
    }

    // Method which is used to set the ball catch rate based on the chosen ball type
    public static void catchRate() {

        // If the obtained type of ball is Pokeball, set the ball catch rate to 0.28
        if (Pokeball.getBallType().equals("PokeBall")) {
            Pokeball.setBallCatchRate(0.28);
        }

        // If the obtained type of ball is GreatBall, set the ball catch rate to 0.41
        else if (Pokeball.getBallType().equals("GreatBall")) {
            Pokeball.setBallCatchRate(0.41);
        }
        // If the obtained type of ball is UltraBall, set the ball catch rate to 0.62
        else if (Pokeball.getBallType().equals("UltraBall")) {
            Pokeball.setBallCatchRate(0.62);
        }

        // If the obtained type of ball is MasterBall, set the ball catch rate to 1
        // which guarantees that the pokemon will be caught
        else if (Pokeball.getBallType().equals("MasterBall")) {
            Pokeball.setBallCatchRate(1);
        }
    }

}
