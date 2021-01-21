package com.youtube.sorcjc.billetero.ui.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.io.MyApiAdapter;
import com.youtube.sorcjc.billetero.io.TicketPreferences;
import com.youtube.sorcjc.billetero.model.Lottery;
import com.youtube.sorcjc.billetero.model.TicketPlay;
import com.youtube.sorcjc.billetero.model.User;
import com.youtube.sorcjc.billetero.ui.adapter.TicketPlayAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private EditText etNumber, etPoints;
    private TextView tvSelectedLotteries;

    private RecyclerView mRecyclerView;
    private TicketPlayAdapter mAdapter;

    private AppCompatSpinner mSpinnerTypes;

    private ArrayList<Lottery> mLotteries;

    private static final ArrayList<TicketPlay> ticketPlays = new ArrayList<>();

    final String[] items = {"Lotería 1", "Lotería 2", "Lotería 3", "Lotería 4", "Lotería 5"};
    final ArrayList<Integer> mSelectedLotteries = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton btnSave = view.findViewById(R.id.ibSave);
        btnSave.setOnClickListener(this);

        ImageButton btnAdd = view.findViewById(R.id.ibAddTicketPlay);
        btnAdd.setOnClickListener(this);

        ImageButton btnClear = view.findViewById(R.id.ibClear);
        btnClear.setOnClickListener(this);

        etNumber = view.findViewById(R.id.etNumber);
        etPoints = view.findViewById(R.id.etPoints);
        tvSelectedLotteries = view.findViewById(R.id.tvSelectedLotteries);

        setupSelectLotteries(view);
        setupRecyclerView(view);
        populateSpinnerTypes(view);

        return  view;
    }

    private void setupSelectLotteries(View view) {
        ImageButton btnSelectLotteries = view.findViewById(R.id.btnSelectLotteries);

        btnSelectLotteries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCheckboxesDialog();
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadLotteries();
    }

    private static final String TAG = HomeFragment.class.getSimpleName();

    private void loadLotteries() {
        MyApiAdapter.getApiService()
                .getLotteries(User.getAuthHeader(getContext()))
                .enqueue(new Callback<ArrayList<Lottery>>() {

            @Override
            public void onResponse(@NonNull Call<ArrayList<Lottery>> call, @NonNull Response<ArrayList<Lottery>> response) {
                if (response.isSuccessful()) {
                    mLotteries = response.body();
                    if (mLotteries == null) return;

                    Log.d(TAG, "We got (" + mLotteries.size() + ") lotteries");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Lottery>> call, @NonNull Throwable t) {
                Log.d(TAG, t.getLocalizedMessage());
            }
        });
    }

    private void createCheckboxesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Loterías donde se registrarán las jugadas:");

        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId, boolean isSelected) {

                        if (isSelected) {
                            mSelectedLotteries.add(selectedItemId);
                        } else if (mSelectedLotteries.contains(selectedItemId)) {
                            mSelectedLotteries.remove(Integer.valueOf(selectedItemId));
                        }
                    }
                })
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        updateSelectedLotteries();
                    }
                })
                .setNegativeButton("Cancelar", null);

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void updateSelectedLotteries() {
        Log.d(TAG, "updateSelectedLotteries()");

        if (mSelectedLotteries.isEmpty()) {
            tvSelectedLotteries.setText(getString(R.string.no_selected_lotteries));
            return;
        }

        StringBuilder lotteriesList = new StringBuilder();

        for (int i = 0; i < mSelectedLotteries.size(); i++) {
            final int lotteryPosition = mSelectedLotteries.get(i);
            final String lotteryAbbreviated = mLotteries.get(lotteryPosition).getAbbreviated();
            lotteriesList.append(lotteryAbbreviated);
        }

        tvSelectedLotteries.setText(getString(R.string.selected_lotteries_label, lotteriesList.toString()));
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
        final int clickedId = view.getId();

        if (clickedId == R.id.ibSave) {
            save();
        } else if (clickedId == R.id.ibAddTicketPlay) {
            addPlay();
        } else if (clickedId == R.id.ibClear) {
            clear();
        }
    }

    private void clear() {
        mAdapter.clear();
        clearEditTexts();
    }

    private void addPlay() {
        final int number = Integer.parseInt(etNumber.getText().toString());
        final int points = Integer.parseInt(etPoints.getText().toString());
        final String type = mSpinnerTypes.getSelectedItem().toString();

        TicketPlay ticketPlay = new TicketPlay(number, points, type);
        mAdapter.addPlay(ticketPlay);

        clearEditTexts();
    }

    private void clearEditTexts() {
        etNumber.setText("");
        etPoints.setText("");
    }

    private void save() {
        final String ticketNumberString = etNumber.getText().toString();
        final String quantityString = etPoints.getText().toString();

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

        etNumber.setText("");
        etPoints.setText("");
    }
}
