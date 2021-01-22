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

    }

    @Override
    public void onClick(View view) {

    }

}
