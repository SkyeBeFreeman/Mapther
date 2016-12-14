package com.example.administrator.mapther;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/7.
 */

public class WeatherActivity extends AppCompatActivity {

    final int UPDATE_WEATHER = 0;
    final int UPDATE_FUTURE = 1;
    final int UPDATE_FAILED = 2;

    private boolean EDIT_FLAG = false;

    LocationManager locationManager;
    Location currentLocation;

    private String apikey = "6a3ae60bad55e2b4248128250fa253a5";

    private String url = "";
    private String city = "";
    private String cityid = "";
    private String temperature = "";
    private String weather = "";
    private String time = "";
    private String l_temperature = "";
    private String h_temperature = "";
    private String pre_weather = "";
    private List<String> future_weeks = new ArrayList<>();
    private List<String> future_weathers = new ArrayList<>();
    private List<String> future_hts = new ArrayList<>();
    private List<String> future_lts = new ArrayList<>();

    private TextView weather_city;
    private TextView weather_temperature;
    private EditText weather_search;
    private TextView weather_weather;
    private TextView weather_time;
    private TextView weather_trange;
    private ListView weather_future;
    private LinearLayout weather_background;
    private ImageView weather_back;

    SetColorAdapter setColorAdapter;

    WeatherChangeReceiver weatherChangeReceiver = new WeatherChangeReceiver();

