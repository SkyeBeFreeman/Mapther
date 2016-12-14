package com.example.administrator.mapther;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {

    private static final String STRING = "";
    private String string;
    private TextView textView;
    private TextView login;
    private TextView regist;
    private TextView update;
    private TextView logout;
    private UserDB db;
    private UserInfo myInfo;
    private TextView userName;
    private TextView userPhone;
    private TextView updateName;
    private EditText updatePhone;
    private EditText updatePwd;
    private LinearLayout unloginMessage;
    private LinearLayout loginMessage;
    private ImageView portrait;


    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param content Parameter 1.
     * @return A new instance of fragment MyFragment.
     */
    public static MyFragment newInstance(String content) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString(STRING, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my, container, false);

        if (getArguments() != null) {
            string = getArguments().getString(STRING);
        }
        logout = (TextView) view.findViewById(R.id.logout);
        loginMessage = (LinearLayout) view.findViewById(R.id.login_message);
        unloginMessage = (LinearLayout) view.findViewById(R.id.unlogin_message);
        userName = (TextView) view.findViewById(R.id.user_name);
        login = (TextView) view.findViewById(R.id.login);
        regist = (TextView) view.findViewById(R.id.regist);
        //update = (TextView) view.findViewById(R.id.update);
        portrait = (ImageView) view.findViewById(R.id.portrait);
        db = new UserDB(getActivity(), "123", null, 1);

        if (MainActivity.getMy_name() != null && !TextUtils.equals(MainActivity.getMy_name(), "")) {
            myInfo = db.getByName(MainActivity.getMy_name());
        }
        showInfo();
        registEventListener();

        return view;
    }

    private void registEventListener() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInfo = null;
                loginMessage.setVisibility(View.GONE);
                unloginMessage.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "登出成功", Toast.LENGTH_SHORT).show();
                MainActivity.setMy_name("");
                SharedPreferences sharedPref = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("MY_NAME", "");
                editor.commit();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View myView = inflater.inflate(R.layout.login, null);
                final EditText login_name = (EditText) myView.findViewById(R.id.login_name);
                final EditText login_pws = (EditText) myView.findViewById(R.id.login_pwd);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(myView);
                builder.setNegativeButton("取消登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfo userInfo;
                        if((userInfo =  db.query(login_name.getText().toString(), login_pws.getText().toString())) == null) {
                            Toast.makeText(getActivity(), "该用户名不存在或者密码错误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        myInfo = userInfo;

                        Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
                        MainActivity.setMy_name(myInfo.getName());
                        SharedPreferences sharedPref = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("MY_NAME", myInfo.getName());
                        editor.commit();

                        showInfo();
                    }
                });
                builder.show();
            }
        });
        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myInfo == null) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    login.performClick();
                    return;
                }
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View myView = inflater.inflate(R.layout.update_info, null);
                updateName = (TextView) myView.findViewById(R.id.update_name);
                updatePwd = (EditText) myView.findViewById(R.id.update_pwd);
                updatePhone = (EditText) myView.findViewById(R.id.update_phone);
                updateName.setText(myInfo.getName());
                updatePwd.setText(myInfo.getPassword());
                updatePhone.setText(myInfo.getPhone());
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(myView);
                builder.setNegativeButton("放弃更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myInfo.setPassword(updatePwd.getText().toString());
                        myInfo.setPhone(updatePhone.getText().toString());
                        db.updateToDB(myInfo);
                        Toast.makeText(getActivity(), "更新成功",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View myView = inflater.inflate(R.layout.regist, null);
                final EditText regist_name = (EditText) myView.findViewById(R.id.regist_name);
                final EditText regist_pws = (EditText) myView.findViewById(R.id.regist_pwd);
                final EditText regist_phone = (EditText) myView.findViewById(R.id.regist__phone);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(myView);
                builder.setNegativeButton("放弃注册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("注册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(db.query(regist_name.getText().toString())) {
                            Toast.makeText(getActivity(), "该用户名已被注册", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        db.insertToDB(regist_name.getText().toString(), regist_pws.getText().toString(), regist_phone.getText().toString());
                        myInfo = new UserInfo(regist_name.getText().toString(), regist_pws.getText().toString(), regist_phone.getText().toString());
                        MainActivity.setMy_name(myInfo.getName());
                        SharedPreferences sharedPref = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("MY_NAME", myInfo.getName());
                        editor.commit();
                        showInfo();
                        Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    public void showInfo() {
        Log.v("MainActivity_getMy_name", MainActivity.getMy_name());
        if (MainActivity.getMy_name() != null && !TextUtils.equals(MainActivity.getMy_name(), "")) {
            userName.setText(MainActivity.getMy_name());
            unloginMessage.setVisibility(View.GONE);
            loginMessage.setVisibility(View.VISIBLE);
        } else {
            unloginMessage.setVisibility(View.VISIBLE);
            loginMessage.setVisibility(View.GONE);
        }
    }


}
