package com.example.administrator.mapther;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ToolFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToolFragment extends Fragment {

    private static final String STRING = "";
    private String string;
    private Button tool_button1;
    private Button tool_button2;


    public ToolFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param content Parameter 1.
     * @return A new instance of fragment ToolFragment.
     */
    public static ToolFragment newInstance(String content) {
        ToolFragment fragment = new ToolFragment();
        Bundle args = new Bundle();
        args.putString(STRING, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tool, container, false);
        if (getArguments() != null) {
            string = getArguments().getString(STRING);
        }

        tool_button1 = (Button) view.findViewById(R.id.tool_button1);
        tool_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                startActivity(intent);
            }
        });

        tool_button2 = (Button) view.findViewById(R.id.tool_button2);
        tool_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeliverActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
