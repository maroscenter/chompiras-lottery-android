package com.youtube.sorcjc.billetero.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.io.TicketPreferences;
import com.youtube.sorcjc.billetero.model.Ticket;
import com.youtube.sorcjc.billetero.ui.adapter.TicketAdapter;

import java.util.ArrayList;

public class WinnersFragment extends Fragment implements View.OnClickListener {

    private NestedScrollView scrollView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private TicketAdapter ticketAdapter;
    private ArrayList<Ticket> ticketsSold;

    // private ProgressDialog progressDialog;

    public WinnersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_winners, container, false);

        scrollView = view.findViewById(R.id.scrollList);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);

        ticketAdapter = new TicketAdapter();
        recyclerView.setAdapter(ticketAdapter);

        recyclerView.setNestedScrollingEnabled(false);

        progressBar = view.findViewById(R.id.progressBar);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadTicketsSoldList();
    }

    private void loadTicketsSoldList() {
        ticketsSold = new ArrayList<>();

        TicketPreferences ticketPreferences = new TicketPreferences(getActivity());

        for (int i=0; i<=99; ++i) {
            Ticket ticket = ticketPreferences.getTicketAt(i);
            ticketsSold.add(ticket);

            int milliseconds;
            if (i<=36 && i%2==0) {
                milliseconds = 50;
                try {
                    Thread.sleep(milliseconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    private void sendToServer() {
        // Call<SimpleResponse> call = MyApiAdapter.getApiService().postSentList(ticketsIntSold, user_id);
        // call.enqueue(new SentListCallback());
    }

}
