package com.youtube.sorcjc.billetero;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.youtube.sorcjc.billetero.ui.adapter.TicketOnNewQuantityListener;

public class Global {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int stringResource) {
        Toast.makeText(context, stringResource, Toast.LENGTH_SHORT).show();
    }

    public static void showMessageDialog(Context context, String title, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setPositiveButton("Ok", null);
        adb.show();
    }

    public static void showMessageDialog(Context context, int titleRes, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(titleRes);
        adb.setMessage(message);
        adb.setPositiveButton("Ok", null);
        adb.show();
    }

    public static void showConfirmationDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setPositiveButton("Aceptar", listener);
        adb.setNegativeButton("Cancelar", null);
        adb.show();
    }

    public static void showInputDialog(final String title, final String initialValue, Context context, ViewGroup viewGroup, final TicketOnNewQuantityListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.input_new_quantity, viewGroup, false);

        // Set up the input
        final EditText input = viewInflated.findViewById(R.id.input);
        input.setText(initialValue);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                final String quantity = input.getText().toString();
                positiveListener.updateQuantity(quantity);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public static void saveIntPref(Activity activity, String key, int value) {
        SharedPreferences sharedPref = activity.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void saveStringPref(Activity activity, String key, String value) {
        SharedPreferences sharedPref = activity.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static int getIntFromPrefs(Activity activity, String key) {
        SharedPreferences sharedPref = activity.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 0);
    }



    public static String getStringFromPrefs(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

}
