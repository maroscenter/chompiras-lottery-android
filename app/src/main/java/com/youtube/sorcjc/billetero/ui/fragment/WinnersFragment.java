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

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.io.MyApiAdapter;
import com.youtube.sorcjc.billetero.model.User;
import com.youtube.sorcjc.billetero.model.Winner;
import com.youtube.sorcjc.billetero.ui.adapter.WinnerAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WinnersFragment extends Fragment {

    private NestedScrollView mScrollList;
    private ProgressBar mProgressBar;

    private WinnerAdapter mAdapter;

    // private ProgressDialog progressDialog;

    public WinnersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_winners, container, false);

        mScrollList = view.findViewById(R.id.scrollList);
        mProgressBar = view.findViewById(R.id.progressBar);

        setupRecyclerView(view);

        return view;
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);

        mAdapter = new WinnerAdapter();
        recyclerView.setAdapter(mAdapter);

        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadWinners();
    }

    private void loadWinners() {
        final String authHeader = User.getAuthHeader(getContext());

        MyApiAdapter.getApiService().getWinners(authHeader).enqueue(new Callback<ArrayList<Winner>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Winner>> call, @NonNull Response<ArrayList<Winner>> response) {
                if (response.isSuccessful()) {
                    displayWinners(response.body());
                } else {
                    showErrorWinnersDialog(getString(R.string.error_fetch_winners));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Winner>> call, @NonNull Throwable t) {
                showErrorWinnersDialog(t.getLocalizedMessage());
            }
        });
    }

    private void displayWinners(ArrayList<Winner> winners) {
        mAdapter.setDataSet(winners);

        mProgressBar.setVisibility(View.GONE);
        mScrollList.setVisibility(View.VISIBLE);
    }

    private void showErrorWinnersDialog(String errorMessage) {
        Global.showMessageDialog(getContext(), getString(R.string.error_unexpected), errorMessage);
    }
}
