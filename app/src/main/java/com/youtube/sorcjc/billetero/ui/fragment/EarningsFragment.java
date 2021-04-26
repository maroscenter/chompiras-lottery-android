package com.youtube.sorcjc.billetero.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.io.MyApiAdapter;
import com.youtube.sorcjc.billetero.io.response.EarningsResponse;
import com.youtube.sorcjc.billetero.io.response.SoldTicketsResponse;
import com.youtube.sorcjc.billetero.model.BalanceMovement;
import com.youtube.sorcjc.billetero.model.User;
import com.youtube.sorcjc.billetero.ui.adapter.BalanceMovementAdapter;
import com.youtube.sorcjc.billetero.ui.adapter.TicketAdapter;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarningsFragment extends Fragment {

    private TextView tvTotalIncome, tvCommission, tvTotalToPay, tvBalance;

    private BalanceMovementAdapter mMovementAdapter;
    private ProgressBar mProgressBar;
    private NestedScrollView mScrollList;


    public EarningsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_earnings, container, false);

        // edit text references
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        tvCommission = view.findViewById(R.id.tvCommission); // revenue
        tvTotalToPay = view.findViewById(R.id.tvTotalToPay);
        tvBalance = view.findViewById(R.id.tvBalance);

        fetchValues();

        // edit-text not editable
        tvTotalIncome.setKeyListener(null);
        tvCommission.setKeyListener(null);
        tvTotalToPay.setKeyListener(null);

        mScrollList = view.findViewById(R.id.scrollList);
        mProgressBar = view.findViewById(R.id.progressBar);
        setupRecyclerView(view);

        setupButtons(view);

        return view;
    }

    private void setupButtons(View view) {
        final Button btnShow = view.findViewById(R.id.btnShowTotals);
        btnShow.setOnClickListener(view1 -> {
            btnShow.setVisibility(View.GONE);

            view.findViewById(R.id.llTotalIncome).setVisibility(View.VISIBLE);
            view.findViewById(R.id.llCommission).setVisibility(View.VISIBLE);
            view.findViewById(R.id.llTotalToPay).setVisibility(View.VISIBLE);
        });
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);

        mMovementAdapter = new BalanceMovementAdapter();
        recyclerView.setAdapter(mMovementAdapter);

        recyclerView.setNestedScrollingEnabled(false);
    }

    private void fetchValues() {
        final String authHeader = User.getAuthHeader(getContext());

        getTotals(authHeader);
        getBalanceMovements(authHeader);
    }

    private void getTotals(String authHeader) {
        MyApiAdapter.getApiService().getEarnings(authHeader).enqueue(new Callback<EarningsResponse>() {
            @Override
            public void onResponse(@NonNull Call<EarningsResponse> call, @NonNull Response<EarningsResponse> response) {
                if (response.isSuccessful()) {
                    EarningsResponse earningsResponse = response.body();

                    EarningsResponse.Total total = earningsResponse.getTotal();
                    displayValues(total.getIncome(), total.getCommission(), total.getBalance());
                } else {
                    showErrorEarningsDialog(getString(R.string.error_fetch_earnings));
                }
            }

            @Override
            public void onFailure(@NonNull Call<EarningsResponse> call, @NonNull Throwable t) {
                showErrorEarningsDialog(t.getLocalizedMessage());
            }
        });
    }

    private void getBalanceMovements(String authHeader) {
        MyApiAdapter.getApiService().getBalanceMovements(authHeader).enqueue(new Callback<ArrayList<BalanceMovement>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<BalanceMovement>> call, @NonNull Response<ArrayList<BalanceMovement>> response) {
                if (response.isSuccessful()) {
                    ArrayList<BalanceMovement> movements = response.body();
                    displayMovements(movements);
                } else {
                    showErrorEarningsDialog(getString(R.string.error_fetch_balance_movements));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<BalanceMovement>> call, @NonNull Throwable t) {
                showErrorEarningsDialog(t.getLocalizedMessage());
            }
        });
    }

    private void displayMovements(ArrayList<BalanceMovement> movements) {
        mMovementAdapter.setDataSet(movements);

        mProgressBar.setVisibility(View.GONE);
        mScrollList.setVisibility(View.VISIBLE);
    }

    private void displayValues(float income, float commissionRevenue, float balance) {
        tvTotalIncome.setText(String.format(Locale.US, "%.2f", income));
        tvCommission.setText(String.format(Locale.US, "%.2f", commissionRevenue));

        final float totalToPay = income - commissionRevenue;
        tvTotalToPay.setText(String.format(Locale.US, "%.2f", totalToPay));

        tvBalance.setText(String.format(Locale.US, "%.2f", balance));
    }

    private void showErrorEarningsDialog(String errorMessage) {
        Global.showMessageDialog(getContext(), "Error inesperado", errorMessage);
    }
}
