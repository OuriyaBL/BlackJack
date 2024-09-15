import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.Timer;
import javax.swing.BoxLayout;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;


public class UserInterface extends JFrame {

    UserInterface() throws Exception {
        GameMath gameMath = new GameMath();

        new GameWindow(gameMath);  // Loads the game window
        gameMath.startGame();  // Starts the game (deals out 2 cards to both dealer and player
        new Sound();  // Starts game music
    }
}

class GameWindow extends JFrame {

    GameWindow(GameMath gameMath) {
        CardPanel gamePanel = new CardPanel(gameMath);
        ControlPanel controlPanel = new ControlPanel(gameMath);

        // Setting properties of the game window
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Setting layout of the JFrame
        this.setLayout(new BorderLayout());
        this.add(gamePanel, BorderLayout.CENTER);
        this.add(controlPanel, BorderLayout.EAST);

        this.setVisible(true);
    }
}

// Panel containing all the player controls for the game
class ControlPanel extends JPanel {

    Color buttonColor = Color.RED.darker().darker().darker();

    ControlPanel(GameMath gameMath) {
        int spacerBetweenButtons = 20;  // Amount of space between buttons
        Dimension buttonSize = new Dimension(100, 50);
        Font buttonFont = new Font("Helvetica", Font.PLAIN, 25);

        // Pushes buttons to the center from the top
        this.add(Box.createVerticalStrut(spacerBetweenButtons));
        this.add(Box.createVerticalGlue());

        this.setBackground(new Color(27, 85, 48));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create hit button
        this.add(Box.createVerticalStrut(spacerBetweenButtons));
        JButton hitButton = new JButton("Hit");
        hitButton.setPreferredSize(buttonSize);
        hitButton.setMaximumSize(buttonSize);
        hitButton.setFont(buttonFont);
        hitButton.addActionListener(e -> gameMath.hitButtonPressed());
        hitButton.setBackground(buttonColor);
        this.add(hitButton);

        // Create stand button
        this.add(Box.createVerticalStrut(spacerBetweenButtons));
        JButton standButton = new JButton("Stand");
        standButton.setPreferredSize(buttonSize);
        standButton.setMaximumSize(buttonSize);
        standButton.setFont(buttonFont);
        standButton.addActionListener(e -> gameMath.standButtonPressed());
        standButton.setBackground(buttonColor);
        this.add(standButton);

        // Create quit button
        this.add(Box.createVerticalStrut(spacerBetweenButtons));
        JButton quitButton = new JButton("Quit");
        quitButton.setPreferredSize(buttonSize);
        quitButton.setMaximumSize(buttonSize);
        quitButton.setFont(buttonFont);
        quitButton.addActionListener(e -> closeGame());
        quitButton.setBackground(buttonColor);
        this.add(quitButton);

        // Create restart button
        this.add(Box.createVerticalStrut(spacerBetweenButtons));
        JButton restartButton = new JButton("Restart");
        restartButton.setPreferredSize(buttonSize);
        restartButton.setMaximumSize(buttonSize);
        restartButton.setFont(buttonFont);
        restartButton.addActionListener(e -> gameMath.reset());
        restartButton.setBackground(buttonColor);
        this.add(restartButton);

        // Pushes buttons to center of the panel from the bottom
        this.add(Box.createVerticalStrut(spacerBetweenButtons));
        this.add(Box.createVerticalGlue());
    }

    private void closeGame() {
        System.exit(0);
    }
}

class CardPanel extends JPanel {

    int widthOfCardPanel = 640;
    int heightOfCardPanel = 360;
    int headerXCoordinate = 100;
    int headerYCoordinate = 50;
    Color headerFontColor = Color.GREEN.darker();
    Color backgroundColor = new Color(0,0,0,0);
    Font headerFont = new Font("Helvetica", Font.BOLD, 25);
    private final GameMath gameMath;

    CardPanel(GameMath gameMath) {
        DealerPanel dealerPanel = new DealerPanel(gameMath);
        PlayerPanel playerPanel = new PlayerPanel(gameMath);

        this.gameMath = gameMath;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(dealerPanel);
        this.add(playerPanel);

        // Updates UI every 100 ms
        Timer paintTimer = new Timer(100, e -> {
            dealerPanel.repaint();
            playerPanel.repaint();
            repaint();
        });

        paintTimer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws the background
        BackgroundImage backgroundImage = new BackgroundImage();
        g.drawImage(backgroundImage.getImage(g), 0, 0, getWidth(), getHeight(), this);
    }

    // Draws Players side of the screen (Contains players score counter and hand)
    class DealerPanel extends JPanel {

        GameMath gameMath;

        public DealerPanel(GameMath gameMath) {
            this.gameMath = gameMath;
            this.setPreferredSize(new Dimension(widthOfCardPanel,heightOfCardPanel));
            backgroundColor = gameMath.getBackGround();
            this.setBackground(backgroundColor);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            String dealerHeaderText = getDealerHeaderText();

            // Header for dealer's hand
            g.setColor(headerFontColor);
            g.setFont(headerFont);
            g.drawString(dealerHeaderText, headerXCoordinate, headerYCoordinate);
            setBackground(gameMath.getBackGround());

            // Second array list takes the elements in the hand array list in GameMath class
            // Fixes error when iterating upon an array that is constantly changing
            List <Card> dealerHand = new ArrayList<>(gameMath.getDealerHand());

            for (Card card : dealerHand) {
                card.paint(g);
            }
        }

        public String getDealerHeaderText() {
            return "Dealer's hand: " + gameMath.getTotal(gameMath.getDealerHand());
        }
    }

    // Draws Players side of the screen (Contains players score counter and hand)
    class PlayerPanel extends JPanel {

        GameMath gameMath;

        PlayerPanel(GameMath gameMath) {
            this.gameMath = gameMath;
            this.setPreferredSize(new Dimension(widthOfCardPanel,heightOfCardPanel));
            backgroundColor = gameMath.getBackGround();
            this.setBackground(backgroundColor);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            String playerHeaderText = getPlayerHeaderText();

            // Header for Player
            g.setColor(headerFontColor);
            g.setFont(headerFont);
            g.drawString(playerHeaderText, headerXCoordinate, headerYCoordinate);
            setBackground(gameMath.getBackGround());

            // Second array list takes the elements in the hand array list in GameMath class
            // Fixes error when iterating upon an array that is constantly changing
            List <Card> playerHand = new ArrayList<>(gameMath.getPlayerHand());

            for (Card card : playerHand) {
                card.paint(g);
            }
        }
        
        public String getPlayerHeaderText() {
            return "Player's hand: " + gameMath.getTotal(gameMath.getPlayerHand());
        }
    }
}