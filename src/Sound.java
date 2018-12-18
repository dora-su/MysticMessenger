/**
 * [Sound.java]
 * @author Dora Su
 * Version: 2.0
 * Date: December 14, 2018
 * Makes playing sounds easier and less blocky, takes care of audiostreaminput, exceptions
 */

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Sound {

    private Clip clip;

    public Sound(String filename){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("dependencies/" + filename +".wav"));
            //get clip and open it
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.out.println("failed to retrieve sound: " + filename);
        }
    }

    public void play(){
        //Resets audio to the beginning of the file
        clip.setFramePosition(0);
        //Play the clip
        clip.start();
    }
}