package com.youtube.sorcjc.billetero.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.io.TicketPreferences;
import com.youtube.sorcjc.billetero.model.Ticket;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {
    private ArrayList<Ticket> dataSet;

    // Define references to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        public TextView tvTicketNumber, tvQuantity, tvSurplus;

        private Context context;
        private ViewGroup viewGroup;

        public ViewHolder(View v) {
            super(v);

            viewGroup = (ViewGroup) v;
            context = v.getContext();

            tvTicketNumber = (TextView) v.findViewById(R.id.tvTicketNumber);
            tvQuantity = (TextView) v.findViewById(R.id.tvQuantity);
            tvSurplus = (TextView) v.findViewById(R.id.tvSurplus);

            tvQuantity.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            if (view.getId() == R.id.tvQuantity) {
                final String title = "Cifra " + tvTicketNumber.getText();
                final String initialValue = tvQuantity.getText().toString();
                Global.showInputDialog(title, initialValue, context, viewGroup, new PositiveListenerForDialog());
            }
            return false;
        }

        class PositiveListenerForDialog implements TicketOnNewQuantityListener {
            @Override
            public void updateQuantity(final String quantityString) {
                if (quantityString.isEmpty()) {
                    Global.showMessageDialog(context, "No se pudo guardar", "No se ingresó ninguna cantidad.");
                    return;
                }

                final int quantity = Integer.parseInt(quantityString);
                final int ticketNumber = Integer.parseInt( tvTicketNumber.getText().toString() );

                TicketPreferences ticketPreferences = new TicketPreferences((Activity) context);
                final int limitTimes = ticketPreferences.getLimitTimes();
                int surplus = 0;
                if (quantity > limitTimes) {
                    surplus = quantity - limitTimes;
                }

                ticketPreferences.setTotalSold(ticketNumber, quantity);

                tvQuantity.setText(quantityString);
                tvSurplus.setText(String.valueOf(surplus));
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

    @Override
    public TicketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_row, parent, false);


        // set the view's size, margins, padding and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get element from the data set
        Ticket ticket = dataSet.get(position);

        // replace the contents of the view with that element
        holder.tvTicketNumber.setText(twoDigits(ticket.getTicketNumber()));
        holder.tvQuantity.setText(String.valueOf(ticket.getTotalSold()));
        holder.tvSurplus.setText(String.valueOf(ticket.getSurplus()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

