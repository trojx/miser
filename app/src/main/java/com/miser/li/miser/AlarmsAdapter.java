package com.miser.li.miser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.miser.li.miser.AlarmsBean;
import com.miser.li.miser.R;

import java.util.List;

/**
 * Created by li on 16-9-9.
 */
public class AlarmsAdapter extends BaseAdapter {

    private List<AlarmsBean> mAlarmsBeanList;
    private LayoutInflater mInflater;


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
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.ivIcon.setImageResource(R.mipmap.ic_launcher);
        viewHolder.tvMastername.setText(mAlarmsBeanList.get(i).masterName);
        return view;
    }

    class ViewHolder{
        public TextView tvMastername,tvSharesname,tvTime,tvGdjg,tvType,tvNumber;
        public ImageView ivIcon;
    }
}