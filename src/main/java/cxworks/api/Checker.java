package cxworks.api;

import net.dongliu.requests.Requests;

/**
 * Created by cxworks on 17-6-27.
 */
public class Checker {

    class Status{
        long time;
        String text;
    }
    Status status;

    public Checker(){
        this.status=check();
    }

    public boolean refreshed(){
        Status now=check();
        if (now.time>this.status.time){
            this.status=now;
            return true;
        }
        return false;
    }

    public String getText(){
        if (this.status.text==null)
            return "";
        return this.status.text;
    }

    private Status check(){
        Status status=Requests.get("http://115.159.106.212:8090/q").send().readToJson(Status.class);
        return status;
    }


}
