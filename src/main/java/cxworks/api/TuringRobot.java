package cxworks.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dongliu.requests.Parameter;
import net.dongliu.requests.Request;
import net.dongliu.requests.Requests;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cxworks on 17-6-13.
 */
public class TuringRobot {
    private final String key="e2be8415341073f37f126a14163f8a44";

    public TuringRobot(){}
    class Result{
        int code;
        String text;
    }
    class RobotRes{
        String reason;
        int error_code;
        Result result;
    }

    public String talk(String msg){
        if (msg==null) msg="";
        Map<String,String> map=new HashMap<String, String>();
        map.put("key",key);
        map.put("info",msg);

        RobotRes res=Requests.get("http://op.juhe.cn/robot/index")
                .params(map)
                .send()
                .readToJson(RobotRes.class);
        if (res==null||res.result==null||res.result.text==null) return "";
        return res.result.text;
    }

    public static void main(String[] args) throws LineUnavailableException {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info info: mixerInfos){
            Mixer m = AudioSystem.getMixer(info);
            Line.Info[] lineInfos = m.getSourceLineInfo();

            lineInfos = m.getTargetLineInfo();
            for (Line.Info lineInfo:lineInfos){
                System.out.println (m+"---"+lineInfo);
                Line line = m.getLine(lineInfo);
                System.out.println("\t-----"+line);

            }

        }

    }
}
