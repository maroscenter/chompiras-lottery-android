package com.youtube.sorcjc.billetero.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.model.TicketPlay;
import com.youtube.sorcjc.billetero.model.Winner;
import com.youtube.sorcjc.billetero.ui.activity.TicketActivity;

import java.util.ArrayList;
import java.util.Locale;

public class WinnerAdapter extends RecyclerView.Adapter<WinnerAdapter.ViewHolder> {
    private ArrayList<Winner> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvId, tvCreatedAt, tvLottery, tvPoints, tvType, tvNumber, tvReward;
        public Button btnPay;

        private final Context mContext;

        public ViewHolder(View v) {
            super(v);

            mContext = v.getContext();

            tvId = v.findViewById(R.id.tvWinnerId);
            tvCreatedAt = v.findViewById(R.id.tvCreatedAt);
            tvLottery = v.findViewById(R.id.tvLottery);
            tvPoints = v.findViewById(R.id.tvPoints);
            tvType = v.findViewById(R.id.tvType);
            tvNumber = v.findViewById(R.id.tvNumber);
            tvReward = v.findViewById(R.id.tvReward);

            btnPay = v.findViewById(R.id.btnPayWinner);
            btnPay.setOnClickListener(view -> payWinner());
        }


        private void payWinner() {

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

        holder.tvId.setText(String.format(Locale.US, "#%d", winner.getId()));
        holder.tvCreatedAt.setText(winner.getCreatedAt());
        holder.tvLottery.setText(winner.getLottery().getName());

        TicketPlay ticketPlay = winner.getTicketPlay();
        holder.tvType.setText(ticketPlay.getType());
        holder.tvPoints.setText(String.valueOf(ticketPlay.getPoints()));
        holder.tvNumber.setText(String.valueOf(ticketPlay.getNumber()));

        final String reward = "$ " + winner.getReward();
        holder.tvReward.setText(reward);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

