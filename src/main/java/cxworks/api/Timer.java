package cxworks.api;

import com.time.nlp.TimeNormalizer;
import com.time.nlp.TimeUnit;
import com.time.util.DateUtil;
import cxworks.api.ds.MyEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.PriorityQueue;

/**
 * Created by cxworks on 17-6-30.
 */
public class Timer {
    private TimeNormalizer normalizer;
    private PriorityQueue<MyEvent> priorityQueue;


    public Timer(){
        normalizer = new TimeNormalizer("lib/TimeExp.m");
        priorityQueue=new PriorityQueue<MyEvent>((a,b)->a.getTargetDate().compareTo(b.getTargetDate()));
    }


    public void parse(String word){
        try {
            normalizer.parse(word);
            TimeUnit[] units=normalizer.getTimeUnit();
            Date regist=new Date();
            Date tar=units[0].getTime();
            System.out.println("event tar: "+tar.toString());
            priorityQueue.add(new MyEvent(regist,word,tar));
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public MyEvent check(){
        if (priorityQueue.isEmpty()) return null;
        long now=System.currentTimeMillis();
        long tar=priorityQueue.peek().getTargetDate().getTime();
        if (now>=tar){
            return priorityQueue.poll();
        }else
            return null;
    }


    public static void main(String[] args){
        Timer timer=new Timer();
        timer.parse("下午3点开会");
        timer.priorityQueue.stream().forEach(e-> System.out.println(e));
    }

}
