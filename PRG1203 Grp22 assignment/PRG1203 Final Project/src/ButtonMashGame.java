import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ButtonMashGame {
    private JFrame frame;
    private JTextArea asciiTextArea;
    private JButton button;
    private JLabel countdownLabel;
    private Timer timer;
    private int count;
    private int timeRemaining;

    public ButtonMashGame() {
        frame = new JFrame("Button Mash Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // ASCII art of Pikachu (multi-line string)
        String pikachuAscii = "⢀⣠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠀⠀⠀⠀⣠⣤⣶⣶\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠀⠀⠀⢰⣿⣿⣿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⣀⣀⣾⣿⣿⣿⣿\n" +
                "⣿⣿⣿⣿⣿⡏⠉⠛⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⠀⠀⠀⠈⠛⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠛⠉⠀⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣧⡀⠀⠀⠀⠀⠙⠿⠿⠿⠻⠿⠿⠟⠿⠛⠉⠀⠀⠀⠀⣸⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣷⣄⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⠏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⣴⣿⣿⣿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⡟⠀⠀⢰⣹⡆⠀⠀⠀⠀⠀⠀⣭⣷⠀⠀⠀⠸⣿⣿⣿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⠃⠀⠀⠈⠉⠀⠀⠤⠄⠀⠀⠀⠉⠁⠀⠀⠀⠀⢿⣿⣿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⢾⣿⣷⠀⠀⠀⠀⡠⠤⢄⠀⠀⠀⠠⣿⣿⣷⠀⢸⣿⣿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⡀⠉⠀⠀⠀⠀⠀⢄⠀⢀⠀⠀⠀⠀⠉⠉⠁⠀⠀⣿⣿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⣧⠀⠀⠀⠀⠀⠀⠀⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢹⣿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿";

        // Set up the text area to display Pikachu ASCII art
        asciiTextArea = new JTextArea(pikachuAscii);
        asciiTextArea.setEditable(false);
        asciiTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        // Label to display instruction
        JLabel label = new JLabel("Mash the button as fast as you can to charge the spirit!");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        // Label to display the countdown timer
        countdownLabel = new JLabel("Time remaining: 10 seconds");
        countdownLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Set up the main panel with a BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(asciiTextArea), BorderLayout.CENTER);
        panel.add(countdownLabel, BorderLayout.SOUTH);

        // Create a red JButton for mashing
        button = new JButton("Mash me!");
        button.setBackground(Color.decode("#FF0000")); // Use Color.decode to set the color
        button.setForeground(Color.decode("#FFFFFF")); // Use Color.decode to set the color
        button.setFocusPainted(false); // Remove the focus border
        button.setPreferredSize(new Dimension(100, 50));
        button.setFont(new Font("Arial", Font.BOLD, 14));

        // Add ActionListener to the button for counting mashes
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count++; // Increment the mash count
                button.setText("Mash count: " + count); // Update button text with the mash count
                if (count >= 50) { // Adjust the count threshold as needed
                    timer.stop(); // Stop the timer
                    double spiritCharge = (double) count / 25.0 * 100.0; // Calculate spirit charge percentage
                    String message = String.format("Spirit charged: %.2f%%", spiritCharge); // Format the message
                    JOptionPane.showMessageDialog(frame, message, "Charging Complete", JOptionPane.INFORMATION_MESSAGE); // Show
                                                                                                                         // message
                                                                                                                         // dialog
                    frame.dispose(); // Close the frame
                }
            }
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.add(button, BorderLayout.SOUTH);

        frame.setVisible(true); // Make the frame visible

        // Initialize the countdown timer
        timeRemaining = 10;
        timer = new Timer(1000, new ActionListener() { // 1 second timer
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--; // Decrement the time remaining
                countdownLabel.setText("Time remaining: " + timeRemaining + " seconds");
                if (timeRemaining <= 0) { // Check if time is up
                    timer.stop(); // Stop the timer
                    if (count == 0) { // Check if no mashes are made
                        JOptionPane.showMessageDialog(frame, "You failed to charge the spirit!", "Game Over",
                                JOptionPane.ERROR_MESSAGE); // Show game over dialog
                        resetGame(); // Reset the game
                    } else {
                        double spiritCharge = (double) count / 25.0 * 100.0; // Calculate spirit charge percentage
                        String message = String.format("Spirit charged: %.2f%%", spiritCharge); // Format the message
                        JOptionPane.showMessageDialog(frame, message, "Time's Up!", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose(); // Close the frame on completion
                    }
                }
            }
        });
        timer.start();
    }

    // Reset the game if the spirit is not charged
    private void resetGame() {
        count = 0;
        button.setText("Mash me!");
        timeRemaining = 10;
        countdownLabel.setText("Time remaining: 10 seconds");
        timer.restart();
    }

    // Method to start the game
    public static void startButtonMashGame() {
        new ButtonMashGame();
    }

}
