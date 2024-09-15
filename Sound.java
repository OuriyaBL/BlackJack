import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

    Sound() throws Exception {
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("src/deck/background-music.wav"));
        //.wav file in this example is in the "src" folder
        Clip clip = AudioSystem.getClip();
        clip.open(inputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        Thread.sleep(10000); // looping as long as this thread is alive
    }
}
