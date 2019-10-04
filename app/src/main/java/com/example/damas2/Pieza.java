package com.example.damas2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Pieza extends View {

    public Pieza(Context context) {
        super(context);
    }


    public void dibujarPeon(Canvas canvas) {
        Drawable dibujo = getResources().getDrawable(R.mipmap.blancas, null);
        dibujo.setBounds(10,10,10,10);
        dibujo.draw(canvas);
    }
}
