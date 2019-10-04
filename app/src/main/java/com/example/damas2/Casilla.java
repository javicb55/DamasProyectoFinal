package com.example.damas2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Casilla {

    private int col;
    private int fila;

    private Paint casillaColor;
    private Rect casillaRectangular;

    public Casilla(int col, int fila){
        this.col = col;
        this.fila = fila;
        this.casillaColor = new Paint();

        casillaColor.setColor(esNegro() ? Color.BLACK : Color.WHITE);
        casillaColor.setAntiAlias(true);
    }

    public void dibujar(final Canvas canvas){
        canvas.drawRect(casillaRectangular, casillaColor);
    }

    private boolean esNegro() {
        return (col + fila) % 2 == 0;
    }


    public String getFilaString() {
        //cambia el index "0" a 1 de las filas
        return String.valueOf(fila + 1);
    }

    public String getColumnaString(){
        switch (col){
            case 0: return "A";
            case 1: return "B";
            case 2: return "C";
            case 3: return "D";
            case 4: return "E";
            case 5: return "F";
            case 6: return "G";
            case 7: return "H";
            default: return null;
        }
    }

    public String toString() {
        final String col = getColumnaString();
        final String fila    = getFilaString();
        return "<Tile " + col + fila + ">";
    }

}
