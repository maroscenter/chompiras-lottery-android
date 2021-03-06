package com.youtube.sorcjc.billetero.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.ui.fragment.HomeFragment;
import com.youtube.sorcjc.billetero.ui.fragment.SoldTicketsFragment;
import com.youtube.sorcjc.billetero.ui.fragment.EarningsFragment;
import com.youtube.sorcjc.billetero.ui.fragment.WinnersFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        Fragment contentFragment = getSupportFragmentManager().findFragmentById(R.id.contentFragment);

        if (contentFragment == null) {
            // Initial fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentFragment, new HomeFragment());
            transaction.commit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    Fragment fragment = null;

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction transaction1 = fm.beginTransaction();

                    if (item.getItemId() == R.id.action_home) {
                        fragment = new HomeFragment();
                    } else if (item.getItemId() == R.id.action_tickets) {
                        fragment = new SoldTicketsFragment();
                    } else if (item.getItemId() == R.id.action_winners) {
                        fragment = new WinnersFragment();
                    } else if (item.getItemId() == R.id.action_total) {
                        fragment = new EarningsFragment();
                    }

                    // replace fragment content
                    if (fragment != null) {
                        transaction1.replace(R.id.contentFragment, fragment);
                        transaction1.commit();
                    }

                    return true;
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            Global.showConfirmationDialog(
                    this,
                    "Confirmar acci??n",
                    "??Desea cerrar sesi??n?",
                    (dialogInterface, i) -> performLogout()
            );

            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void performLogout() {
        Global.saveStringPref(this, "access_token", "");

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
