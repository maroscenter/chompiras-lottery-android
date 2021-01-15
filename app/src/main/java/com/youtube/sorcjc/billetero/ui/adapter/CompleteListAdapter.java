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

public class CompleteListAdapter extends RecyclerView.Adapter<CompleteListAdapter.ViewHolder> {
    private ArrayList<Integer> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv1, tv2, tv3, tv4;

        private Context context;

        public ViewHolder(View v) {
            super(v);

            context = v.getContext();

            tv1 = (TextView) v.findViewById(R.id.tv1);
            tv2 = (TextView) v.findViewById(R.id.tv2);
            tv3 = (TextView) v.findViewById(R.id.tv3);
            tv4 = (TextView) v.findViewById(R.id.tv4);
        }

    }

    public CompleteListAdapter() {
        dataSet = new ArrayList<>();
    }

    public void setDataSet(ArrayList<Integer> dataSet) {
        this.dataSet = dataSet;
    }

    public void addItem(Integer ticket) {
        dataSet.add(ticket);
        notifyItemInserted(dataSet.size() - 1);
    }

    private String twoDigits(final int i) {
        final String pre = (i<=9 ? "0" : "");
        return pre + i;
    }

    @Override
    public CompleteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_4_col, parent, false);

        // set the view's size, margins, padding and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        // get elements from the data set
        Integer ticket1 = dataSet.get(i);
        Integer ticket2 = dataSet.get(i+25);
        Integer ticket3 = dataSet.get(i+50);
        Integer ticket4 = dataSet.get(i+75);

        // replace the contents of the view with that element
        // the twoDigits function just have sense for the 1st column !
        holder.tv1.setText(twoDigits(i)+": "+ticket1);
        holder.tv2.setText((i+25)+": "+ticket2);
        holder.tv3.setText((i+50)+": "+ticket3);
        holder.tv4.setText((i+75)+": "+ticket4);
    }

    @Override
    public int getItemCount() {
        // wow, this overwritten method is really useful :P
        return dataSet.size() / 4;
    }
}

