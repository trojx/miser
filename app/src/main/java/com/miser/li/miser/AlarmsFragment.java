package com.miser.li.miser;

import android.app.DownloadManager;
import android.graphics.Color;
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
        gLogger = Logger.getLogger("CrifanLiLog4jTest");
    }

    private String mTitle = "Default";

    public static final String TITLE = "title";

    private static String URL = "http://www.imooc.com/api/teacher?type=4&num=1";
    private ListView mAlarmsListView;//买卖动态列表
    private AlarmsAdapter malarmsAdapter;//数据适配器

    private List<AlarmsBean> mAlarmsBeanList = new ArrayList<AlarmsBean>();//保存获取到的数据

    private String mcookstr;
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

        mcookstr = readFileSdcardFile("/mnt/sdcard/miser.cook");
        new AlarmsAsyncTask().execute(URL);



        return view;
    }

    //写数据到SD中的文件
    public void writeFileSdcardFile(String fileName,String write_str)
    {
        try{

            FileOutputStream fout = new FileOutputStream(fileName);
            byte [] bytes = write_str.getBytes();

            fout.write(bytes);
            fout.close();
        }

        catch(Exception e){
            gLogger.debug(e.toString());
            e.printStackTrace();
        }
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


    private void addHttpHeaders(Request.Builder reqBuilder)
    {
        reqBuilder.addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        //reqBuilder.addHeader("Accept-Encoding","gzip, deflate, sdch");//服务端没有压缩
        reqBuilder.addHeader("Connection","keep-alive");
        reqBuilder.addHeader("Cookie",mcookstr);
        reqBuilder.addHeader("Referer","http://t.10jqka.com.cn/circle/8530");
        reqBuilder.addHeader("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.101 Safari/537.36");
        reqBuilder.addHeader("X-Requested-With","XMLHttpRequest");
    }
    //内部类 异步执行网络任务
    public  class AlarmsAsyncTask extends AsyncTask<String,Integer,List<AlarmsBean>>
        //启动任务执行的输入参数”、“后台任务执行的进度”、“后台计算结果的类型”
    {


        List<AlarmsBean> mAlarmsBeenList = new ArrayList<AlarmsBean>();//保存历史所有数据 用以去重
        List<AlarmsBean> mNewAlarmsBeenList = new ArrayList<AlarmsBean>();//保存最新数据 用以更新界面
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request.Builder mReqBuilder = new Request.Builder();



        private Boolean hasJsonData(String url )//先判断是否有新数据
        {

            url = "http://t.10jqka.com.cn/newcircle/message/getunread/";
            try {
                mReqBuilder.url(url);
                Request request = mReqBuilder.build();
                Response response = mOkHttpClient.newCall(request).execute();
                String jsonString = response.body().string();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(jsonString);
                    Integer errorCode = jsonObject.getInt("errorCode");
                    String errorMsg = jsonObject.getString("errorMsg");
                    if (errorCode != 0)
                    {
                        gLogger.debug(errorMsg);
                        return false;
                    }
                    //JSONObject jsonRes = jsonObject.getJSONObject("result");
                    if (jsonObject.getJSONObject("result").getInt("ace") > 0)//有新数据
                    {
                        return true;
                    }
                } catch (JSONException e) {
                    gLogger.debug(e.toString());
                    e.printStackTrace();
                }

            }
            catch (Exception e) {
                gLogger.debug(e.toString());
                e.printStackTrace();
            }

            return false;

        }
        private List<AlarmsBean> getJsonData(String url)//获取数据
        {

           //List<AlarmsBean> alarmsBeenList = new ArrayList<AlarmsBean>();
            url = "http://t.10jqka.com.cn/trace/trade/getEntrust/?_=" + String.valueOf(System.currentTimeMillis());
            try {
                mNewAlarmsBeenList.clear();
                mReqBuilder.url(url);
                Request request = mReqBuilder.build();
                Response response = mOkHttpClient.newCall(request).execute();
                String jsonString = response.body().string();

                JSONObject jsonObject;
                AlarmsBean alarmsBean;
                try {
                    jsonObject = new JSONObject(jsonString);
                    if ("成功".equals(jsonObject.getString("errormsg")))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            alarmsBean = new AlarmsBean();
                            alarmsBean.masterName = jsonObject.getString("nickname");
                            alarmsBean.masterIconUrl = jsonObject.getString("avatar");
                            alarmsBean.number = jsonObject.getString("wtsl");
                            alarmsBean.sharesname = jsonObject.getString("zqmc") + "   " + jsonObject.getString("zqdm");
                            alarmsBean.time = jsonObject.getString("time");
                            alarmsBean.wdjg = jsonObject.getString("wtjg");

                            String type = jsonObject.getString("type");
                            if ("B".equals(type))
                            {
                                alarmsBean.type = "买";
                            }
                            else if ("S".equals(type))
                            {
                                alarmsBean.type = "卖";
                            }
                            Boolean bExist = false;
                            for (AlarmsBean exist: mAlarmsBeenList)
                            {
                                if (exist.equals(alarmsBean))
                                {
                                    bExist = true;
                                }
                            }
                            if (!bExist)//该数据是新的
                            {
                                mAlarmsBeenList.add(alarmsBean);
                                mNewAlarmsBeenList.add(alarmsBean);
                            }
                        }
                    }

                } catch (JSONException e) {
                    gLogger.debug(e.toString());
                    e.printStackTrace();
                }

            } catch (IOException e) {
                gLogger.debug(e.toString());
                e.printStackTrace();
            }

            return mNewAlarmsBeenList;
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
        protected List<AlarmsBean> doInBackground(String... params) {//后台执行
            addHttpHeaders(mReqBuilder);
            while (true)
            {
                //List<AlarmsBean> alarmsBeenList = new ArrayList<AlarmsBean>();
                if (hasJsonData("")) {
                    mNewAlarmsBeenList = getJsonData(params[0]);
                    if (!mNewAlarmsBeenList.isEmpty()) {
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
        }
        @Override
        protected void onProgressUpdate(Integer... p)
        {
            malarmsAdapter.addItem(mNewAlarmsBeenList);
            //malarmsAdapter.notifyDataSetChanged();
        }
        @Override
        protected void onPostExecute(List<AlarmsBean> alarmsBeen) {//doInBackground结束后执行该函数
            super.onPostExecute(alarmsBeen);
           AlarmsAdapter adapter = new AlarmsAdapter(getActivity(), alarmsBeen);
            mAlarmsListView.setAdapter(adapter);
        }
    }
}
