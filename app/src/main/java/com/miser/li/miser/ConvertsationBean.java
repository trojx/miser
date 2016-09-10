package com.miser.li.miser;

/**
 * Created by li on 16-9-10.
 */
import android.os.Parcel;
import android.os.Parcelable;

public class ConvertsationBean  implements Parcelable {

    public String mMsg;//信息内容
    public String mTime;
    public String mName;
    public String mImgUrl;

    public ConvertsationBean(){

    }

    public ConvertsationBean(Parcel source){
        this.mMsg = source.readString();
        this.mTime = source.readString();
        this.mName = source.readString();
        this.mImgUrl = source.readString();
    }
    public ConvertsationBean(String name,String time,String msg,String img){
        this.mMsg=msg;
        this.mTime=time;
        this.mName=name;
        this.mImgUrl=img;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mMsg);
        parcel.writeString(mTime);
        parcel.writeString(mName);
        parcel.writeString(mImgUrl);

    }

    public static final Parcelable.Creator<ConvertsationBean> CREATOR = new Creator<ConvertsationBean>(){

        @Override
        public ConvertsationBean createFromParcel(Parcel source) {
            return new ConvertsationBean(source);
        }

        @Override
        public ConvertsationBean[] newArray(int size) {
            return new ConvertsationBean[size];
        }

    };

}
