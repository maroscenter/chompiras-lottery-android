package com.youtube.sorcjc.billetero.ui.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.io.MyApiAdapter;
import com.youtube.sorcjc.billetero.io.TicketPreferences;
import com.youtube.sorcjc.billetero.io.response.SimpleResponse;
import com.youtube.sorcjc.billetero.model.Ticket;
import com.youtube.sorcjc.billetero.ui.activity.CompleteListActivity;
import com.youtube.sorcjc.billetero.ui.adapter.TicketAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragment extends Fragment implements View.OnClickListener {

    private NestedScrollView scrollView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout layoutButtons;

    private TicketAdapter ticketAdapter;
    private ArrayList<Ticket> ticketsSold;

    private ProgressDialog progressDialog;

    private Button btnSend;
    private ImageButton ibUndo, ibViewList;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        scrollView = view.findViewById(R.id.scrollList);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);

        ticketAdapter = new TicketAdapter();
        recyclerView.setAdapter(ticketAdapter);

        recyclerView.setNestedScrollingEnabled(false);

        progressBar = view.findViewById(R.id.progressBar);
        layoutButtons = view.findViewById(R.id.layoutButtons);

        // buttons
        btnSend = view.findViewById(R.id.btnSend);
        ibUndo = view.findViewById(R.id.ibUndo);
        ibViewList = view.findViewById(R.id.ibViewList);
        // button events
        btnSend.setOnClickListener(this);
        ibUndo.setOnClickListener(this);
        ibViewList.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadTicketsSoldList();
    }

    private void loadTicketsSoldList() {
        new ReadListOperation().execute("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                confirmToSend();
                break;

            case R.id.ibUndo:
                TicketPreferences ticketPreferences = new TicketPreferences(getActivity());
                ticketPreferences.undoLastOperation(ticketAdapter);
                break;

            case R.id.ibViewList:
                Intent mIntent = new Intent(getContext(), CompleteListActivity.class);
                for (Ticket ticket : ticketsSold) {
                    mIntent.putExtra(String.valueOf(ticket.getTicketNumber()), ticket.getTotalSold());
                }
                startActivity(mIntent);
                break;
        }
    }

    private void confirmToSend() {

        Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int min = c.get(Calendar.MINUTE);

        String nextHour; // 13:40hr ó 21:00hr

        // between 13:40 and before 21:00 show 21hr, in other cases, 13:40hr
        if (hour==13&&min>=40 || hour>=14&&hour<=23)
            nextHour = "el día de mañana.";
        else
            nextHour = "las 13:40 horas.";

        final String message =
                "¿Seguro que desea enviar? Recuerde que los datos serán enviados y su lista se reiniciará. Además no podrá volver a enviar hasta "+nextHour;

        Global.showConfirmationDialog(
                getContext(),
                "Confirmar envío", message,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendToServer();
            }
        }
        );
    }
    private void sendToServer() {
        progressDialog = ProgressDialog.show(getActivity(), "Enviando",
                "Espere un momento mientras la lista se termina de enviar.", true);

        ArrayList<Integer> ticketsIntSold = new ArrayList<>();
        for (Ticket ticket : ticketsSold) {
            ticketsIntSold.add(ticket.getTotalSold());
        }

        final int user_id = Global.getIntFromGlobalPreferences(getActivity(), "user_id");

        Call<SimpleResponse> call = MyApiAdapter.getApiService().postSentList(ticketsIntSold, user_id);
        call.enqueue(new SentListCallback());
    }

    private class SentListCallback implements Callback<SimpleResponse> {

        @Override
        public void onResponse(@NonNull Call<SimpleResponse> call, Response<SimpleResponse> response) {
            if (response.isSuccessful()) {
                SimpleResponse simpleResponse = response.body();
                progressDialog.dismiss();

                if (simpleResponse.isError()) {
                    Global.showMessageDialog(getContext(), "Mensaje de error", simpleResponse.getMessage());
                } else {
                    Global.showMessageDialog(getContext(), "Operación exitosa", "El servidor ha recibido la lista enviada.");
                    clearListAndRecyclerView();
                }
            } else {
                progressDialog.dismiss();
                Global.showMessageDialog(getContext(), "Mensaje de error", "Ha ocurrido un error inesperado.");
            }
        }

        @Override
        public void onFailure(@NonNull Call<SimpleResponse> call, Throwable t) {
            progressDialog.dismiss();
            Global.showMessageDialog(getContext(), "Mensaje de error", t.getMessage());
        }
    }

    public void clearListAndRecyclerView() {
        new TicketPreferences(getActivity()).clearList(ticketAdapter);
    }

    private class ReadListOperation extends AsyncTask<String, Ticket, Boolean> {

        @Override
        protected void onPreExecute() {}

        @Override
        protected Boolean doInBackground(String... params) {
            ticketsSold = new ArrayList<>();

            TicketPreferences ticketPreferences = new TicketPreferences(getActivity());

            for (int i=0; i<=99; ++i) {
                Ticket ticket = ticketPreferences.getTicketAt(i);
                ticketsSold.add(ticket);
                publishProgress(ticket);

                int milliseconds;
                if (i<=36 && i%2==0) {
                    milliseconds = 50;
                    try {
                        Thread.sleep(milliseconds);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Ticket... tickets) {
            ticketAdapter.addItem(tickets[0]);

            if (ticketsSold.size() < 5) // sometimes the scrollview doesn't appear
                scrollView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // hide the progress bar and show the list when it's ready
                progressBar.setVisibility(View.GONE);

                // show the options
                layoutButtons.setVisibility(View.VISIBLE);
            }
        }

    }
}