    //用于更新UI
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            //更新当前天气
            if (msg.what == UPDATE_WEATHER) {
                weather_city.setText(city);
                weather_temperature.setText(temperature + "℃");
                weather_weather.setText(weather);
                weather_time.setText(time);
                weather_trange.setText(l_temperature + "℃~" + h_temperature + "℃");

                TransitionDrawable td;

                //根据天气设置背景图
                switch (weather) {
                    case "小雨":
                    case "中雨":
                    case "大雨":
                    case "阵雨":
                    case "雷阵雨":
                        setTextColor("white");
                        if (pre_weather.equals("雾") || pre_weather.equals("霾")|| pre_weather.equals("阴"))
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.h2r);
                        else if (pre_weather.equals("晴") || pre_weather.equals("多云"))
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.s2r);
                        else if (pre_weather.contains("雪"))
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.sn2r);
                        else if (pre_weather.isEmpty())
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.d2r);
                        else
                            break;
                        weather_background.setBackground(td);
                        td.startTransition(1000);
                        break;
                    case "雾":
                    case "霾":
                    case "阴":
                        setTextColor("dark");
                        if (pre_weather.contains("雨"))
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.r2h);
                        else if (pre_weather.equals("晴") || pre_weather.equals("多云"))
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.s2h);
                        else if (pre_weather.contains("雪"))
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.sn2h);
                        else if (pre_weather.isEmpty())
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.d2h);
                        else
                            break;
                        weather_background.setBackground(td);
                        td.startTransition(1000);
                        break;
                    case "晴":
                    case "多云":
                        setTextColor("dark");
                        if (pre_weather.contains("雨"))
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.r2s);
                        else if (pre_weather.equals("雾") || pre_weather.equals("霾") || pre_weather.equals("阴"))
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.h2s);
                        else if (pre_weather.contains("雪"))
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.sn2s);
                        else if (pre_weather.isEmpty())
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.d2s);
                        else
                            break;
                        weather_background.setBackground(td);
                        td.startTransition(1000);
                        break;
                    case "小雪":
                    case "中雪":
                    case "大雪":
                    case "阵雪":
                        setTextColor("dark");
                        if (pre_weather.contains("雨"))
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.r2sn);
                        else if (pre_weather.equals("晴") || pre_weather.equals("多云"))
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.s2sn);
                        else if (pre_weather.equals("雾") || pre_weather.equals("霾")|| pre_weather.equals("阴"))
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.h2sn);
                        else if (pre_weather.isEmpty())
                            td = (TransitionDrawable) getResources().getDrawable(R.drawable.d2sn);
                        else
                            break;
                        weather_background.setBackground(td);
                        td.startTransition(1000);
                        break;
                    default:
                        setTextColor("white");
                        break;
                }

            }

            //显示未来的天气
            if (msg.what == UPDATE_FUTURE) {
                List<Future> futures = new ArrayList<>();
                for (int i = 0; i < future_weeks.size(); i++)
                    futures.add(new Future(future_weeks.get(i), future_weathers.get(i), future_lts.get(i) + "~" + future_hts.get(i)));

                setColorAdapter = new SetColorAdapter(getApplicationContext(), futures);

                switch (weather) {
                    case "小雨":
                    case "中雨":
                    case "大雨":
                    case "阵雨":
                    case "雷阵雨":
                        setColorAdapter.setColor("white");
                        break;
                    case "雾":
                    case "霾":
                    case "阴":
                        setColorAdapter.setColor("dark");
                        break;
                    case "晴":
                    case "多云":
                        setColorAdapter.setColor("dark");
                        break;
                    case "小雪":
                    case "中雪":
                    case "大雪":
                    case "阵雪":
                        setColorAdapter.setColor("dark");
                        break;
                    default:
                        setColorAdapter.setColor("dark");
                        break;
                }
                weather_future.setAdapter(setColorAdapter);
            }

            //更新失败时，弹出Toast提示
            if (msg.what == UPDATE_FAILED) {
                Toast.makeText(getApplicationContext(), "查询失败，请检查网络设置或城市名", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };

    //设置当前天气字体的颜色
    private void setTextColor(String color) {
        switch (color) {
            case "white" :
                weather_city.setTextColor(getResources().getColor(R.color.colorWhite));
                weather_temperature.setTextColor(getResources().getColor(R.color.colorWhite));
                weather_search.setTextColor(getResources().getColor(R.color.colorWhite));
                weather_weather.setTextColor(getResources().getColor(R.color.colorWhite));
                weather_time.setTextColor(getResources().getColor(R.color.colorWhite));
                weather_trange.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            case "dark":
                weather_city.setTextColor(getResources().getColor(R.color.colorDark));
                weather_temperature.setTextColor(getResources().getColor(R.color.colorDark));
                weather_search.setTextColor(getResources().getColor(R.color.colorDark));
                weather_weather.setTextColor(getResources().getColor(R.color.colorDark));
                weather_time.setTextColor(getResources().getColor(R.color.colorDark));
                weather_trange.setTextColor(getResources().getColor(R.color.colorDark));
                break;
            default:
                break;
        }


    }


    //获取天气数据
    private String getData() {
        url = "http://apis.baidu.com/apistore/weatherservice/cityname?cityname=" + city;
        String result = "";
        HttpURLConnection httpURLConnection = null;
        try {
            URL mURL = new URL(url);
            httpURLConnection = (HttpURLConnection) mURL.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("apikey", apikey);
            httpURLConnection.setReadTimeout(50000);
            httpURLConnection.setConnectTimeout(100000);
            InputStream inputStream = httpURLConnection.getInputStream();
            result = getStringFromInputStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return result;
    }

    //获取未来几天的天气数据
    private String getFutureData() {
        url = "http://apis.baidu.com/apistore/weatherservice/recentweathers?cityname="
                + city + "&cityid" + cityid;
        String result = "";
        HttpURLConnection httpURLConnection = null;
        try {
            URL mURL = new URL(url);
            httpURLConnection = (HttpURLConnection) mURL.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("apikey", apikey);
            httpURLConnection.setReadTimeout(50000);
            httpURLConnection.setConnectTimeout(100000);

            InputStream inputStream = httpURLConnection.getInputStream();
            result = getStringFromInputStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return result;
    }

    //更新当前天气
    private boolean update() {
        try {
            String result = getData();
            //Log.i("weather", result);
            JSONObject jsonObject = new JSONObject(result);
            JSONObject retData = jsonObject.getJSONObject("retData");

            cityid = retData.getString("citycode");

            pre_weather = weather;

            temperature = retData.getString("temp");
            weather = retData.getString("weather");
            time = retData.getString("time");
            l_temperature = retData.getString("l_tmp");
            h_temperature = retData.getString("h_tmp");
            Message message = new Message();
            message.what = UPDATE_WEATHER;
            handler.sendMessage(message);
        } catch (Exception e) {
            Message message = new Message();
            message.what = UPDATE_FAILED;
            handler.sendMessage(message);
            e.printStackTrace();
        }
        return true;
    }

    //更新未来几天的天气
    private boolean updateFuture() {
        try {
            String future_result = getFutureData();
            //Log.i("future_result", future_result + cityid);
            JSONObject future_jsonObject = new JSONObject(future_result);
            JSONObject future_retData = future_jsonObject.getJSONObject("retData");
            JSONArray futures = future_retData.getJSONArray("forecast");

            future_weeks = new ArrayList<>();
            future_weathers = new ArrayList<>();
            future_hts = new ArrayList<>();
            future_lts = new ArrayList<>();
            for (int i = 0; i < futures.length(); i++) {
                JSONObject future = futures.getJSONObject(i);
                future_weeks.add(future.getString("week"));
                future_weathers.add(future.getString("type"));
                future_hts.add(future.getString("hightemp"));
                future_lts.add(future.getString("lowtemp"));
            }

            Message message = new Message();
            message.what = UPDATE_FUTURE;
            handler.sendMessage(message);
        } catch (Exception e) {
            Message message = new Message();
            message.what = UPDATE_FAILED;
            handler.sendMessage(message);
            e.printStackTrace();
        }
        return true;
    }

    //通过输入流来获取字符串
    private String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();
        os.close();
        return state;
    }

    //判断用户是否输入结束
    private boolean isEditTextLoseFocus(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom)
                return false;
            else
                return true;
        }
        return false;
    }

    //用户输入结束时，隐藏输入法并更新天气
    private void editTextLoseFocus(IBinder token) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (token != null && EDIT_FLAG) {
            inputMethodManager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            EDIT_FLAG = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    city = weather_search.getText().toString();
                    update();
                    updateFuture();
                    notifyWeatherChange();
                }
            }).start();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isEditTextLoseFocus(v, motionEvent))
                editTextLoseFocus(v.getWindowToken());
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);

        init();

        weather_city = (TextView) findViewById(R.id.weather_city);
        weather_temperature = (TextView) findViewById(R.id.weather_temperature);
        weather_search = (EditText) findViewById(R.id.weather_search);
        weather_weather = (TextView) findViewById(R.id.weather_weather);
        weather_time = (TextView) findViewById(R.id.weather_time);
        weather_trange = (TextView) findViewById(R.id.weather_trange);
        weather_future = (ListView) findViewById(R.id.weather_future);
        weather_background = (LinearLayout) findViewById(R.id.weather_background);
        weather_back = (ImageView) findViewById(R.id.weather_back);

        city = weather_city.getText().toString();

        weather_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EDIT_FLAG = true;
            }
        });

        weather_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //注册动态广播
        IntentFilter dynamic_filter = new IntentFilter();
        dynamic_filter.addAction("WEATHER_CHANGE");
        registerReceiver(weatherChangeReceiver,dynamic_filter);

    }

    //初始化provider
    public boolean init() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 2, 50, GPSlocationListener);
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkLocationLinstener);
                }
            } else
                return false;
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return true;
    }

    //根据位置信息获取当前所在城市
    public void getCityName() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    String url = "http://api.map.baidu.com/geocoder/v2/?location=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() + "&output=json&pois=1&ak=Ns9bbGEDR0xSpoXDCChkfTBThXuaQGuZ";
                    connection = (HttpURLConnection) ((new URL(url.toString()).openConnection()));
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        int i = line.indexOf("\"city\"");
                        String b = line.substring(i + 8, i + 20);
                        int index = b.indexOf('"');
                        city = b.substring(0, index - 1);
                        update();
                        updateFuture();
                        notifyWeatherChange();
                    }
                } catch (Exception ex) {
                    Log.i("Key", ex.toString());
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    //广播天气发生变化
    private void notifyWeatherChange() {
        Intent intent = new Intent("WEATHER_CHANGE");
        Bundle bundle = new Bundle();
        bundle.putString("city", city);
        bundle.putString("weather", weather + "  " + temperature + "℃");
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    private LocationListener networkLocationLinstener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (currentLocation != null) return;
            currentLocation = location;
            getCityName();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    //位置监听器
    private LocationListener GPSlocationListener = new LocationListener() {
        private boolean isRemove = false;//判断网络监听是否移除

        @Override
        public void onLocationChanged(Location location) {
            if (currentLocation != null) return;
            currentLocation = location;
            getCityName();
            if (location != null && !isRemove) {
                try {
                    locationManager.removeUpdates(networkLocationLinstener);
                    isRemove = true;
                } catch (SecurityException e) {

                }
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (LocationProvider.OUT_OF_SERVICE == status) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkLocationLinstener);
                } catch (SecurityException e) {

                }
            }
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

}
