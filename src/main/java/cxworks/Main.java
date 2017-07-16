package cxworks;

import com.sun.media.sound.DirectAudioDeviceProvider;
import com.time.nlp.TimeNormalizer;
import com.time.nlp.TimeUnit;
import com.time.util.DateUtil;

import java.util.Calendar;
import java.util.Date;
import cxworks.api.*;
import cxworks.api.ds.MyEvent;

import javax.sound.sampled.Mixer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
/**
 * Created by cxworks on 17-6-14.
 */
public class Main {

    public static void main(String[] args){

        Music music=new Music();
        TTS tts=new TTS();
        TuringRobot turingRobot=new TuringRobot();
        ExecutorService executor=Executors.newFixedThreadPool(4);
        Player bg=null;
        Checker checker=new Checker();
        Timer timer=new Timer();
        while (true){
            try {

                if (checker.refreshed()) {
                    String result = checker.getText();
                    if (result.startsWith("播放")) {
                        bg = music.play(result.substring(2));
                        executor.execute(bg);
                    } else if(result.startsWith("日程")){
                        timer.parse(result.substring(2));
                    } else if (result.equalsIgnoreCase("暂停")) {
                        if (bg != null && bg.checkState())
                            bg.stop();
                    } else if (result.equalsIgnoreCase("开始")) {
                        if (bg != null && !bg.checkState())
                            bg.resume();
                    } else if (result.equalsIgnoreCase("你好小翔")){
                        Player player=new Player("audio/welcome.pcm");
                        player.playPMV();
                    } else if (result.equalsIgnoreCase("关闭")){
                        tts.shutDown();
                        executor.shutdown();
                        System.exit(0);
                    } else if (result.length() != 0) {
                        String answer = turingRobot.talk(result);
                        tts.startTTS(answer);
                        Thread.sleep(1 * 1000);
                        Player player1 = new Player("tts.pcm");
                        player1.run();
                    }
                }
                MyEvent event=timer.check();
                if (event!=null){


                    tts.startTTS(event.toString());
                    Thread.sleep(2 * 1000);
                    Player player1 = new Player("tts.pcm");
                    player1.run();
                }
                Thread.sleep(1*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
