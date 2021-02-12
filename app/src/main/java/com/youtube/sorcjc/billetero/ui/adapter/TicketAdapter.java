package com.youtube.sorcjc.billetero.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.io.MyApiAdapter;
import com.youtube.sorcjc.billetero.io.response.SimpleResponse;
import com.youtube.sorcjc.billetero.model.Ticket;
import com.youtube.sorcjc.billetero.model.User;
import com.youtube.sorcjc.billetero.ui.activity.TicketActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {
    private ArrayList<Ticket> mDataSet;

    // Define references to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvId, tvCreatedAt, tvLotteries;
        public ImageButton btnDetails, btnDelete;

        private final Context mContext;

        public ViewHolder(View v) {
            super(v);

            mContext = v.getContext();

            tvId = v.findViewById(R.id.tvWinnerId);
            tvCreatedAt = v.findViewById(R.id.tvCreatedAt);
            tvLotteries = v.findViewById(R.id.tvLotteries);
            btnDetails = v.findViewById(R.id.btnDetails);
            btnDelete = v.findViewById(R.id.btnDelete);

            btnDetails.setOnClickListener(view -> startTicketActivity());
            btnDelete.setOnClickListener(view -> deleteTicket());
        }


        private void startTicketActivity() {
            final Intent intent = new Intent(mContext, TicketActivity.class);
            intent.putExtra("ticket_id", tvId.getText().toString());
            mContext.startActivity(intent);
        }

        private void deleteTicket() {
            final String authHeader = User.getAuthHeader(mContext);
            final String ticketId = tvId.getText().toString();

            MyApiAdapter.getApiService().deleteTicket(authHeader, ticketId).enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(@NonNull Call<SimpleResponse> call, @NonNull Response<SimpleResponse> response) {
                    if (response.isSuccessful()) {
                        SimpleResponse simpleResponse = response.body();
                        if (simpleResponse == null) {
                            Global.showToast(mContext, R.string.retrofit_response_not_successful);
                            return;
                        }

                        if (simpleResponse.isSuccess()) {
                            Global.showMessageDialog(mContext, "Operación exitosa", "Ticket anulado correctamente");
                        } else {
                            Global.showMessageDialog(mContext, R.string.dialog_title_error, simpleResponse.getErrorMessage());
                        }
                    } else {
                        Global.showToast(mContext, R.string.retrofit_response_not_successful);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SimpleResponse> call, @NonNull Throwable t) {
                    Global.showToast(mContext, t.getLocalizedMessage());
                }
            });
        }
    }


    public TicketAdapter() {
        mDataSet = new ArrayList<>();
    }

    public void setDataSet(ArrayList<Ticket> dataSet) {
        this.mDataSet = dataSet;
        notifyDataSetChanged();
    }

    public void addItem(Ticket ticket) {
        mDataSet.add(ticket);
        notifyItemInserted(mDataSet.size() - 1);
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
        Ticket ticket = mDataSet.get(position);

        // replace the contents of the view with that element
        holder.tvId.setText(twoDigits(ticket.getId()));
        holder.tvCreatedAt.setText(ticket.getCreatedAt());
        holder.tvLotteries.setText(ticket.getLotteries());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

