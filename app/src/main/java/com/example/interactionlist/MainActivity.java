package com.example.interactionlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<Map<String, String>> values;
    private BaseAdapter listContentAdapter;
    public static final String APP_PREFERENCES_TEXT = "largeText";
    public static final String APP_PREFERENCES_KEY = "large_text";
    SharedPreferences mSettings;
    String[] arrayContent;
    private SwipeRefreshLayout mySwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettings = getSharedPreferences(APP_PREFERENCES_TEXT, MODE_PRIVATE);
        mySwipeLayout = findViewById(R.id.swipe_layout);
        if(!mSettings.contains(APP_PREFERENCES_KEY)) {
            SharedPreferences.Editor myEditor = mSettings.edit();
            myEditor.putString(APP_PREFERENCES_KEY, getString(R.string.large_text));
            myEditor.apply();

        }
        createAdapter();
        interactionList();
        mySwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createAdapter();
                interactionList();
                mySwipeLayout.setRefreshing(false);
            }
        });
    }

    private void interactionList() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                values.remove(position);
                listContentAdapter.notifyDataSetChanged();
            }
        });
    }

    private void createAdapter() {
        values = prepareContent();
        listView = findViewById(R.id.list);
        listContentAdapter = new SimpleAdapter(this,
                values,
                R.layout.my_simple_list_item,
                new String[]{"title", "subtitle"},
                new int[]{R.id.titleView, R.id.subtitleView}
        );
        listView.setAdapter(listContentAdapter);
    }

    private List<Map<String, String>> prepareContent() {
        List<Map<String, String>> simpleAdapterContent = new ArrayList<>();
        arrayContent = mSettings.getString(APP_PREFERENCES_KEY,"").split("\n\n");
     for (String s : arrayContent) {
            Map<String, String> myMap = new HashMap<>();
            myMap.put("title", s);
            myMap.put("subtitle", String.valueOf(s.length()));
            simpleAdapterContent.add(myMap);
        }
        return simpleAdapterContent;
    }
}
