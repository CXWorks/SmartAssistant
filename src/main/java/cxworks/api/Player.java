package cxworks.api;

import javazoom.jl.decoder.JavaLayerException;

import javax.sound.sampled.*;
import java.io.*;

/**
 * Created by cxworks on 17-6-13.
 */
public class Player implements Runnable {
    String path;

    public Player(String path){this.path=path;}

    private volatile boolean state;
    public  boolean checkState(){return state;}
    private javazoom.jl.player.Player player;
    private int length=0;
    public void stop(){
        if (state){
            if (path.endsWith("mp3")){
                length=player.getPosition();
                player.close();
                state=false;
            }
        }
    }

    public void resume(){
        if (state==false){
            if (path.endsWith("mp3")){
                try {
                    state=true;
                    player = new javazoom.jl.player.Player(new BufferedInputStream(new FileInputStream(path)));
                    player.play(length);
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void playMP3(){
        BufferedInputStream buffer = null;
        try {
            buffer = new BufferedInputStream(new FileInputStream(path));
            player = new javazoom.jl.player.Player(buffer);

            player.play();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }


    }

    public void playPMV(){
        try {
            File file = new File(path);
            int offset = 0;
            int bufferSize = Integer.valueOf(String.valueOf(file.length())) ;
            byte[] audioData = new byte[bufferSize];
            InputStream in = new FileInputStream(file);
            in.read(audioData);
            float sampleRate = 16000;
            int sampleSizeInBits = 16;
            int channels = 1;
            boolean signed = true;
            boolean bigEndian = false;
            AudioFormat af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
            SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, af, bufferSize);

            SourceDataLine sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(af);
            sdl.start();
            FloatControl volume= (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue((float) (volume.getMaximum()));
            while (offset < audioData.length) {
                offset += sdl.write(audioData, offset, bufferSize);
            }
        } catch (LineUnavailableException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        } catch (IOException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    public static void main(String[] args){


        Player player=new Player("out.pcm");
        player.playPMV();

    }

    public void run() {
        if (path==null||path.equalsIgnoreCase("")) return;
        if (path.endsWith("pcm")){
            state=true;
            playPMV();
        }
        else if(path.endsWith("mp3")){
            state=true;
            playMP3();
        }

    }


}
