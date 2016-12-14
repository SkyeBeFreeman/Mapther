package com.example.administrator.mapther;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Created by Administrator on 2016/12/11.
 */

public class WeatherWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        Intent clickInt = new Intent(context, WeatherActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, clickInt, 0);
        rv.setOnClickPendingIntent(R.id.weather_widget_img, pi);
        appWidgetManager.updateAppWidget(appWidgetIds, rv);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        Bundle bundle = intent.getExtras();

        if (intent.getAction().equals("WEATHER_CHANGE")) {
            rv.setImageViewResource(R.id.weather_widget_img, R.drawable.weather_notification);
            rv.setTextViewText(R.id.weather_wiget_text, bundle.getString("weather"));
            mgr.updateAppWidget(new ComponentName(context, WeatherWidget.class), rv);
        }

        super.onReceive(context, intent);
    }
}
