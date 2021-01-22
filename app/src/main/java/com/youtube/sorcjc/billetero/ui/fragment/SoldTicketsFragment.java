package com.youtube.sorcjc.billetero.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.io.MyApiAdapter;
import com.youtube.sorcjc.billetero.io.response.SoldTicketsResponse;
import com.youtube.sorcjc.billetero.model.Ticket;
import com.youtube.sorcjc.billetero.model.User;
import com.youtube.sorcjc.billetero.ui.adapter.TicketAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoldTicketsFragment extends Fragment {

    private TicketAdapter mTicketAdapter;

    // private ProgressBar progressBar;
    // private ProgressDialog progressDialog;

    public SoldTicketsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_sold_tickets, container, false);

        // NestedScrollView scrollView = view.findViewById(R.id.scrollList);

        setupRecyclerView(view);

        // progressBar = view.findViewById(R.id.progressBar);

        return view;
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);

        mTicketAdapter = new TicketAdapter();
        recyclerView.setAdapter(mTicketAdapter);

        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadTicketsSoldList();
    }

    private void loadTicketsSoldList() {
        final String authHeader = User.getAuthHeader(getContext());

        MyApiAdapter.getApiService().getTickets(authHeader).enqueue(new Callback<SoldTicketsResponse>() {
            @Override
            public void onResponse(@NonNull Call<SoldTicketsResponse> call, @NonNull Response<SoldTicketsResponse> response) {
                if (response.isSuccessful()) {
                    SoldTicketsResponse ticketsResponse = response.body();
                    mTicketAdapter.setDataSet(ticketsResponse.getTickets());
                } else {
                    showErrorTicketsDialog(getString(R.string.error_fetching_sold_tickets));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SoldTicketsResponse> call, @NonNull Throwable t) {
                showErrorTicketsDialog(t.getLocalizedMessage());
            }
        });
    }

    private void showErrorTicketsDialog(String errorMessage) {
        Global.showMessageDialog(getContext(), "Error inesperado", errorMessage);
    }
}
