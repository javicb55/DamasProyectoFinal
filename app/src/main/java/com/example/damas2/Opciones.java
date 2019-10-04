package com.example.damas2;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CheckBox;

public class Opciones extends AppCompatActivity {

    private CheckBox cb_turno_jugador;
    Tablero tablero;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opciones);

        cb_turno_jugador = findViewById(R.id.cb_turno_jugador);

    }

    public Opciones() {
    }

    public String cambiarTurnoJugador(){
        if(cb_turno_jugador.isChecked()){
            tablero.turnoJugador = "Turno del jugador 2";
        }

        return tablero.turnoJugador;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.mainActivity:
                Intent myIntent = new Intent(Opciones.this, MainActivity.class);
                startActivity(myIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
