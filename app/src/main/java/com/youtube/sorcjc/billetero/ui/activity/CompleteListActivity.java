package com.youtube.sorcjc.billetero.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.ui.adapter.CompleteListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CompleteListActivity extends AppCompatActivity {

    private CompleteListAdapter completeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayout);

        completeListAdapter = new CompleteListAdapter();
        recyclerView.setAdapter(completeListAdapter);

        ArrayList<Integer> ticketsSold = new ArrayList<>();
        int totalSold = 0;
        for (int i=0; i<=99; ++i) {
            final int timesSold = getIntent().getExtras().getInt(String.valueOf(i));
            ticketsSold.add(timesSold);
            totalSold += timesSold;
        }

        completeListAdapter.setDataSet(ticketsSold);

        // meta-data in top
        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        final String name = Global.getStringFromPrefs(this, "name");
        tvUsername.setText(name + " | Total: " + totalSold);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US);
        Date date = new Date();

        TextView tvDateTime = (TextView) findViewById(R.id.tvDateTime);
        tvDateTime.setText(sdf.format(date));
    }
}
