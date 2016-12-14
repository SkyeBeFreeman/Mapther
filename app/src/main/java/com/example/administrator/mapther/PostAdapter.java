package com.example.administrator.mapther;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.mapther.Post;
import com.example.administrator.mapther.R;

import java.util.List;
import java.util.Objects;

/**
 * Created by zhtian on 2016/10/9.
 */

public class PostAdapter extends BaseAdapter {

    private Context context;
    private List<Post> list;

    public PostAdapter(Context context, List<Post> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        if (list == null) {
            return null;
        }
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView;
        ViewHolder viewHolder;
        if (view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.post_item, null);
            viewHolder = new ViewHolder();
            viewHolder.post_user_name = (TextView) convertView.findViewById(R.id.post_user_name);
            viewHolder.post_content = (TextView) convertView.findViewById(R.id.post_short_content);
            viewHolder.post_phone = (TextView) convertView.findViewById(R.id.post_phone);
            convertView.setTag(viewHolder);
        } else {
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.post_user_name.setText(list.get(i).getUser_name());
        viewHolder.post_content.setText(list.get(i).getContent());
        viewHolder.post_phone.setText(list.get(i).getPhone());

        return convertView;
    }

    private class ViewHolder {
        public TextView post_user_name;
        public TextView post_content;
        public TextView post_phone;
    }

}

