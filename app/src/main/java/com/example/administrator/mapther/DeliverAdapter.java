package com.example.administrator.mapther;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chenjg on 2016/12/8.
 */

public class DeliverAdapter extends BaseAdapter {

    private Context context;
    private List<DeliverItem> list;
    public DeliverAdapter(Context context, List<DeliverItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public Object getItem(int i) {
        if (list == null) {
            return null;
        }
        return list.get(i);
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView;
        ViewHolder viewHolder;
        if (view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
            viewHolder = new ViewHolder();
            viewHolder.deliverContent = (TextView) convertView.findViewById(R.id.deliver_content);
            viewHolder.deliverDate = (TextView) convertView.findViewById(R.id.deliver_date);
            viewHolder.deliverTime = (TextView) convertView.findViewById(R.id.deliver_time);
            convertView.setTag(viewHolder);
        }
        else {
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String date = list.get(i).getDate();
        String time = list.get(i).getTime();
        String content = list.get(i).getContext();
        viewHolder.deliverContent.setText(content);
        viewHolder.deliverTime.setText(time);
        viewHolder.deliverDate.setText(date);
        return convertView;
    }

    private class ViewHolder{
        public TextView deliverDate;
        public TextView deliverTime;
        public TextView deliverContent;
    }
}
