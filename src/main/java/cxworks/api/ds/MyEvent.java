package cxworks.api.ds;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by cxworks on 17-7-7.
 */

public class MyEvent {

        Date registerDate;
        String word;
        Date targetDate;

    public MyEvent(Date registerDate, String word, Date targetDate) {
        this.registerDate = registerDate;
        this.word = word;
        this.targetDate = targetDate;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public String getWord() {
        return word;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    @Override
    public String toString(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(registerDate.getTime());
        String words="您在"+calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1)
                +"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日提醒"+getWord();
        return words;
    }
}
