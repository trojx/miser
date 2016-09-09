package com.miser.li.miser;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmsFragment extends Fragment {


    private String mTitle = "Default";

    public static final String TITLE = "title";

    private static String URL = "http://www.imooc.com/api/teacher?type=4&num=1";
    private ListView mAlarmsListView;//买卖动态列表
    private AlarmsAdapter malarmsAdapter;//数据适配器

    private List<AlarmsBean> mAlarmsBeanList = new ArrayList<AlarmsBean>();//保存获取到的数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null)
        {
            mTitle = getArguments().getString(TITLE);

        }


        //获取视图实例
        View view= inflater.inflate(R.layout.alarms_main , container, false);
        mAlarmsListView = (ListView)view.findViewById(R.id.alarms_main_lv);
        //创建数据适配器
        malarmsAdapter = new AlarmsAdapter(getActivity(), mAlarmsBeanList);
        mAlarmsListView.setAdapter(malarmsAdapter);

        new AlarmsAsyncTask().execute(URL);


        return view;
    }
    //有新数据更新界面
    private void UpdateUIFunction(List<AlarmsBean> alarmsBeenList)
    {
        malarmsAdapter.addItem(alarmsBeenList);
    }

    public  class AlarmsAsyncTask extends AsyncTask<String,Integer,List<AlarmsBean>>
        //启动任务执行的输入参数”、“后台任务执行的进度”、“后台计算结果的类型”
    {

        List<AlarmsBean> malarmsBeenList = new ArrayList<AlarmsBean>();;
        private List<AlarmsBean> getJsonData(String url)
        {

            List<AlarmsBean> alarmsBeenList = new ArrayList<AlarmsBean>();
            try {
                String jsonString = readStream(new URL(url).openStream());
                //Log.d("alarms", jsonString);
                JSONObject jsonObject;
                AlarmsBean alarmsBean;

                try {
                    jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {

                        jsonObject = jsonArray.getJSONObject(i);
                        alarmsBean = new AlarmsBean();
                        alarmsBean.masterName = "汗来世";//jsonObject.getString("");
                        alarmsBeenList.add(alarmsBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return alarmsBeenList;
        }
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
        }
        @Override
        protected List<AlarmsBean> doInBackground(String... params) {//后台执行

            while (true)
            {
                //List<AlarmsBean> alarmsBeenList = new ArrayList<AlarmsBean>();
                malarmsBeenList = getJsonData(params[0]);
                if (!malarmsBeenList.isEmpty()) {
                    publishProgress(1);


                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        protected void onProgressUpdate(Integer... p)
        {
            malarmsAdapter.addItem(malarmsBeenList);
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
