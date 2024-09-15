import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BackgroundImage {
    private final BufferedImage bufferedBackgroundImage;

    public BackgroundImage() {
        try {
            bufferedBackgroundImage = ImageIO.read(new File("src/Deck/background.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Image getImage(Graphics g) {
        return this.bufferedBackgroundImage;
    }
}
