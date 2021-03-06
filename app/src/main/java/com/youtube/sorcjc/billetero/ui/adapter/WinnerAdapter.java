package com.youtube.sorcjc.billetero.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.io.MyApiAdapter;
import com.youtube.sorcjc.billetero.io.response.SimpleResponse;
import com.youtube.sorcjc.billetero.model.TicketPlay;
import com.youtube.sorcjc.billetero.model.User;
import com.youtube.sorcjc.billetero.model.Winner;
import com.youtube.sorcjc.billetero.ui.activity.TicketActivity;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WinnerAdapter extends RecyclerView.Adapter<WinnerAdapter.ViewHolder> {
    private ArrayList<Winner> mDataSet;
    private static final String TAG = "WinnerAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvId, tvCreatedAt, tvLottery, tvPoints, tvType, tvNumber, tvReward, tvAlreadyPaid;
        public Button btnPay, btnShowTicket;

        private final Context mContext;

        public ViewHolder(View v) {
            super(v);

            mContext = v.getContext();

            tvId = v.findViewById(R.id.tvTickerId);
            tvCreatedAt = v.findViewById(R.id.tvCreatedAt);
            tvLottery = v.findViewById(R.id.tvLottery);
            tvPoints = v.findViewById(R.id.tvPoints);
            tvType = v.findViewById(R.id.tvType);
            tvNumber = v.findViewById(R.id.tvNumber);
            tvReward = v.findViewById(R.id.tvReward);
            tvAlreadyPaid = v.findViewById(R.id.tvTicketAlreadyPaid);

            btnPay = v.findViewById(R.id.btnPayWinner);
            btnShowTicket = v.findViewById(R.id.btnShowTicket);
        }

        public void setupButtonShowTicket(final int ticketId) {
            btnShowTicket.setOnClickListener(view -> {
                Log.d(TAG, "Opening ticket " + ticketId);

                final Intent intent = new Intent(mContext, TicketActivity.class);
                intent.putExtra("ticket_id", ticketId);

                mContext.startActivity(intent);
            });
        }
    }


    public WinnerAdapter() {
        mDataSet = new ArrayList<>();
    }

    public void setDataSet(ArrayList<Winner> dataSet) {
        this.mDataSet = dataSet;
        notifyDataSetChanged();
    }

    public void addItem(Winner winner) {
        mDataSet.add(winner);
        notifyItemInserted(mDataSet.size() - 1);
    }

    @NonNull
    @Override
    public WinnerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_winner, parent, false);

        // set the view's size, margins, padding and layout parameters

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Winner winner = mDataSet.get(position);

        holder.tvId.setText(String.format(Locale.US, "Ticket %s", winner.getTicketCode()));
        holder.tvCreatedAt.setText(winner.getCreatedAt());
        holder.tvLottery.setText(winner.getLottery().getName());

        TicketPlay ticketPlay = winner.getTicketPlay();
        holder.tvType.setText(ticketPlay.getType());
        holder.tvPoints.setText(String.valueOf(ticketPlay.getPoints()));
        holder.tvNumber.setText(ticketPlay.getNumber());

        final String reward = "$ " + winner.getReward();
        holder.tvReward.setText(reward);

        if (winner.getPaid()) {
            holder.btnPay.setVisibility(View.GONE);
            holder.tvAlreadyPaid.setVisibility(View.VISIBLE);
        } else {
            // holder.btnPay.setEnabled(true);
            holder.tvAlreadyPaid.setVisibility(View.GONE);
            holder.btnPay.setOnClickListener(view -> {
                final Context context = holder.mContext;
                confirmPayment(context, winner.getId());
            });
        }

        holder.setupButtonShowTicket(ticketPlay.getTicketId());
    }

    private void confirmPayment(Context context, int winnerId) {
        final String title = "Confirmaci??n de pago";
        final String message = "??Est?? seguro que desea pagar el premio #" + winnerId + "?";
        Global.showConfirmationDialog(context, title, message, (dialogInterface, i) -> {
            payWinner(context, winnerId);
        });
    }

    private void payWinner(Context context, int winnerId) {
        final String authHeader = User.getAuthHeader(context);

        MyApiAdapter.getApiService().payWinner(authHeader, winnerId).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(@NonNull Call<SimpleResponse> call, @NonNull Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    SimpleResponse simpleResponse = response.body();
                    if (simpleResponse.isSuccess()) {
                        Global.showMessageDialog(context, "Confirmaci??n", "El pago se ha registrado correctamente.");
                    } else {
                        Global.showMessageDialog(context, "Pago no realizado", simpleResponse.getErrorMessage());
                    }
                } else {
                    Global.showMessageDialog(context, "Pago no realizado", "Ocurri?? un error inesperado.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<SimpleResponse> call, @NonNull Throwable t) {
                Global.showMessageDialog(context, "Pago no realizado", t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

