package com.youtube.sorcjc.billetero.ui.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.io.TicketPreferences;
import com.youtube.sorcjc.billetero.model.TicketPlay;
import com.youtube.sorcjc.billetero.ui.adapter.TicketPlayAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private ImageButton btnSave;
    private EditText etTicket, etQuantity;

    private RecyclerView mRecyclerView;
    private TicketPlayAdapter mAdapter;

    private AppCompatSpinner mSpinnerTypes;

    private static final ArrayList<TicketPlay> ticketPlays = new ArrayList<>();

    final String[] items = {"Lotería 1", "Lotería 2", "Lotería 3", "Lotería 4", "Lotería 5"};
    final ArrayList<Integer> itemsSelected = new ArrayList<>();

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

        ticketPlays.add(new TicketPlay(12, 4, "Quiniela"));
        ticketPlays.add(new TicketPlay(23, 2, "Pale"));

        ImageButton btnSelectLotteries = view.findViewById(R.id.btnSelectLotteries);
        btnSelectLotteries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCheckboxesDialog();
            }
        });

        setupRecyclerView(view);
        populateSpinnerTypes(view);

        return  view;
    }

    private void createCheckboxesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Loterías donde se comprarán los tickets:");

        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {

                            itemsSelected.add(selectedItemId);
                        } else if (itemsSelected.contains(selectedItemId)) {

                            itemsSelected.remove(Integer.valueOf(selectedItemId));
                        }
                    }
                })
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // OK button clicked
                    }
                })
                .setNegativeButton("Cancelar", null);

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void setupRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.rvTicketDetails);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new TicketPlayAdapter(ticketPlays);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void populateSpinnerTypes(View view) {
        mSpinnerTypes = view.findViewById(R.id.spinnerType);

        ArrayList<String> types = new ArrayList<>();
        types.add("Quiniela");
        types.add("Pale");
        types.add("Tripleta");

        ArrayAdapter<String> typesArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, types);
        typesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerTypes.setAdapter(typesArrayAdapter);
        mSpinnerTypes.setSelection(0);
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
