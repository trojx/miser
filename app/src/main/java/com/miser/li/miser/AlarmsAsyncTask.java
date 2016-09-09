package com.miser.li.miser;

import android.os.AsyncTask;
import android.util.Log;

import com.miser.li.miser.AlarmsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/*
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
        //AlarmsAdapter adapter = new AlarmsAdapter(MainActivity.this, alarmsBeen);
    }

*/