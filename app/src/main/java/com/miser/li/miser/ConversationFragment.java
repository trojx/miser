package com.miser.li.miser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.miser.li.miser.R;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends Fragment {


    private String mTitle = "Default";

    public static final String TITLE = "title";


    private ListView mMsgListView;//消息显示列表
    public ConversationAdapter mMsgAdapter;//数据适配器

    private List<ConvertsationBean> mMsgBeanList = new ArrayList<ConvertsationBean>();//保存获取到的数据
    private String mcookstr;//保存cookies
    private static String URL = "http://zhangtingx.top:888/getChatList/?pid=";
    private Logger gLogger;
    private UIConversationReceiver mConReceiver;
    public void ui(List<ConvertsationBean> L)
    {
        mMsgBeanList.addAll(L);
        mMsgAdapter.addItem(mMsgBeanList);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        //获取视图实例
        View view= inflater.inflate(R.layout.conversation_main , container, false);
        mMsgListView = (ListView)view.findViewById(R.id.conversation_main_lv);
        //创建数据适配器
        mMsgAdapter = new ConversationAdapter(getActivity(),mMsgBeanList);
        mMsgListView.setAdapter(mMsgAdapter);

        mcookstr = readFileSdcardFile("/mnt/sdcard/miser.cook");
        gLogger = Logger.getLogger("miser");
        //new ConversationFragment.MsgAsyncTask().execute(URL);

        mConReceiver = new UIConversationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.miser.li.miser.CONVERSATION");
       getActivity().registerReceiver(mConReceiver, filter);//(CONVERSATION_CHANGED_ACTION));

        return view;
    }

    //读SD中的文件
    public String readFileSdcardFile(String fileName)
    {
        String res="";
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;

            while ((line = reader.readLine()) != null) {
                res += line;
            }
            reader.close();
        }

        catch(Exception e){
            gLogger.debug(e.toString());
            e.printStackTrace();
        }
        return res;
    }


    //内部类接受广播 方便更新界面
    public class UIConversationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(action.equals("com.miser.li.miser.CONVERSATION"))//(MainActivity.CONVERSATION_CHANGED_ACTION))
            {
                Bundle bundle = intent.getExtras();
                List<ConvertsationBean> MsgBeanList;// = new ArrayList<ConvertsationBean>();

                ArrayList list = bundle.getParcelableArrayList("list");
                MsgBeanList = (List<ConvertsationBean>) list.get(0);
                mMsgBeanList.addAll(MsgBeanList);
                mMsgAdapter.addItem(mMsgBeanList);
            }
        }

    }
}
