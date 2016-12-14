package com.example.administrator.mapther;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.opengl.Visibility;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DeliverActivity extends AppCompatActivity {

    LocationManager locationManager;
    private EditText deliverNum;
    private Button deliverBtn;
    private String num;
    private ListView listView;
    private DeliverAdapter deliverAdapter = null;
    private Spinner spinner;
    private List<String> company_list;
    private ArrayAdapter<String> arr_adapter;
    private Map<String, String> map;
    private String companyName;
    private String companyCode;
    String currentCompanyName;
    private TextView currentComName;
    private ImageView deliver_back;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                currentComName.setText("快递公司:" + currentCompanyName);
                currentComName.setVisibility(View.VISIBLE);
                listView.setAdapter(deliverAdapter);
            }
            else if (msg.what == 1) {
                Toast.makeText(DeliverActivity.this, "没有输入快递单号或者快递单号是错的", Toast.LENGTH_SHORT).show();
            }
    }
};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_deliver);
            init();
            registEventListener();
    }

    public void init() {
        map = new LinkedHashMap<>();
        deliverNum = (EditText) findViewById(R.id.deliver_num);
        deliverBtn = (Button) findViewById(R.id.deliver_btn);
        listView = (ListView) findViewById(R.id.liveView);
        spinner = (Spinner) findViewById(R.id.cityList);
        currentComName = (TextView) findViewById(R.id.currentComName);
        deliver_back = (ImageView) findViewById(R.id.deliver_back);
        company_list = new ArrayList<String>();
        company_list.add("顺丰速运");
        map.put("顺丰速运", "shunfeng");
        company_list.add("申通快递");
        map.put("申通快递", "shentong");
        company_list.add("圆通速递");
        map.put("圆通速递", "yuantong");
        company_list.add("韵达快运");
        map.put("韵达快运", "yunda");
        company_list.add("EMS");
        map.put("EMS", "ems");
        company_list.add("中通快递");
        map.put("中通快递","zhongtong");
        company_list.add("百世汇通");
        map.put("百世汇通", "huitong");
        company_list.add("天天快递");
        map.put("天天快递", "tiantian");
        company_list.add("宅急送");
        map.put("宅急送", "zhaijisong");
        company_list.add("速尔物流");
        map.put("速尔物流", "sure");
        company_list.add("中铁快运");
        map.put("中铁快运","zhongtie");
        company_list.add("德邦物流");
        map.put("德邦物流", "debang");
        company_list.add("京广速递");
        map.put("京广速递", "jingguang");
        company_list.add("京东快递");
        map.put("京东快递", "jingdong");
        company_list.add("捷特快递");
        map.put("捷特快递", "jiete");


        arr_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, company_list);
        arr_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                companyName = company_list.get(arg2);
                companyCode = transformToCode(companyName);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
//                myTextView.setText("Nothing");
            }
        });

        deliver_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public String transformToCode(String deliverCompany) {
        String companyCode = map.get(deliverCompany);
        return companyCode;
    }
    public void registEventListener() {
        deliverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                num = deliverNum.getText().toString();
                getDelieveInfo();
            }
        });
    }

    private void getDelieveInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    String url = "http://ali-deliver.showapi.com/showapi_expInfo?com=" + companyCode + "&nu=" + deliverNum.getText();
                    String Appcode = "APPCODE 3fa1cd2d97214163be5deb3f4c4cf424";
                    connection = (HttpURLConnection) ((new URL(url.toString()).openConnection()));
                    connection.setRequestProperty("Authorization", Appcode);
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    String result = "";
                    while ((line = reader.readLine()) != null) {
                        result += line;
                    }
                    processData(result);
                } catch (Exception ex) {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    Log.i("Key", ex.toString());
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void processData(String result) {
//        JSONObject jsonObject=
        try {
            JSONObject jsonObject = new JSONObject(result);
//            int ischeck = jsonObject.getInt("ischeck");
            JSONObject data1 = jsonObject.getJSONObject("showapi_res_body");
            JSONArray data = data1.getJSONArray("data");
            currentCompanyName = data1.getString("expTextName");
            List<DeliverItem> deliverItems = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonObject1 = data.getJSONObject(i);
                String myDate = jsonObject1.getString("time");
                String date = myDate.substring(0, myDate.indexOf(" "));
                String time = myDate.substring(myDate.indexOf(" ")+1);
                String content = jsonObject1.getString("context");
                DeliverItem item = new DeliverItem(time, date, content);
                deliverItems.add(item);
            }
            deliverAdapter = new DeliverAdapter(DeliverActivity.this, deliverItems);
            Message message = new Message();
            message.what = 0;
            handler.sendMessage(message);


            Log.i("key", result);
        }
        catch (Exception e) {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
            e.printStackTrace();
        }
        Log.i("key", result);
    }

}
