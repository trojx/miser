package com.miser.li.miser;

/**
 * Created by li on 16-9-10.
 */
public class ConvertsationBean {

    public String mMsg;//信息内容
    public String mTime;
    public String mName;
    public String mImgUrl;

    public ConvertsationBean(){
    }
    public ConvertsationBean(String name,String time,String msg,String img){
        this.mMsg=msg;
        this.mName=name;
        this.mImgUrl=img;
        this.mTime=time;
    }
}
