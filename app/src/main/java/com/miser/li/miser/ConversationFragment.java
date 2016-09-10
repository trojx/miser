package com.miser.li.miser;

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
    private ConversationAdapter mMsgAdapter;//数据适配器

    private List<ConvertsationBean> mMsgBeanList = new ArrayList<ConvertsationBean>();//保存获取到的数据
    private String mcookstr;//保存cookies
    private static String URL = "http://zhangtingx.top:888/getChatList/?pid=";
    private Logger gLogger;
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
        new ConversationFragment.MsgAsyncTask().execute(URL);
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


    //内部类 异步执行网络任务
    public  class MsgAsyncTask extends AsyncTask<String,Integer,List<ConvertsationBean>>
            //启动任务执行的输入参数”、“后台任务执行的进度”、“后台计算结果的类型”
    {


       // List<ConvertsationBean> mMsgListView = new ArrayList<ConvertsationBean>();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request.Builder mReqBuilder = new Request.Builder();

        private String pid = "0";

        private List<ConvertsationBean> getJsonData(String url)//获取数据
        {
            url += pid;
            try {
                //mMsgBeanList.clear();
                mReqBuilder.url(url);
                Request request = mReqBuilder.build();
                Response response = mOkHttpClient.newCall(request).execute();
                String resString = response.body().string();

                Document doc = Jsoup.parse(resString);
                Elements contents = doc.getElementsByAttributeValue("class", "chattxt");
                for (Element li : contents) {
                    pid = li.attr("pid");
                    String Text = li.ownText();
                    String time = li.getElementsByTag("span").text();

                    ConvertsationBean MsgBean = new ConvertsationBean();
                    MsgBean.mMsg = Text;
                    MsgBean.mName = "铁扇公主";
                    MsgBean.mTime =time;
                    mMsgBeanList.add(MsgBean);
                }

            } catch (Exception e) {
                gLogger.debug(e.toString());
                e.printStackTrace();
            }

            return mMsgBeanList;
        }
        /*
        private String readStream(InputStream is)//字节流
        {
            InputStreamReader isr;

            String result = "";

            try {
                String line = "";
                isr = new InputStreamReader(is, "utf-8");//字符流
                BufferedReader br = new BufferedReader(isr);
                while ((line = br.readLine()) != null)
                {
                    result += line;
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return result;
        }*/
        @Override
        protected List<ConvertsationBean> doInBackground(String... params) {//后台执行
            //addHttpHeaders(mReqBuilder);
            Log.d("xyz", "in");
            while (true)
            {
                //List<AlarmsBean> alarmsBeenList = new ArrayList<AlarmsBean>();

                    mMsgBeanList = getJsonData(params[0]);
                    if (!mMsgBeanList.isEmpty()) {
                        publishProgress(1);


                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        gLogger.debug(e.toString());
                        e.printStackTrace();
                    }

            }
        }
        @Override
        protected void onProgressUpdate(Integer... p)
        {
            mMsgAdapter.addItem(mMsgBeanList);
            //malarmsAdapter.notifyDataSetChanged();
        }
        @Override
        protected void onPostExecute(List<ConvertsationBean> alarmsBeen) {//doInBackground结束后执行该函数
            super.onPostExecute(alarmsBeen);
            //AlarmsAdapter adapter = new AlarmsAdapter(getActivity(), alarmsBeen);
           // mMsgBeanList.setAdapter(adapter);
        }
    }
}
