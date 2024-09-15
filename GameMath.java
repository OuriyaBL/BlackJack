import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class GameMath
{
    // 1. Two arrays (player and dealer)
    private ArrayList<Card> playerHand = new ArrayList<>(21);
    private ArrayList<Card> dealerHand = new ArrayList<>(21);

    // 2. RNG Card from Deck
    private int rnd_suite, rnd_card_number;
    Random random = new Random();

    // 6. Draw card from deck mechanics
    private final int intialX = 150;
    private final int intialY = 150;
    private final int scaler = -5;

    private boolean blackJack = false;
    boolean isHitting = false;
    boolean isStanding = false;

    private final Timer gameTimer;

    private Color color;

    // 3. Card to Path translator
    // 4. Card to value translator
    // 4.1 Ace mechanic
    // 5. Hand total tracking
    // 6. Draw card from deck mechanics
    // 7. Win conditions
    // 8. Lose conditions
    // 9. Draw conditions
    // 10. End (reset) method

    public GameMath() {

        gameTimer = new Timer(100, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                playGame();
            }

        });
    }

    // 2. RANDOM CARD NUMBER GENERATORS
    public int generateSuite()
    { // Generates a Suite number (random from 1-4)
        // Index: 1=Heart, 2=Diamond, 3=Club, 4=Spades
        return rnd_suite = random.nextInt(1,4);
    }
    public int generateCardNumber()
    { // Generates a card number (random from 2-14)
        // Index: 2=2, 3=3, ..., 10=10, 11=Ace, 12=Jack, 13=Queen, 14=King
        return rnd_suite = random.nextInt(2,14);
    }



    // 3. RANDOM CARD TO FILE PATH TRANSLATOR
    public String cardToPathTranslator(int Suite, int Number) {
        if (Number < 10) {
            return ""+Suite+"0"+Number;
        } else {
            return ""+Suite+Number;
        }
    }



    // 4. RANDOM CARD TO NUMBER TRANSLATOR
    public int aceValue(int handTotal)
    { // Inputs a hand value (total point 0<hand<21) and returns appropriate value of ace
        if (handTotal > 11)
        { // If adding 10 to the player would make them lose (11 < hand < 21) make ace value "1"
            return 1;
        } else { // Otherwise the ace value is "11"
            return 11;
        }
    }
    public int cardToValueTranslator(int cardNumber, int handTotal)
    { // Translates card draw to value (input hands to use aceValue method)
        if (cardNumber < 11)
        {
            return cardNumber; // cardNumber is within range of 2 to 10, no special value
        }
        else if (cardNumber > 11)
        {
            return 10; // Jack (12), Queen (13) and King (14) should all add 10 points to the counter
        }
        else
        { // cardNumber = 11
            return aceValue(handTotal); // Activates ace mechanic
        }
    }

    // 5. HAND TOTAL TRACKING
    public int getTotal(ArrayList<Card> hand)
    { // Checks the total of a hand
        int total = 0;

        for (Card card : hand) // Go through all the cards in the hand
        {
            // For each card call the "get number" method which returns the "number" of the card (not path)
            // Translate to values based on the provided number and the total thus far
            total += cardToValueTranslator(card.getCardNumber(), total);
        }
        return total;
    }

    // 6. DRAW CARD FROM DECK MECHANICS
    public void drawForHand(ArrayList<Card> hand)
    {

        // Generates Card numerical values
        int thisSuite = generateSuite();
        int thisCardNumber = generateCardNumber();

        // Generates x,y values for card (based on previous cards)
        int thisX = intialX;

        if (hand != null && !hand.isEmpty()) // Moves card to the right of the last card
        {
            Card previousCard = hand.get(hand.size()-1);
            thisX = previousCard.xToRightOf();
        }
        // Creates new card with the generated numerical values and coordinates

        hand.add(new Card(cardToPathTranslator(thisSuite,thisCardNumber), thisX, intialY, scaler));
    }

    public void dealerTurn() {
        while (getTotal(dealerHand) < 17 && !blackJack) {
            drawForHand(dealerHand);
        }
        determineGameOutcome();
    }

    public void startGame() {
        color = new Color(171, 10, 4, 0);
        drawForHand(playerHand);
        drawForHand(dealerHand);
        drawForHand(playerHand);
        drawForHand(dealerHand);

        System.out.println("[" + getTotal(playerHand) + "] Player's hand is: " + playerHand);
        System.out.println("[?] Dealer's hand is: [" + dealerHand.get(0) + ", ?]");

        if (getTotal(playerHand) == 21) {
            System.out.println("BLACKJACK!");
            blackJack = true;

        } else {
            blackJack = false;
        }
    }

    // Determines the outcome of the game.
    // Colors are assigned to game outcomes and the background is changed to display the color when outcome is shown.
    // Depending on the outcome specific messages pop up on the screen informing the user.
    public void determineGameOutcome() {

        int playerTotal = getTotal(getPlayerHand());
        int dealerTotal = getTotal(getDealerHand());

        Color winColor = new Color(7, 191, 29, 128);
        Color lossColor = new Color(255, 10, 1, 128);
        Color drawColor = new Color(207, 175, 28, 128);

        if (blackJack) {
            System.out.println("You won!");
            color = winColor;
            showMessage("Congratulations, you won!", "Match Won");

        } else if (playerTotal > 21) {
            System.out.println("You lost."); // Player bust
            color = lossColor;
            showMessage("You lost, better luck next time!", "Match Lost");

        } else {

            if (dealerTotal > 21) {
                System.out.println("You won!"); // Dealer bust
                color = winColor;
                showMessage("Congratulations, you won!", "Match Won");

            } else if (playerTotal == dealerTotal && playerTotal != 0) {
                System.out.println("Draw"); // Draw
                color = drawColor;
                showMessage("You and dealer had a draw!", "Match Draw");

            } else {
                if ((21 - playerTotal) < (21 - dealerTotal)) {
                    System.out.println("You won!"); // Closer win
                    color = winColor;
                    showMessage("Congratulations, you won!", "Match Won");

                } else {
                    System.out.println("You lost."); // Further lost
                    color = lossColor;
                    showMessage("You lost, better luck next time!", "Match Lost");
                }
            }
        }
    }

    // Method which displays a pop-up message depending on the outcome of the game
    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        reset();
    }

    // The actions (whether hitting or standing) determines how the game will progress.
    public void playGame() {

        if (isHitting) {
            drawForHand(playerHand);
            System.out.println("[" + getTotal(playerHand) + "] Player's hand is: " + playerHand);
            if (getTotal(getPlayerHand()) >= 21) {
                dealerTurn();
                gameTimer.stop();
            }
            isHitting = false;

        } else if (isStanding) {
            dealerTurn();
            isStanding = false;
            System.out.println("[" + getTotal(playerHand) + "] Player's hand is: " + playerHand);
            gameTimer.stop();
        }

    }

    // Getter for the cards held by the dealer
    public ArrayList<Card> getDealerHand() {
        return dealerHand;
    }

    // Getter for the cards held by the player
    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }

    // Initialized when the hit button is pressed
    public void hitButtonPressed() {
        isHitting = true;
        gameTimer.start();
    }

    // Initialized when the stand button is pressed
    public void standButtonPressed() {
        isStanding = true;
        gameTimer.start();
    }

    public Color getBackGround() {
        return color;
    }

    public void reset() {
        playerHand.clear();
        dealerHand.clear();
        startGame();
    }
}
