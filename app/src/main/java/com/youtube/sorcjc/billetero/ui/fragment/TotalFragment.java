package com.youtube.sorcjc.billetero.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.io.TicketPreferences;

import java.util.Locale;

public class TotalFragment extends Fragment implements View.OnClickListener {

    private EditText etTimesLimit;
    private EditText etTotalRevenue, etCommissionRevenue, etCommissionAmount, etTotalToPay;

    public TotalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_total, container, false);

        etTimesLimit = view.findViewById(R.id.etTimesLimit);
        TicketPreferences ticketPreferences = new TicketPreferences(getActivity());
        etTimesLimit.setText(String.valueOf(ticketPreferences.getLimitTimes()));

        ImageButton btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        // edit text references
        etTotalRevenue = view.findViewById(R.id.etTotalRevenue);
        etCommissionRevenue = view.findViewById(R.id.etCommissionRevenue);
        etCommissionAmount = view.findViewById(R.id.etCommissionAmount);
        etTotalToPay = view.findViewById(R.id.etTotalToPay);

        performCalculations();

        // edit-text not editable
        etTotalRevenue.setKeyListener(null);
        etCommissionRevenue.setKeyListener(null);
        etCommissionAmount.setKeyListener(null);
        etTotalToPay.setKeyListener(null);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSave) {


            final String timesLimitString = etTimesLimit.getText().toString();
            if (timesLimitString.isEmpty()) {
                Global.showMessageDialog(getContext(), "No se pudo guardar", "Ingrese una nueva cantidad límite.");
                return;
            }

            TicketPreferences ticketPreferences = new TicketPreferences(getActivity());
            final int timesLimit = Integer.parseInt(timesLimitString);
            ticketPreferences.setLimitTimes(timesLimit);

            // update total values in edit texts
            performCalculations();
        }
    }

    private void performCalculations() {
        TicketPreferences ticketPreferences = new TicketPreferences(getActivity());
        final int limitTimes = ticketPreferences.getLimitTimes();

        // total revenue = total times sold * 0.20 cents
        int totalSold = 0;
        int commissionableSold = 0;
        for (int i=0; i<= 99; ++i) {
            final int ticketTimesSold = ticketPreferences.getTotalSold(i);
            totalSold += ticketTimesSold;

            // int surplus = 0;
            if (ticketTimesSold > limitTimes) {
                // surplus = ticketTimesSold - limitTimes;
                commissionableSold += limitTimes;
            } else {
                commissionableSold += ticketTimesSold;
            }
        }

        final float totalRevenue = totalSold * 0.20f;

        // commission-able revenue = commission-able times sold * 0.20 cents
        float commissionRevenue = commissionableSold * 0.20f;
        commissionRevenue = Math.round(commissionRevenue / 0.05f) * 0.05f;

        // commission amount = 0.13 * commission-able revenue
        float commissionAmount = 0.13f * commissionRevenue;
        commissionAmount = Math.round(commissionAmount / 0.05f) * 0.05f;

        // total to pay = total revenue - commission amount
        float totalToPay = totalRevenue - commissionAmount;
        totalToPay = Math.round(totalToPay / 0.05f) * 0.05f;

        // display values
        etTotalRevenue.setText(String.format(Locale.US, "%.2f", totalRevenue));
        etCommissionRevenue.setText(String.format(Locale.US, "%.2f", commissionRevenue));
        etCommissionAmount.setText(String.format(Locale.US, "%.2f", commissionAmount));
        etTotalToPay.setText(String.format(Locale.US, "%.2f", totalToPay));
    }
}
