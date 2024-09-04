import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Scanner;

public class ReviveGame extends JFrame {
    private static final int WINDOW_WIDTH = 400; // Window width
    private static final int WINDOW_HEIGHT = 300; // Window height
    private static final int TARGET_SIZE = 60; // Diameter of the target
    private static final int MOVE_DELAY = 1000; // Delay between target movements in milliseconds
    private static final int ENERGY_BAR_WIDTH = 20; // Width of the energy bar
    private static final int ENERGY_BAR_HEIGHT = 200; // Height of the energy bar
    private static final int ENERGY_INCREMENT = 10; // Amount to increase energy by each click
    private static final int MAX_ENERGY = 100; // Maximum energy level
    private static final int REQUIRED_CLICKS = 8; // Number of successful clicks needed to win
    private static final int GAME_TIME_LIMIT = 30; // Game time limit in seconds

    private JPanel gamePanel;
    private RoundTargetPanel targetPanel;
    private JProgressBar energyBar;
    private JLabel timerLabel;
    private Random random;
    private int clickCount;
    private int energyLevel;
    private Timer timer;
    private Timer countdownTimer;
    private int remainingTime;
    private static boolean gameWon = false;

    public ReviveGame() {
        setTitle("Revive Your Pokémon!"); // Set window title
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen

        random = new Random();
        clickCount = 0; // Counter for successful clicks
        energyLevel = 0; // Initial energy level
        remainingTime = GAME_TIME_LIMIT; // Initial time remaining

        showInstructions(); // Display game instructions
    }

    // Display instructions to the player
    private void showInstructions() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Instructions:\n\n" +
                        "1. Click on the energy ball as many times as possible.\n" +
                        "2. Each successful click increases the energy bar.\n" +
                        "3. Collect as many energy as you can and fill the energy bar to win the game.\n" +
                        "4. The game is time-limited to " + GAME_TIME_LIMIT + " seconds.\n\n" +
                        "Good luck!",
                "Game Instructions",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE);

        if (response == JOptionPane.OK_OPTION) {
            startGame();
        } else {
            dispose(); // Close the window if the player cancels
        }
    }

    // Start the game
    private void startGame() {
        gamePanel = new JPanel(null);
        gamePanel.setBackground(Color.WHITE);

        targetPanel = new RoundTargetPanel();
        targetPanel.setBounds(getRandomBounds());
        targetPanel.setPreferredSize(new Dimension(TARGET_SIZE, TARGET_SIZE));

        targetPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick();
            }
        });

        // Energy bar setup
        energyBar = new JProgressBar(SwingConstants.VERTICAL, 0, MAX_ENERGY);
        energyBar.setValue(energyLevel);
        energyBar.setBounds(WINDOW_WIDTH - ENERGY_BAR_WIDTH - 10, 10, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT);
        energyBar.setForeground(Color.GREEN);
        energyBar.setBackground(Color.WHITE);
        energyBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Timer label setup
        timerLabel = new JLabel("Time left: " + remainingTime + "s", SwingConstants.CENTER);
        timerLabel.setBounds(10, 10, 150, 30);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));

        gamePanel.add(targetPanel);
        gamePanel.add(energyBar);
        gamePanel.add(timerLabel);
        add(gamePanel);

        // Move the target periodically
        timer = new Timer(MOVE_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clickCount < REQUIRED_CLICKS && energyLevel < MAX_ENERGY && remainingTime > 0) {
                    moveTarget();
                }
            }
        });
        timer.start();

        // Countdown timer setup
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingTime--;
                timerLabel.setText("Time left: " + remainingTime + "s");

                if (remainingTime <= 0) {
                    endGame();
                }
            }
        });
        countdownTimer.start();
    }

    // Get random bounds for target position
    private Rectangle getRandomBounds() {
        int x = random.nextInt(WINDOW_WIDTH - TARGET_SIZE - ENERGY_BAR_WIDTH - 20); // Leave space for the energy bar
        int y = random.nextInt(WINDOW_HEIGHT - TARGET_SIZE);
        return new Rectangle(x, y, TARGET_SIZE, TARGET_SIZE);
    }

    private void moveTarget() {
        if (clickCount < REQUIRED_CLICKS) {
            targetPanel.setBounds(getRandomBounds());
            gamePanel.repaint();
        }
    }

    // Handle click on target
    private void handleClick() {
        if (remainingTime > 0) {
            clickCount++;
            energyLevel += ENERGY_INCREMENT;
            if (energyLevel > MAX_ENERGY) {
                energyLevel = MAX_ENERGY;
            } // Cap energy at the maximum level
            energyBar.setValue(energyLevel);

            if (clickCount >= REQUIRED_CLICKS && energyLevel >= MAX_ENERGY) {
                endGame();
            } else {
                moveTarget();
            }
        }
    }

    // End the game
    private void endGame() {
        timer.stop(); // Stop the target movement timer
        countdownTimer.stop(); // Stop the countdown timer
        if (clickCount >= REQUIRED_CLICKS && energyLevel >= MAX_ENERGY) {
            gameWon = true;
            JOptionPane.showMessageDialog(this, "Game Over! "
                    + (energyLevel >= MAX_ENERGY ? "Congratulations! You've revived the Pokémon!" : "Time's up!"));
            dispose(); // Close the game window
        } else {
            gameWon = false; // Set game won flag
            JOptionPane.showMessageDialog(this, "Game Over! You didn't revive the Pokémon.");
        }

        Battle.continueBattleWithRevival(gameWon);
    }

    // Start the Revive Game
    public static void startReviveGame() {
        System.out.println("You have an opportunity to revive your Pokémon! Press Enter to continue...");

        Scanner scanner = new Scanner(System.in);

        scanner.nextLine(); // Wait for Enter key press

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ReviveGame game = new ReviveGame();
                game.setVisible(true);
            }
        });
    }

    // Custom JPanel to draw a round target
    private class RoundTargetPanel extends JPanel {
        private static final int TARGET_DIAMETER = TARGET_SIZE;

        public RoundTargetPanel() {
            setOpaque(false);
            setBounds(0, 0, TARGET_DIAMETER, TARGET_DIAMETER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(0, 0, TARGET_DIAMETER, TARGET_DIAMETER);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(0, 0, TARGET_DIAMETER, TARGET_DIAMETER);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(TARGET_DIAMETER, TARGET_DIAMETER);
        }
    }
}
