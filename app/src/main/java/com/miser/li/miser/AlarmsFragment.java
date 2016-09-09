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

    private static String URL = "http://www.imooc.com/api/teacher?type=4&num=30";
    private ListView mAlarmsListView;//买卖动态列表
    private AlarmsAdapter malarmsAdapter;

    private List<String> getData(){
        List<String> data = new ArrayList<String>();
        for(int i = 0;i < 2;i++) {
            data.add(i+"");
        }
        return data;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null)
        {
            mTitle = getArguments().getString(TITLE);

        }



        View view= inflater.inflate(R.layout.alarms_main , container, false);
        mAlarmsListView = (ListView)view.findViewById(R.id.alarms_main_lv);

        new AlarmsAsyncTask().execute(URL);
        return view;
    }



    public  class AlarmsAsyncTask extends AsyncTask<String,Void,List<AlarmsBean>>
    {

        private List<AlarmsBean> getJsonData(String url)
        {
            List<AlarmsBean> alarmsBeenList = new ArrayList<>();
            try {
                String jsonString = readStream(new URL(url).openStream());
                Log.d("alarms", jsonString);
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
        protected List<AlarmsBean> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<AlarmsBean> alarmsBeen) {
            super.onPostExecute(alarmsBeen);
           AlarmsAdapter adapter = new AlarmsAdapter(getActivity(), alarmsBeen);
            mAlarmsListView.setAdapter(adapter);
        }
    }
}
