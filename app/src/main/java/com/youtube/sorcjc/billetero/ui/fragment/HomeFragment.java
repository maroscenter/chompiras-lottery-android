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
import com.youtube.sorcjc.billetero.io.response.SimpleResponse;
import com.youtube.sorcjc.billetero.model.Lottery;
import com.youtube.sorcjc.billetero.model.TicketBody;
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

    private TicketPlayAdapter mAdapter;

    private AppCompatSpinner mSpinnerTypes;

    private ArrayList<Lottery> mLotteries = new ArrayList<>();

    private static final ArrayList<TicketPlay> ticketPlays = new ArrayList<>();

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

        btnSelectLotteries.setOnClickListener(view1 -> {
            mSelectedLotteries.clear();
            createCheckboxesDialog();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadLotteries();
    }

    private static final String TAG = HomeFragment.class.getSimpleName();

    private void loadLotteries() {
        // skip if already fetched
        if (!mLotteries.isEmpty()) {
            return;
        }

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

        builder.setMultiChoiceItems(getLotteryNamesAsArray(), null,
                (dialog, position, isSelected) -> {
                    // Global.showToast(getContext(), String.valueOf(position));
                    final Lottery selectedLottery = mLotteries.get(position);
                    final int selectedLotteryId = selectedLottery.getId();

                    final boolean containedId = mSelectedLotteries.contains(selectedLotteryId);

                    if (isSelected) {
                        if (!containedId)
                            mSelectedLotteries.add(selectedLotteryId);

                    } else if (containedId) {
                        // remove by object (value instead of id)
                        mSelectedLotteries.remove((Integer) selectedLotteryId);
                    }
                })
                .setPositiveButton("Confirmar", (dialog, id) -> updateSelectedLotteries())
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
            final int lotteryId = mSelectedLotteries.get(i);
            final Lottery lottery = findLotteryById(lotteryId);
            lotteriesList.append(lottery.getAbbreviated());
        }

        tvSelectedLotteries.setText(getString(R.string.selected_lotteries_label, lotteriesList.toString()));
    }

    private Lottery findLotteryById(int lotteryId) {
        for (Lottery lottery : mLotteries) {
            if (lottery.getId() == lotteryId) {
                return lottery;
            }
        }

        return null;
    }

    private void setupRecyclerView(View view) {
        RecyclerView mRecyclerView = view.findViewById(R.id.rvTicketDetails);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new TicketPlayAdapter(ticketPlays, true);
        mRecyclerView.setAdapter(mAdapter);

        // header: show last column
        view.findViewById(R.id.viewLastColumn).setVisibility(View.VISIBLE);
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
        final String number = etNumber.getText().toString();
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
        final String authHeader = User.getAuthHeader(getContext());
        final TicketBody ticketBody = new TicketBody(getLotteryIdsAsArray(), mAdapter.getPlays());

        MyApiAdapter.getApiService()
                .storeTicket(authHeader, ticketBody)
                .enqueue(new Callback<SimpleResponse>() {

            @Override
            public void onResponse(@NonNull Call<SimpleResponse> call, @NonNull Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    SimpleResponse simpleResponse = response.body();
                    if (simpleResponse.isSuccess()) {
                        showSuccessfulTicketDialog();
                    } else {
                        showErrorTicketDialog(simpleResponse.getErrorMessage());
                    }
                } else {
                    showErrorTicketDialog(response.raw().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SimpleResponse> call, @NonNull Throwable t) {
                showErrorTicketDialog(t.getLocalizedMessage());
            }
        });
    }

    private int[] getLotteryIdsAsArray() {
        final int[] lotteryIds = new int[mSelectedLotteries.size()];

        for (int i=0; i<mSelectedLotteries.size(); ++i) {
            lotteryIds[i] = mSelectedLotteries.get(i);
        }

        return lotteryIds;
    }

    private String[] getLotteryNamesAsArray() {
        final String[] names = new String[mLotteries.size()];

        for (int i=0; i<mLotteries.size(); ++i) {
            final Lottery lottery = mLotteries.get(i);
            names[i] = lottery.getName();
        }

        return names;
    }

    private void showSuccessfulTicketDialog() {
        Global.showMessageDialog(getContext(), "Operación exitosa", "El ticket se ha registrado correctamente.");
        clear();
    }

    private void showErrorTicketDialog(String errorMessage) {
        Global.showMessageDialog(getContext(), "Operación rechazada", errorMessage);
    }
}
