package com.youtube.sorcjc.billetero.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.io.MyApiAdapter;
import com.youtube.sorcjc.billetero.io.response.EarningsResponse;
import com.youtube.sorcjc.billetero.model.User;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarningsFragment extends Fragment {

    private EditText etTotalIncome, etCommission, etTotalToPay;

    public EarningsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_earnings, container, false);

        // edit text references
        etTotalIncome = view.findViewById(R.id.etTotalIncome);
        etCommission = view.findViewById(R.id.etCommission); // revenue
        etTotalToPay = view.findViewById(R.id.etTotalToPay);

        fetchValues();

        // edit-text not editable
        etTotalIncome.setKeyListener(null);
        etCommission.setKeyListener(null);
        etTotalToPay.setKeyListener(null);

        return view;
    }

    private void fetchValues() {
        final String authHeader = User.getAuthHeader(getContext());

        MyApiAdapter.getApiService().getEarnings(authHeader).enqueue(new Callback<EarningsResponse>() {
            @Override
            public void onResponse(@NonNull Call<EarningsResponse> call, @NonNull Response<EarningsResponse> response) {
                if (response.isSuccessful()) {
                    EarningsResponse earningsResponse = response.body();

                    EarningsResponse.Total total = earningsResponse.getTotal();
                    displayValues(total.getIncome(), total.getCommission());
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

    private void displayValues(float income, float commissionRevenue) {
        etTotalIncome.setText(String.format(Locale.US, "%.2f", income));
        etCommission.setText(String.format(Locale.US, "%.2f", commissionRevenue));

        final float totalToPay = income - commissionRevenue;
        etTotalToPay.setText(String.format(Locale.US, "%.2f", totalToPay));
    }

    private void showErrorEarningsDialog(String errorMessage) {
        Global.showMessageDialog(getContext(), "Error inesperado", errorMessage);
    }
}
