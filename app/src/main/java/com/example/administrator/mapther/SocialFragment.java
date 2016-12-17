package com.example.administrator.mapther;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.mapther.MainActivity.getMy_name;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialFragment extends Fragment {

    private static final String STRING = "";
    private String string;
    private FloatingActionButton floatingActionButton;
    private ViewPager viewPager;
    private String[] imageDescription = { "珠江花城", "《名叫海贼的男人》",
            "最新耳机", "好喝的果汁", "猩猩都爱穿的皮鞋" };
    private int[] imageView_id = {R.mipmap.adv1, R.mipmap.adv2, R.mipmap.adv3, R.mipmap.adv4, R.mipmap.adv5};
    // 圆圈标志位
    private int pointIndex = 0;
    // 线程标志
    private boolean isStop = false;
    // 布局控件
    private LinearLayout show_pointer;
    private TextView image_description;
    private ListView post_listview;
    private View sub_view;
    private EditText add_content;
    private EditText add_phone;
    private EditText edit_content;
    private EditText edit_phone;

    private String my_name = null;
    private Thread thread;
    private List<ImageView> imageViews_list;
    private BannerListener bannerListener;
    private List<Post> postList;
    private PostAdapter postAdapter;
    private PostsDB postsDB;
    private MyPagerAdapter mAdapter;
    private Post temp;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    };

    public SocialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param content Parameter 1.
     * @return A new instance of fragment SocialFragment.
     */
    public static SocialFragment newInstance(String content) {
        SocialFragment fragment = new SocialFragment();
        Bundle args = new Bundle();
        args.putString(STRING, content);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_social, container, false);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        show_pointer = (LinearLayout) view.findViewById(R.id.show_pointer);
        image_description = (TextView) view.findViewById(R.id.image_description);
        post_listview = (ListView) view.findViewById(R.id.post_listview);

        if (getArguments() != null) {
            string = getArguments().getString(STRING);
        }

        // 初始化数据
        initData();
        initAction();
        my_name = getMy_name();
        postsDB = new PostsDB(getActivity(), "", null, 0);
        init();
        postAdapter = new PostAdapter(getActivity(), postList);
        post_listview.setAdapter(postAdapter);

        // 开启新线程，2秒一次更新Banner
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                isStop = false;
                while (!isStop) {
                    SystemClock.sleep(3000);
                    handler.sendMessage(new Message());
                }
            }
        });
        thread.start();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.getMy_name() != null && MainActivity.getMy_name() != "") {
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    sub_view = layoutInflater.inflate(R.layout.add_dialog, null);
                    add_content = (EditText) sub_view.findViewById(R.id.add_content);
                    add_phone = (EditText) sub_view.findViewById(R.id.add_phone);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("新建帖子");
                    builder.setView(sub_view);
                    builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String content = add_content.getText().toString();
                            String phone = add_phone.getText().toString();
                            postsDB.insert2DB(my_name, content, phone);
                            notifyPostList();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();
                } else {
                    Toast.makeText(getActivity(), "登录后才可发帖", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 帖子列表短按显示详情
        post_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                temp = postList.get(position);
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                sub_view = layoutInflater.inflate(R.layout.show_dialog, null);
                ((TextView) sub_view.findViewById(R.id.show_name)).setText(temp.getUser_name());
                ((TextView) sub_view.findViewById(R.id.show_content)).setText(temp.getContent());
                ((TextView) sub_view.findViewById(R.id.show_content)).setMovementMethod(ScrollingMovementMethod.getInstance());
                ((TextView) sub_view.findViewById(R.id.show_phone)).setText(temp.getPhone());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("帖子详情");
                builder.setView(sub_view);
                builder.setPositiveButton("确定", null);
                builder.create().show();
            }
        });

        // 帖子列表长按修改
        post_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                temp = postList.get(position);
                if (TextUtils.equals(temp.getUser_name(), my_name)) {

                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    sub_view = layoutInflater.inflate(R.layout.edit_dialog, null);
                    ((TextView) sub_view.findViewById(R.id.edit_name)).setText(temp.getUser_name());
                    edit_content = (EditText) sub_view.findViewById(R.id.edit_content);
                    edit_content.setText(temp.getContent());
                    edit_content.setMovementMethod(ScrollingMovementMethod.getInstance());
                    edit_content.setSelection(temp.getContent().length());
                    edit_phone = (EditText) sub_view.findViewById(R.id.edit_phone);
                    edit_phone.setText(temp.getPhone());
                    edit_phone.setSelection(temp.getPhone().length());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("修改帖子");
                    builder.setView(sub_view);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String content = edit_content.getText().toString();
                            String phone = edit_phone.getText().toString();
                            postsDB.updateById(temp.getPost_id(), content, phone);
                            notifyPostList();
                        }
                    });
                    builder.setNegativeButton("否", null);
                    builder.create().show();
                } else {
                    Toast.makeText(getActivity(), "只有本人才能够修改帖子", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        // 帖子列表左滑删除
        post_listview.setOnTouchListener(new SwipeDismissListViewTouchListener(
                post_listview,
                new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        temp = postList.get(reverseSortedPositions[0]);
                        if (TextUtils.equals(temp.getUser_name(), my_name)
                                || TextUtils.equals(my_name, MainActivity.getSuper_admin())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("是否删除？");
                            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    postsDB.deleteById(temp.getPost_id());
                                    notifyPostList();
                                }
                            });
                            builder.setNegativeButton("否", null);
                            builder.create().show();
                        } else {
                            Toast.makeText(getActivity(), "只有本人或是管理员账户才能够删除帖子", Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

        return view;
    }

    /**
     * 初始化事件
     */
    private void initAction() {
        bannerListener = new BannerListener();
        viewPager.setOnPageChangeListener(bannerListener);
        //取中间数来作为起始位置
        int index = (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2 % imageViews_list.size());
        //用来出发监听器
        viewPager.setCurrentItem(index);
        show_pointer.getChildAt(pointIndex).setEnabled(true);
    }

    /**
     * 初始化数据
     * 动态设置UI界面
     */
    private void initData() {
        imageViews_list = new ArrayList<ImageView>();
        View view;
        LayoutParams params;
        for (int i = 0; i < imageView_id.length; i++) {
            // 设置广告图
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            imageView.setBackgroundResource(imageView_id[i]);
            imageViews_list.add(imageView);
            // 设置圆圈点
            view = new View(getActivity());
            params = new LayoutParams(15, 15);
            params.leftMargin = 10;
            view.setBackgroundResource(R.drawable.pointer_selector);
            view.setLayoutParams(params);
            view.setEnabled(false);

            show_pointer.addView(view);
        }
        mAdapter = new MyPagerAdapter(imageViews_list);
        viewPager.setAdapter(mAdapter);
    }

    //实现VierPager监听器接口
    class BannerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case 0:
                    Log.v("PageScrollStateChanged", "Idle.");
                    break;
                case 1:
                    Log.v("PageScrollStateChanged", "Dragging.");
                    break;
                case 2:
                    Log.v("PageScrollStateChanged", "Settling.");
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            int newPosition = position % imageView_id.length;
            image_description.setText(imageDescription[newPosition]);
            show_pointer.getChildAt(newPosition).setEnabled(true);
            show_pointer.getChildAt(pointIndex).setEnabled(false);
            // 更新标志位
            pointIndex = newPosition;

        }

    }

    @Override
    public void onDestroyView() {
        // 关闭定时器
        isStop = true;
        postsDB.close();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.v("TAG", "destroy");
        super.onDestroy();
    }

    private void notifyPostList() {
        Cursor cursor = postsDB.getAll();
        List<Post> newList = new ArrayList<>();
        if (cursor != null && cursor.getCount() >= 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                int id_temp = cursor.getInt(0);
                String name_temp = cursor.getString(cursor.getColumnIndex("user_name"));
                String content_temp = cursor.getString(cursor.getColumnIndex("content"));
                String phone_temp = cursor.getString(cursor.getColumnIndex("phone"));
                Post temp = new Post(id_temp, name_temp, content_temp, phone_temp);
                newList.add(temp);
                cursor.moveToNext();
            }
        }
        cursor.close();
        postList.clear();
        postList.addAll(newList);
        postAdapter = new PostAdapter(getActivity(), postList);
        post_listview.setAdapter(postAdapter);
    }

    private void init() {
        Cursor cursor = postsDB.getAll();
        postList = new ArrayList<>();
        if (cursor != null && cursor.getCount() >= 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                int id_temp = cursor.getInt(0);
                String name_temp = cursor.getString(cursor.getColumnIndex("user_name"));
                String content_temp = cursor.getString(cursor.getColumnIndex("content"));
                String phone_temp = cursor.getString(cursor.getColumnIndex("phone"));
                Post temp = new Post(id_temp, name_temp, content_temp, phone_temp);
                postList.add(temp);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

}
