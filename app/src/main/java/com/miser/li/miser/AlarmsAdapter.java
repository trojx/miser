package com.miser.li.miser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.miser.li.miser.AlarmsBean;
import com.miser.li.miser.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 16-9-9.
 */
public class AlarmsAdapter extends BaseAdapter {

    private List<AlarmsBean> mAlarmsBeanList;
    private LayoutInflater mInflater;


    public void addItem(List<AlarmsBean> abl)
    {
        //Log.d("errrr","in");
        if (!abl.isEmpty()) {
            mAlarmsBeanList= abl;
            if (mAlarmsBeanList.size() > 100)//最多显示100条
                return;
            notifyDataSetChanged();
        }
    }
    public AlarmsAdapter(Context context)
    {
        mAlarmsBeanList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }

    public AlarmsAdapter(Context context, List<AlarmsBean> data)
    {
        mAlarmsBeanList = data;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mAlarmsBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return mAlarmsBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null)
        {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.alarms_items, null);
            viewHolder.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            viewHolder.tvMastername = (TextView) view.findViewById(R.id.al_mastername);
            viewHolder.tvSharesname = (TextView) view.findViewById(R.id.al_shaersname);
            viewHolder.tvTime = (TextView) view.findViewById(R.id.al_time);
            viewHolder.tvGdjg = (TextView) view.findViewById(R.id.al_jg);
            viewHolder.tvType = (TextView) view.findViewById(R.id.al_type);
            viewHolder.tvNumber = (TextView) view.findViewById(R.id.al_number);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.ivIcon.setImageResource(R.mipmap.ic_launcher);
        viewHolder.tvMastername.setText(mAlarmsBeanList.get(i).masterName);
        viewHolder.tvSharesname.setText(mAlarmsBeanList.get(i).sharesname);
        viewHolder.tvTime.setText(mAlarmsBeanList.get(i).time);
        viewHolder.tvGdjg.setText(mAlarmsBeanList.get(i).wdjg);
        viewHolder.tvType.setText(mAlarmsBeanList.get(i).type);
        viewHolder.tvNumber.setText(mAlarmsBeanList.get(i).number);;
        return view;
    }

    class ViewHolder{
        public TextView tvMastername,tvSharesname,tvTime,tvGdjg,tvType,tvNumber;
        public ImageView ivIcon;
    }
}