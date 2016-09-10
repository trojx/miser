package com.miser.li.miser;

/**
 * Created by li on 16-9-10.
 */


 import java.util.ArrayList;
 import java.util.List;

 import android.content.Context;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.BaseAdapter;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.TextView;


public class ConversationAdapter extends BaseAdapter{

    Context mContext;
    LayoutInflater mInflater;
    List<ConvertsationBean> mMsgList;


    public void addItem(List<ConvertsationBean> abl)
    {
        //Log.d("errrr","in");
        if (!abl.isEmpty()) {
            mMsgList.addAll(abl);
            notifyDataSetChanged();
        }
    }
    public ConversationAdapter(Context context,List<ConvertsationBean> arr){
        this.mContext=context;
        this.mInflater=LayoutInflater.from(context);
        this.mMsgList=arr;
    }

    @Override
    public int getCount() {
        if(mMsgList==null){
            return 0;
        }else{
            return mMsgList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if(mMsgList==null ){
            return null;
        }else{
            return mMsgList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  viewHolder;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.conversation_items, null);
            viewHolder=new ViewHolder();


            viewHolder.lly=(LinearLayout)convertView.findViewById(R.id.lly);
            viewHolder.tvTime=(TextView)convertView.findViewById(R.id.tv_time);
            viewHolder.tvName=(TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.tvMsg=(TextView)convertView.findViewById(R.id.tv_msg);
            viewHolder.ivHead=(ImageView)convertView.findViewById(R.id.iv_head);

            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        ConvertsationBean msgInfo=(ConvertsationBean)getItem(position);


        //viewHolder.lly.setVisibility(View.VISIBLE);
        viewHolder.tvTime.setText(msgInfo.mTime);
        viewHolder.tvName.setText(msgInfo.mName);
        viewHolder.tvMsg.setText(msgInfo.mMsg);
        //viewHolder.ivHead.setImageDrawable(mContext.getResources().getDrawable(msgInfo.mHead));
        viewHolder.ivHead.setImageResource(R.mipmap.head_4);
        //viewHolder.llyMy.setVisibility(View.GONE);

        return convertView;
    }
    class ViewHolder{
        /***
         * 显示别人的信息的控件
         */

        LinearLayout lly;
        TextView tvTime;
        TextView tvName;
        TextView tvMsg;
        ImageView ivHead;;
    }
}