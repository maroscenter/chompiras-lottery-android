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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.ui.fragment.HomeFragment;
import com.youtube.sorcjc.billetero.ui.fragment.SoldTicketsFragment;
import com.youtube.sorcjc.billetero.ui.fragment.TotalFragment;
import com.youtube.sorcjc.billetero.ui.fragment.WinnersFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initial fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFragment, new HomeFragment());
        transaction.commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        Fragment fragment = null;

                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();

                        if (item.getItemId() == R.id.action_home) {
                            fragment = new HomeFragment();
                        } else if (item.getItemId() == R.id.action_tickets) {
                            fragment = new SoldTicketsFragment();
                        } else if (item.getItemId() == R.id.action_winners) {
                            fragment = new WinnersFragment();
                        } else if (item.getItemId() == R.id.action_total) {
                            fragment = new TotalFragment();
                        }

                        // replace fragment content
                        if (fragment != null) {
                            transaction.replace(R.id.contentFragment, fragment);
                            transaction.commit();
                        }

                        return true;
                    }
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
        if (item.getItemId() == R.id.reset) {
            Global.showConfirmationDialog(
                    this,
                    "Confirmar acción",
                    "¿Realmente desea resetear su lista? Todos los tiempos vendidos serán modificados por cero.",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            resetList();
                        }
                    }
            );

            return true;

        } else if (item.getItemId() == R.id.logout) {
            Global.showConfirmationDialog(
                    this,
                    "Confirmar acción",
                    "¿Desea cerrar sesión?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            performLogout();
                        }
                    }
            );

            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void resetList() {
    }

    private void performLogout() {
        Global.saveStringGlobalPreference(this, "name", "");
        Global.saveGlobalPreference(this, "user_id", 0);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
