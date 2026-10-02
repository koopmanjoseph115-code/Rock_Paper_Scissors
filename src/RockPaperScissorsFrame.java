import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.Image;


public class RockPaperScissorsFrame extends JFrame {

    private JButton rockButton;
    private JButton paperButton;
    private JButton scissorsButton;
    private JButton quitButton;


    private JTextField playerWinsField;
    private JTextField computerWinsField;
    private JTextField tiesField;


    private JTextArea resultsArea;
    private JScrollPane scrollPane;


    private int playerWins = 0;
    private int computerWins = 0;
    private int tiesCount = 0;


    private int playerRockCount = 0;
    private int playerPaperCount = 0;
    private int playerScissorsCount = 0;
    private String lastPlayerMove = "";


    private final Random randomEngine = new Random();

    public RockPaperScissorsFrame() {

        setTitle("Rock Paper Scissors Game");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 10, 0));
        buttonPanel.setBorder(new TitledBorder("Select your Gesture"));


        int imgWidth = 50;
        int imgHeight = 50;

        // Load and scale Rock icon
        ImageIcon rockIcon = new ImageIcon(getClass().getResource("/rock.png"));
        Image scaledRock = rockIcon.getImage().getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
        rockButton = new JButton("Rock", new ImageIcon(scaledRock));

        // Load and scale Paper icon
        ImageIcon paperIcon = new ImageIcon(getClass().getResource("/paper.png"));
        Image scaledPaper = paperIcon.getImage().getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
        paperButton = new JButton("Paper", new ImageIcon(scaledPaper));

        // Load and scale Scissors icon
        ImageIcon scissorsIcon = new ImageIcon(getClass().getResource("/scissors.png"));
        Image scaledScissors = scissorsIcon.getImage().getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
        scissorsButton = new JButton("Scissors", new ImageIcon(scaledScissors));

        quitButton = new JButton("Quit");


        GameListener gameListener = new GameListener();
        rockButton.addActionListener(gameListener);
        paperButton.addActionListener(gameListener);
        scissorsButton.addActionListener(gameListener);

        quitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Final Session Results Displayed! Exiting...");
            System.exit(0);
        });

        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        buttonPanel.add(quitButton);


        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        statsPanel.setBorder(new TitledBorder("Current Session Standings"));

        playerWinsField = new JTextField("0", 5);
        playerWinsField.setEditable(false);
        computerWinsField = new JTextField("0", 5);
        computerWinsField.setEditable(false);
        tiesField = new JTextField("0", 5);
        tiesField.setEditable(false);

        statsPanel.add(new JLabel("Player Wins:"));
        statsPanel.add(playerWinsField);
        statsPanel.add(new JLabel("Computer Wins:"));
        statsPanel.add(computerWinsField);
        statsPanel.add(new JLabel("Ties:"));
        statsPanel.add(tiesField);


        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BorderLayout());
        resultsPanel.setBorder(new TitledBorder("Battle Logs"));

        resultsArea = new JTextArea(10, 45);
        resultsArea.setEditable(false);
        scrollPane = new JScrollPane(resultsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);


        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.add(statsPanel, BorderLayout.NORTH);
        centerContainer.add(resultsPanel, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.NORTH);
        add(centerContainer, BorderLayout.CENTER);
    }


    private class GameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String playerMove = "";

            if (e.getSource() == rockButton) {
                playerMove = "R";
                playerRockCount++;
            } else if (e.getSource() == paperButton) {
                playerMove = "P";
                playerPaperCount++;
            } else if (e.getSource() == scissorsButton) {
                playerMove = "S";
                playerScissorsCount++;
            }


            int roll = randomEngine.nextInt(100) + 1;
            Strategy activeStrategy;
            String strategyLabel;

            if (roll <= 10) {
                activeStrategy = new Cheat();
                strategyLabel = "Cheat";
            } else if (roll <= 30) {
                activeStrategy = new LeastUsed();
                strategyLabel = "Least Used";
            } else if (roll <= 50) {
                activeStrategy = new MostUsed();
                strategyLabel = "Most Used";
            } else if (roll <= 70) {

                if (lastPlayerMove.isEmpty()) {
                    activeStrategy = new RandomStrategy();
                    strategyLabel = "Random (Last Used Fallback)";
                } else {
                    activeStrategy = new LastUsed();
                    strategyLabel = "Last Used";
                }
            } else {
                activeStrategy = new RandomStrategy();
                strategyLabel = "Random";
            }

            String computerMove = activeStrategy.getMove(playerMove);
            processMatchOutcome(playerMove, computerMove, strategyLabel);


            lastPlayerMove = playerMove;
        }
    }


    private void processMatchOutcome(String pMove, String cMove, String strategyName) {
        String resultLine = "";
        String winner = "";


        if (pMove.equals(cMove)) {
            winner = "Tie";
            tiesCount++;
            if (pMove.equals("R")) resultLine = "Rock ties Rock.";
            if (pMove.equals("P")) resultLine = "Paper ties Paper.";
            if (pMove.equals("S")) resultLine = "Scissors ties Scissors.";
        } else if ((pMove.equals("R") && cMove.equals("S")) ||
                (pMove.equals("P") && cMove.equals("R")) ||
                (pMove.equals("S") && cMove.equals("P"))) {
            winner = "Player";
            playerWins++;
            if (pMove.equals("R")) resultLine = "Rock breaks Scissors.";
            if (pMove.equals("P")) resultLine = "Paper covers Rock.";
            if (pMove.equals("S")) resultLine = "Scissors cuts Paper.";
        } else {
            winner = "Computer";
            computerWins++;
            if (cMove.equals("R")) resultLine = "Rock breaks Scissors.";
            if (cMove.equals("P")) resultLine = "Paper covers Rock.";
            if (cMove.equals("S")) resultLine = "Scissors cuts Paper.";
        }


        String outputMessage;
        if (winner.equals("Tie")) {
            outputMessage = String.format("%s (It's a Tie! Computer: %s)\n", resultLine, strategyName);
        } else {
            outputMessage = String.format("%s (%s wins! Computer: %s)\n", resultLine, winner, strategyName);
        }

        resultsArea.append(outputMessage);


        playerWinsField.setText(String.valueOf(playerWins));
        computerWinsField.setText(String.valueOf(computerWins));
        tiesField.setText(String.valueOf(tiesCount));
    }


    private class LeastUsed implements Strategy {
        @Override
        public String getMove(String playerMove) {
            if (playerRockCount <= playerPaperCount && playerRockCount <= playerScissorsCount) {
                return "P";
            } else if (playerPaperCount <= playerRockCount && playerPaperCount <= playerScissorsCount) {
                return "S";
            } else {
                return "R";
            }
        }
    } // Ends LeastUsed

    private class MostUsed implements Strategy {
        @Override
        public String getMove(String playerMove) {
            if (playerRockCount >= playerPaperCount && playerRockCount >= playerScissorsCount) {
                return "P";
            } else if (playerPaperCount >= playerRockCount && playerPaperCount >= playerScissorsCount) {
                return "S";
            } else {
                return "R";
            }
        }
    } // Ends MostUsed

    private class LastUsed implements Strategy {
        @Override
        public String getMove(String playerMove) {
            return lastPlayerMove;
        }
    }
}