package com.miser.li.miser;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.miser.li.miser.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import de.mindpipe.android.logging.log4j.LogConfigurator;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmsFragment extends Fragment {

    private Logger gLogger;

    public void configLog()
    {
        final LogConfigurator logConfigurator = new LogConfigurator();

        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "miser.log");
        // Set the root log level
        logConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.configure();

        //gLogger = Logger.getLogger(this.getClass());
        gLogger = Logger.getLogger("miser");
    }

    private String mTitle = "Default";

    public static final String TITLE = "title";


    private ListView mAlarmsListView;//买卖动态列表
    private AlarmsAdapter malarmsAdapter;//数据适配器

    private List<AlarmsBean> mAlarmsBeanList = new ArrayList<AlarmsBean>();//保存获取到的数据


    static int NotificationCount = 0;

    private UIAlarmsReceiver mUIAlarmsRecevier;
    private  void Notification(String Title, String Content)//发送通知栏消息
    {

        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        android.support.v4.app.NotificationCompat.Builder mBuilder = new android.support.v4.app.NotificationCompat.Builder(getContext());
        //系统收到通知时，通知栏上面显示的文字。
        mBuilder.setTicker(Content);
        //显示在通知栏上的小图标
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        //通知标题
        mBuilder.setContentTitle(Title);
        //通知内容
        mBuilder.setContentText(Content);
        //mBuilder.setDefaults(Notification.DEFAULT_ALL);
       //mBuilder.setDefaults(Notification.DEFAULT_SOUND);//设置声音
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);//设置指示灯，，，，，指示灯个震动都需要配置权限
        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);//设置震动效果
        //设置大图标，即通知条上左侧的图片（如果只设置了小图标，则此处会显示小图标）
        //mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.share_sina));
        //显示在小图标左侧的数字
        mBuilder.setNumber(6);

        //设置为不可清除模式
       /* mBuilder.setOngoing(true);
        //播放闹铃声
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(getContext(), alert);
            AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                player.setAudioStreamType(AudioManager.STREAM_ALARM);
                player.prepare();
            }
                player.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();*/

        //显示通知，id必须不重复，否则新的通知会覆盖旧的通知（利用这一特性，可以对通知进行更新）
        mNotificationManager.notify(NotificationCount++, mBuilder.build());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null)
        {
            mTitle = getArguments().getString(TITLE);

        }
        configLog();
        gLogger.debug("test log");
        //获取视图实例
        View view= inflater.inflate(R.layout.alarms_main , container, false);
        mAlarmsListView = (ListView)view.findViewById(R.id.alarms_main_lv);
        //创建数据适配器
        malarmsAdapter = new AlarmsAdapter(getActivity(), mAlarmsBeanList);
        mAlarmsListView.setAdapter(malarmsAdapter);


        //new AlarmsAsyncTask().execute(URL);


        mUIAlarmsRecevier = new UIAlarmsReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.miser.li.miser.ALARMS");
        getActivity().registerReceiver(mUIAlarmsRecevier, filter);//(CONVERSATION_CHANGED_ACTION));
        return view;
    }

    public Boolean isHaveNew(List<AlarmsBean> Old, List<AlarmsBean> New)
    {
        Boolean ret = false;
        Boolean bExist = false;
        int sizeOfNew = New.size();
        int sizeOfOld = Old.size();
        for (int i = 0; i < sizeOfNew; i++)
        {
            bExist = false;
            AlarmsBean N = New.get(i);
            for (int j = 0; j < sizeOfOld; j++)
            {
                if (N.equals(Old.get(j)))
                {
                    bExist = true;
                    break;
                }
            }
            if (!bExist)
            {//有新元素
                ret = true;
                Notification(N.masterName, N.type + " " + N.sharesname + " " + N.wdjg);
            }

        }
        return ret;
    }
    //内部类接受广播 方便更新界面
    public class UIAlarmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(action.equals("com.miser.li.miser.ALARMS"))//(MainActivity.CONVERSATION_CHANGED_ACTION))
            {
                Bundle bundle = intent.getExtras();
                List<AlarmsBean> ABeanList;// = new ArrayList<ConvertsationBean>();

                ArrayList list = bundle.getParcelableArrayList("list");
                ABeanList = (List<AlarmsBean>) list.get(0);
                if (isHaveNew(mAlarmsBeanList, ABeanList)) {
                    mAlarmsBeanList.addAll(ABeanList);
                    malarmsAdapter.addItem(mAlarmsBeanList);
                }
            }
        }

    }
}
