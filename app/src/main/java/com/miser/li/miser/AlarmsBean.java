package com.miser.li.miser;

/**
 * Created by li on 16-9-9.
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AlarmsBean implements Parcelable {
    public String masterIconUrl;
    public String masterName;
    public String sharesname;
    public String time;
    public String wdjg;
    public String type;
    public String number;

    public AlarmsBean(){}
    public AlarmsBean(Parcel source){
        this.masterIconUrl = source.readString();
        this.masterName = source.readString();
        this.sharesname = source.readString();
        this.time = source.readString();
        this.wdjg = source.readString();
        this.type = source.readString();
        this.number = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(masterIconUrl);
        parcel.writeString(masterName);
        parcel.writeString(sharesname);
        parcel.writeString(time);
        parcel.writeString(wdjg);
        parcel.writeString(type);
        parcel.writeString(number);
    }

    public static final Parcelable.Creator<AlarmsBean> CREATOR = new Creator<AlarmsBean>(){

        @Override
        public AlarmsBean createFromParcel(Parcel source) {
            return new AlarmsBean(source);
        }

        @Override
        public AlarmsBean[] newArray(int size) {
            return new AlarmsBean[size];
        }

    };

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