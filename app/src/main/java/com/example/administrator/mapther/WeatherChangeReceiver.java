package com.example.administrator.mapther;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Created by Administrator on 2016/12/11.
 */

public class WeatherChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("WEATHER_CHANGE")) {
            Bundle bundle = intent.getExtras();

            NotificationManager manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);

            builder.setContentTitle(bundle.getString("city") + "的天气更新了~")
                    .setContentText(bundle.getString("weather"))
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.weather_notification))
                    .setSmallIcon(R.drawable.weather_notification);

            Intent mIntent = new Intent(context, WeatherActivity.class);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);
            builder.setContentIntent(mPendingIntent);
            Notification notification = builder.build();
            manager.notify(0, notification);

            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            rv.setImageViewResource(R.id.weather_future_week, R.drawable.weather_notification);
            rv.setTextViewText(R.id.weather_wiget_text, bundle.getString("weather"));
            mgr.updateAppWidget(new ComponentName(context, WeatherWidget.class), rv);
        }
    }
}
