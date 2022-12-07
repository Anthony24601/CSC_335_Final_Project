/**
 * File: SoundEffect.java
 * Author: Grace Driskill
 * Purpose: Represents a sound from a .wav file that can be played
 * Construstor
 * SoundEffect(String fileName)
 */
package UI;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundEffect{
    Clip clip;

    /**
     * Creates a SoundEffect from a specified .wav file
     * @param fileName String in format *.wav
     */
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

    /**
     * Plays this SoundEffect
     */
    public void play(){
        clip.setFramePosition(0);
        clip.start();
    }
}
