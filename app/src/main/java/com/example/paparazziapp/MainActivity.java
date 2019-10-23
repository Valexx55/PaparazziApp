package com.example.paparazziapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;//el menú lateral
    private boolean menu_visible;//para gestionar si está visible o no el menú lateral

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        //asignamos listener del menú lateral
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navview);
        navigationView.setNavigationItemSelectedListener(this); //escucho los eventos de esta clase aquí
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id_item = item.getItemId();
        switch (id_item) {


            case android.R.id.home:
                if (menu_visible) {
                    drawerLayout.closeDrawers();
                    menu_visible = false;
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                    menu_visible = true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        String menu = item.getTitle().toString();
        int npi = item.getOrder();//obtengo el número del punto de interés

        Log.d(getClass().getCanonicalName(), "Ha tocado la opción " + menu + " " +npi);
        Intent irTomarFoto = new Intent(this,TomarFotoDisco.class);
        startActivity(irTomarFoto);
        drawerLayout.closeDrawers();
        menu_visible = false;

        return false;

    }
}
