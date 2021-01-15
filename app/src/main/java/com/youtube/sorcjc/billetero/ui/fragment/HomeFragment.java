package com.youtube.sorcjc.billetero.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.io.TicketPreferences;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button btnSave;
    private EditText etTicket, etQuantity;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        etTicket = view.findViewById(R.id.etTicket);
        etQuantity = view.findViewById(R.id.etQuantity);

        return  view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSave) {

            final String ticketNumberString = etTicket.getText().toString();
            final String quantityString = etQuantity.getText().toString();

            if (ticketNumberString.isEmpty() || quantityString.isEmpty()) {
                Global.showMessageDialog(getContext(), "No se pudo guardar", "Asegúrese de ingresar un valor para cada campo.");
                return;
            }

            final int ticketNumber = Integer.parseInt(ticketNumberString);
            final int quantity = Integer.parseInt(quantityString);

            if (ticketNumber<0 || ticketNumber>99) {
                Global.showMessageDialog(getContext(), "No se pudo guardar", "Ingrese una cifra entre 0 y 99.");
                return;
            }

            if (quantity<=0) {
                Global.showMessageDialog(getContext(), "No se pudo guardar", "Ingrese una cantidad de tiempos válida.");
                return;
            }

            TicketPreferences ticketPreferences = new TicketPreferences(getActivity());
            ticketPreferences.addTotalSold(ticketNumber, quantity);
            ticketPreferences.setLastOperation(ticketNumber, quantity);

            final String message = "Se registraron " + quantityString + " tiempos más para la cifra " + ticketNumberString + ".";
            Global.showMessageDialog(getContext(), "Registro exitoso", message);

            etTicket.setText("");
            etQuantity.setText("");
        }
    }
}
