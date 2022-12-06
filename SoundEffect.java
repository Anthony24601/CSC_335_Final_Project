import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundEffect{
    Clip clip;

    public SoundEffect(String fileName){
        try{
            File file = new File(fileName);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
            }
            catch(Exception e){
                System.out.println("Error setting sound file.");
            }
    }

    public void play(){
        clip.setFramePosition(0);
        clip.start();
    }
}
