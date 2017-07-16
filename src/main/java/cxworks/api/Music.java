package cxworks.api;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cxworks on 17-6-14.
 */
public class Music {

    private Set<String> resources;
    private final String folder="songs/";

    public Music(){
        resources=new HashSet<String>();
        File file=new File(folder);
        String[] names=file.list();
        for (String n:names){
            if (n.endsWith(".mp3")&&!resources.contains(n)){
                resources.add(n.substring(0,n.length()-4).trim());
            }
        }
    }


    public Player play(String song){
        if (resources.contains(song)){
            return new Player(folder+song+".mp3");
        }else {
            if (callPython(song))
                return new Player(folder+song+".mp3");
        }
        return new Player(Constants.DOWNLOAD_ERROR);
    }

    public boolean callPython(String song){
        Thread thread=new Thread(new Player(Constants.DOWNLOAD));
        thread.start();
        try {
            Process process=Runtime.getRuntime().exec("python music.py "+song);
            process.waitFor();
            if (process.exitValue()==0){
                resources.add(song);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
