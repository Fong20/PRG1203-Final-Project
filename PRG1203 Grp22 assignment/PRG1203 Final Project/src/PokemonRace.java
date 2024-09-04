import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Scanner;

public class PokemonRace {
    private JFrame frame; // Main frame of the application
    private JPanel racePanel; // Panel to display the race
    private JLabel allyPokemon1; // Label for the first ally Pokémon
    private JLabel allyPokemon2; // Label for the second ally Pokémon
    private JLabel opponentPokemon1; // Label for the first opponent Pokémon
    private JLabel opponentPokemon2; // Label for the second opponent Pokémon
    private Timer timer; // Timer for the race
    private int ally1Pos = 0; // Position of the first ally Pokémon
    private int ally2Pos = 0; // Position of the second ally Pokémon
    private int opponent1Pos = 0; // Position of the first opponent Pokémon
    private int opponent2Pos = 0; // Position of the second opponent Pokémon
    private static boolean allyWon = false; // Flag to check if ally won

    public PokemonRace() {
        frame = new JFrame("Pokemon Race");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());

        // Display instructions
        JOptionPane.showMessageDialog(frame, "Welcome to the Pokemon Race!\n\n"
                + "How to play:\n"
                + "1. Press the 'R' key repeatedly to move your ally Pokemon.\n"
                + "2. The first Pokemon to reach the end wins the race and gets to attack first!\n\n"
                + "Good luck!", "Instructions", JOptionPane.INFORMATION_MESSAGE);

        racePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.decode("#FFFFFF")); // Set background color using color code
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.decode("#61A75C"));
                g.fillRect(0, 90, getWidth(), 10);
                g.fillRect(0, 190, getWidth(), 10);
                g.fillRect(0, 290, getWidth(), 10);
            }
        };
        racePanel.setPreferredSize(new Dimension(800, 300));
        racePanel.setLayout(null);

        // Using color codes for ally and opponent Pokémon
        allyPokemon1 = createPokemonLabel("Ally 1", Color.decode("#F23020"), 0, 50);
        allyPokemon2 = createPokemonLabel("Ally 2", Color.decode("#F23020"), 0, 150);
        opponentPokemon1 = createPokemonLabel("Opponent 1", Color.decode("#60BAFF"), 0, 100);
        opponentPokemon2 = createPokemonLabel("Opponent 2", Color.decode("#60BAFF"), 0, 200);

        // Add Pokemon labels to the panel
        racePanel.add(allyPokemon1);
        racePanel.add(allyPokemon2);
        racePanel.add(opponentPokemon1);
        racePanel.add(opponentPokemon2);

        frame.add(racePanel, BorderLayout.CENTER);

        // Add key listener to move ally Pokémon when 'R' key is pressed
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    moveAllyPokemon();
                }
            }
        });

        // Timer to move opponent Pokémon periodically
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveOpponentPokemon();
                checkWinner();
            }
        });
        timer.start();

        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
    }

    // Method to create a label for a Pokémon
    private JLabel createPokemonLabel(String name, Color color, int x, int y) {
        JLabel label = new JLabel(name);
        label.setOpaque(true);
        label.setBackground(color);
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 80, 30);
        return label;
    }

    // Method to move ally Pokémon
    private void moveAllyPokemon() {
        ally1Pos += 10;
        ally2Pos += 10;
        allyPokemon1.setLocation(ally1Pos, allyPokemon1.getY());
        allyPokemon2.setLocation(ally2Pos, allyPokemon2.getY());
    }

    // Method to move opponent Pokêmon
    private void moveOpponentPokemon() {
        opponent1Pos += (int) (Math.random() * 10);
        opponent2Pos += (int) (Math.random() * 10);
        opponentPokemon1.setLocation(opponent1Pos, opponentPokemon1.getY());
        opponentPokemon2.setLocation(opponent2Pos, opponentPokemon2.getY());
    }

    // Method to check if a Pokémon has won the race
    private void checkWinner() {
        if (ally1Pos >= 700 || ally2Pos >= 700) {
            timer.stop();
            allyWon = true;
            JOptionPane.showMessageDialog(frame, "Ally Pokemon won! Your ally gets to attack first!", "Race Result",
                    JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        } else if (opponent1Pos >= 700 || opponent2Pos >= 700) {
            timer.stop();
            allyWon = false;
            JOptionPane.showMessageDialog(frame, "Opponent Pokemon won! The opponent gets to attack first!",
                    "Race Result", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        }

        // Pass the result to Battle class for handling
        Battle.handleBattle(allyWon);
    }

    // Static method to retrieve allyWon value
    public static boolean isAllyWon() {
        return allyWon;
    }

    // Method to start the Pokémon Race game
    public static void startPokemonRace() {
        new PokemonRace();
    }
}
