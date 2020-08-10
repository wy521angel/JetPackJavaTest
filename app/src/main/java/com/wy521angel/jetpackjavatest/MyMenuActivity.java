package com.wy521angel.jetpackjavatest;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.wy521angel.jetpackjavatest.livedata.LiveDataActivity;

public class MyMenuActivity extends ListActivity {
    private String[] labels = new String[]{"LiveData封装传值"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                labels);
        getListView().setAdapter(adapter);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch (position) {
            case 0:
                startActivity(new Intent(this, LiveDataActivity.class));
                break;
        }
    }
}