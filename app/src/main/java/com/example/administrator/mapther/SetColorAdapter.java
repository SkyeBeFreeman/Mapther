package com.example.administrator.mapther;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/9.
 */

public class SetColorAdapter extends BaseAdapter {

    private List<Future> futures;
    Context context;
    String color;

    public void setColor(String color) {
        this.color = color;
    }

    public SetColorAdapter(Context context, List<Future> futures) {
        this.futures = futures;
        this.context = context;
    }

    private class ViewHolder {
        public TextView weather_future_weather;
        public TextView weather_future_week;
        public TextView weather_future_trange;
    }

    @Override
    public int getCount() {
        if (futures == null)
            return 0;
        return futures.size();
    }

    @Override
    public Object getItem(int position) {
        if (futures == null)
            return null;
        return futures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View convertView;
        ViewHolder viewHolder;

        if (view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.future_day, null);
            viewHolder = new ViewHolder();
            viewHolder.weather_future_weather = (TextView) convertView.findViewById(R.id.weather_future_weather);
            viewHolder.weather_future_week = (TextView) convertView.findViewById(R.id.weather_future_week);
            viewHolder.weather_future_trange = (TextView) convertView.findViewById(R.id.weather_future_trange);
            convertView.setTag(viewHolder);
        }
        else {
            convertView = view;
            viewHolder = (ViewHolder)convertView.getTag();
        }

        switch (color) {
            case "white" :
                viewHolder.weather_future_weather.setTextColor(context.getResources().getColor(R.color.colorWhite));
                viewHolder.weather_future_week.setTextColor(context.getResources().getColor(R.color.colorWhite));
                viewHolder.weather_future_trange.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case "dark":
                viewHolder.weather_future_weather.setTextColor(context.getResources().getColor(R.color.colorDark));
                viewHolder.weather_future_week.setTextColor(context.getResources().getColor(R.color.colorDark));
                viewHolder.weather_future_trange.setTextColor(context.getResources().getColor(R.color.colorDark));
                break;
            default:
                break;
        }

        viewHolder.weather_future_week.setText(futures.get(position).getWeek());
        viewHolder.weather_future_weather.setText(futures.get(position).getWeather());
        viewHolder.weather_future_trange.setText(futures.get(position).getTrange());

        return convertView;
    }
}
