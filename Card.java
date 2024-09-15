import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Card
{
    private int cardX, cardY, size_factor;
    private BufferedImage bufferedImage;
    private Image image;
    private String card_path;
    public Card(String in_path, int in_x, int in_y, int scaler) // Constructor
    {
        this.card_path = in_path;
        this.cardX = in_x;
        this.cardY = in_y;
        this.size_factor = scaler;

        try {
            bufferedImage = ImageIO.read(new File("src/Deck/"+this.card_path+".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString()
    {
        String card_name = "";

        switch (this.card_path.charAt(1))
        {
            case '0':
                switch (this.card_path.charAt(2))
                {
                    case '1':
                        card_name += "1 ";
                        break;
                    case '2':
                        card_name += "2 ";
                        break;
                    case '3':
                        card_name += "3 ";
                        break;
                    case '4':
                        card_name += "4 ";
                        break;
                    case '5':
                        card_name += "5 ";
                        break;
                    case '6':
                        card_name += "6 ";
                        break;
                    case '7':
                        card_name += "7 ";
                        break;
                    case '8':
                        card_name += "8 ";
                        break;
                    case '9':
                        card_name += "9 ";
                        break;
                    default:
                        card_name += "Unknown ";
                        break;
                }
                break;
            case '1':
                switch (this.card_path.charAt(2))
                {
                    case '0':
                        card_name += "10 ";
                        break;
                    case '1':
                        card_name += "Ace ";
                        break;
                    case '2':
                        card_name += "Jack ";
                        break;
                    case '3':
                        card_name += "Queen ";
                        break;
                    case '4':
                        card_name += "King ";
                        break;
                    default:
                        card_name += "Unknown ";
                        break;
                }
                break;
            default:
                card_name += "Unknown ";
                break;
        }
        switch (this.card_path.charAt(0))
        {
            case '1':
                card_name += "of Hearts";
                break;
            case '2':
                card_name += "of Diamonds";
                break;
            case '3':
                card_name += "of Clubs";
                break;
            case '4':
                card_name += "of Spades";
                break;
            default:
                card_name += "of Unknown";
                break;
        }
        return card_name;
    }

    // Getters
    public int getCardX() { return cardX; }
    public int getCardY() { return cardY; }
    public int getScaler() { return size_factor; }
    public String getPath() { return card_path; }

    // Setters
    public void setCardX(int new_cardX) { this.cardX = new_cardX; }
    public void setCardY(int new_cardY) { this.cardY = new_cardY; }
    public void setScaler(int new_size_factor) { this.size_factor = new_size_factor; }
    public void setPath(String new_card_path) { this.card_path = new_card_path; }

    public int getCardNumber() { // Takes the 2nd and 3rd chars of a path and creates an appropiate integer
        int second_decimal = Character.getNumericValue(this.card_path.charAt(1));
        int first_decimal =  Character.getNumericValue(this.card_path.charAt(2));
        return ((second_decimal*10)+first_decimal);
    }
    public boolean covers(int userX, int userY)
    { // Gets an input of X and Y coordinates
        return ((this.cardX-this.image.getWidth(null)/2<=userX &&
                userX<=this.cardX+this.image.getWidth(null)/2) &&
                (this.cardY-this.image.getHeight(null)/2<=userY &&
                        userY<=this.cardY+this.image.getHeight(null)/2));
    }

    public void paint(Graphics g)
    { // Paints the card
        this.image = bufferedImage.getScaledInstance(bufferedImage.getWidth()/-this.size_factor,
                bufferedImage.getHeight()/-this.size_factor, Image.SCALE_SMOOTH);
        int x = this.cardX-this.image.getWidth(null)/2;
        int y = this.cardY-this.image.getHeight(null)/2;
        g.drawImage(this.image, x,
                y, null);
    }

    public int xToRightOf() {
        this.image = bufferedImage.getScaledInstance(bufferedImage.getWidth()/-this.size_factor,
                bufferedImage.getHeight()/-this.size_factor, Image.SCALE_SMOOTH);
        return cardX + this.image.getWidth(null)+20;

    }

} // Class Card ends

