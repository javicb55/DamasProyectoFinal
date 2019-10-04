package com.example.damas2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.zip.Inflater;


public class MainActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Tablero tablero = new Tablero(this);
        tablero.setNumColumnas(8);
        tablero.setNumFilas(8);

        setContentView(tablero);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.idopciones:
                Intent myIntent = new Intent(MainActivity.this, Opciones.class);
                startActivity(myIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


