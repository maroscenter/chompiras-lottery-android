package com.youtube.sorcjc.billetero.io;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.model.Ticket;
import com.youtube.sorcjc.billetero.ui.adapter.TicketAdapter;

public class TicketPreferences {

    private Activity activity;
    private int limitTimes = -1;

    public TicketPreferences(Activity activity) {
        this.activity = activity;
    }

    public void clearList(final TicketAdapter ticketAdapter) {
        SharedPreferences sharedPref = activity.getSharedPreferences("tickets_list", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        for (int i=0; i<=99; ++i) {
            editor.putInt(String.valueOf(i), 0);
            if (ticketAdapter != null)
                ticketAdapter.updateItem(new Ticket(i, 0, 0), i);
        }

        editor.apply();

        // clear last operation
        setLastOperation(0, 0);
    }

    public void setTotalSold(int ticketNumber, int value) {
        SharedPreferences sharedPref = activity.getSharedPreferences("tickets_list", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(String.valueOf(ticketNumber), value);
        editor.apply();
    }

    public void addTotalSold(int ticketNumber, int valueToAdd) {
        int newTimesQuantity = getTotalSold(ticketNumber) +valueToAdd;

        // no hack opportunity
        if (newTimesQuantity < 0)
            newTimesQuantity = 0;

        setTotalSold(ticketNumber, newTimesQuantity);
    }

    public int getTotalSold(int ticketNumber) {
        SharedPreferences sharedPref = activity.getSharedPreferences("tickets_list", Context.MODE_PRIVATE);

        return sharedPref.getInt(String.valueOf(ticketNumber), 0);
    }

    public Ticket getTicketAt(int position) {
        final int totalSold = getTotalSold(position);
        int surplus = 0;

        if (position>=1 && position<=31 && totalSold>getLimitTimes())
            surplus = totalSold - getLimitTimes();

        return new Ticket(position, totalSold, surplus);
    }

    public int getLimitTimes() {
        if (limitTimes == -1)
            limitTimes = Global.getIntFromGlobalPreferences(activity, "limit_times");

        return limitTimes;
    }

    public void setLimitTimes(final int newLimitTimes) {
        Global.saveGlobalPreference(activity, "limit_times", newLimitTimes);
    }


    // last operation and undo action

    public void setLastOperation(int ticketNumber, int valueAdded) {
        // the last operation will be the last sum performed from the home tab
        SharedPreferences sharedPref = activity.getSharedPreferences("tickets_list", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("last_ticket_number_modified", ticketNumber);
        editor.putInt("last_quantity_times_added", valueAdded);
        editor.apply();
    }

    public void undoLastOperation(final TicketAdapter ticketAdapter) {
        SharedPreferences sharedPref = activity.getSharedPreferences("tickets_list", Context.MODE_PRIVATE);
        final int lastTicketNumberModified = sharedPref.getInt("last_ticket_number_modified", 0);
        final int lastQuantityTimesAdded = sharedPref.getInt("last_quantity_times_added", 0);

        if (lastQuantityTimesAdded == 0) {
            Global.showMessageDialog(activity, "No es posible", "No se encontró información de la última operación.");
            return;
        }

        String message = "¿Acepta deshacer la última operación registrada desde Inicio? "
                + "Esto eliminará " + lastQuantityTimesAdded + " tiempos a la cifra "
                + lastTicketNumberModified + ".";

        Global.showConfirmationDialog(activity, "Deshacer última operación", message, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                addTotalSold(lastTicketNumberModified, lastQuantityTimesAdded * -1);

                // clear last operation
                setLastOperation(0, 0);

                // & refresh the recycler view in ListFragment
                ticketAdapter.updateItem(getTicketAt(lastTicketNumberModified), lastTicketNumberModified);
            }
        });
    }
}
