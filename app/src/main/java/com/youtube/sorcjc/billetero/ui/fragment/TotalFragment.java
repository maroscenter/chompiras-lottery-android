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

        // TicketPreferences ticketPreferences = new TicketPreferences(getActivity());
        // etTimesLimit.setText(String.valueOf(ticketPreferences.getLimitTimes()));

        ImageButton btnSave = view.findViewById(R.id.ibSave);
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
        if (view.getId() == R.id.ibSave) {


            final String timesLimitString = etTimesLimit.getText().toString();

            if (timesLimitString.isEmpty()) {
                Global.showMessageDialog(getContext(), "No se pudo guardar", "Ingrese una nueva cantidad límite.");
                return;
            }

            final int timesLimit = Integer.parseInt(timesLimitString);

            // TicketPreferences ticketPreferences = new TicketPreferences(getActivity());
            // ticketPreferences.setLimitTimes(timesLimit);

            // update total values in edit texts
            performCalculations();
        }
    }

    private void performCalculations() {
        // display values
        /*
        etTotalRevenue.setText(String.format(Locale.US, "%.2f", totalRevenue));
        etCommissionRevenue.setText(String.format(Locale.US, "%.2f", commissionRevenue));
        etCommissionAmount.setText(String.format(Locale.US, "%.2f", commissionAmount));
        etTotalToPay.setText(String.format(Locale.US, "%.2f", totalToPay));*/
    }
}
