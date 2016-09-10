package com.miser.li.miser;

/**
 * Created by li on 16-9-9.
 */
import java.text.SimpleDateFormat;
import java.util.Date;


public class AlarmsBean {
    public String masterIconUrl;
    public String masterName;
    public String sharesname;
    public String time;
    public String wdjg;
    public String type;
    public String number;

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof AlarmsBean)){
            return false;
        }
        AlarmsBean ab = (AlarmsBean) obj;

        return (this.sharesname.equals(ab.sharesname)) &&
                (this.type.equals(ab.type)) &&
                 (this.masterName.equals(ab.masterName));

    }
}