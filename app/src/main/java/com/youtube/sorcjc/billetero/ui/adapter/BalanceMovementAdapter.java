package com.youtube.sorcjc.billetero.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.model.BalanceMovement;
import com.youtube.sorcjc.billetero.model.Ticket;

import java.util.ArrayList;

public class BalanceMovementAdapter extends RecyclerView.Adapter<BalanceMovementAdapter.ViewHolder> {
    private ArrayList<BalanceMovement> mDataSet;

    // Define references to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDescription, tvCreatedAt, tvAmount;

        private final Context mContext;

        public ViewHolder(View v) {
            super(v);

            mContext = v.getContext();

            tvDescription = v.findViewById(R.id.tvDescription);
            tvCreatedAt = v.findViewById(R.id.tvCreatedAt);
            tvAmount = v.findViewById(R.id.tvAmount);
        }
    }


    public BalanceMovementAdapter() {
        mDataSet = new ArrayList<>();
    }

    public void setDataSet(ArrayList<BalanceMovement> dataSet) {
        this.mDataSet = dataSet;
        notifyDataSetChanged();
    }

    private String twoDigits(final int i) {
        final String pre = (i<=9 ? "0" : "");
        return pre + i;
    }

    @NonNull
    @Override
    public BalanceMovementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movement, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get element from the data set
        BalanceMovement movement = mDataSet.get(position);

        // replace the contents of the view with that element
        holder.tvDescription.setText(movement.getDescription());
        holder.tvCreatedAt.setText(movement.getCreatedAt());
        holder.tvAmount.setText(String.valueOf(movement.getAmount()));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

