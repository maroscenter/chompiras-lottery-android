package com.youtube.sorcjc.billetero.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.model.Ticket;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {
    private ArrayList<Ticket> dataSet;

    // Define references to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvId, tvCreatedAt, tvLotteries;
        public Button btnDetails;

        private final Context context;

        public ViewHolder(View v) {
            super(v);

            context = v.getContext();

            tvId = v.findViewById(R.id.tvTicketId);
            tvCreatedAt = v.findViewById(R.id.tvCreatedAt);
            tvLotteries = v.findViewById(R.id.tvLotteries);
            btnDetails = v.findViewById(R.id.btnDetails);

            btnDetails.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btnDetails) {
                Toast.makeText(context, "Development", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public TicketAdapter() {
        dataSet = new ArrayList<>();
    }

    public void setDataSet(ArrayList<Ticket> dataSet) {
        this.dataSet = dataSet;
    }

    public void addItem(Ticket ticket) {
        dataSet.add(ticket);
        notifyItemInserted(dataSet.size() - 1);
    }

    public void updateItem(Ticket ticket, int position) {
        // it is possible because the position is equals to the ticket number for each item
        dataSet.set(position, ticket);
        notifyItemChanged(position);
    }

    private String twoDigits(final int i) {
        final String pre = (i<=9 ? "0" : "");
        return pre + i;
    }

    @NonNull
    @Override
    public TicketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket, parent, false);

        // set the view's size, margins, padding and layout parameters
        // ...

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get element from the data set
        Ticket ticket = dataSet.get(position);

        // replace the contents of the view with that element
        holder.tvId.setText(twoDigits(ticket.getId()));
        holder.tvCreatedAt.setText(ticket.getCreatedAt());
        holder.tvLotteries.setText(ticket.getLotteries());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

