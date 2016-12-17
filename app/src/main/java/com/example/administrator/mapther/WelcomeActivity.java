package com.example.administrator.mapther;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.administrator.mapther.titanic.Titanic;
import com.example.administrator.mapther.titanic.TitanicTextView;
import com.example.administrator.mapther.titanic.Typefaces;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

public class WelcomeActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TitanicTextView tv = (TitanicTextView) findViewById(R.id.titanic_tv);

        // set fancy typeface
        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));

        // start animation
        new Titanic().start(tv);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            Toast.makeText(WelcomeActivity.this,
                                    "Granted", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable(){

                                @Override
                                public void run() {
                                    Intent mainIntent = new Intent(WelcomeActivity.this,MainActivity.class);
                                    startActivity(mainIntent);//跳转到MainActivity
                                    finish();//结束SplashActivity
                                }
                            }, 5000);//给postDelayed()方法传递延迟参数
                        }
                        else {
                            Toast.makeText(WelcomeActivity.this,
                                    "App will finish in 3 seconds", Toast.LENGTH_SHORT).show();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 5000);
                        }
                    }
                });

    }
}
