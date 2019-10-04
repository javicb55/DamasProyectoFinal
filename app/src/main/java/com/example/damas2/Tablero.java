package com.example.damas2;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import java.lang.reflect.Array;
import java.util.zip.Inflater;


/**
 * @author javi
 * Esta clase muestra el tablero de juego.
 */
public class Tablero extends View {


    private int numColumnas, numFilas;
    private int celdaAnchura, celdaAltura;

    // Esta es la posición donde se colocará el tablero y el tamaño
    int poshorini = 100;
    int posverini = 50;
    double proporcion = 1.5;

   //Inicialización de la clase Paint
    private Paint pinturaNegra = new Paint();
    private Paint pinturaAmarilla = new Paint();
    private Paint pinturaAzul = new Paint();
    private Paint pinturaRoja = new Paint();
    private Paint pinturaVerde = new Paint();
    private Drawable dibujo;




    DisplayMetrics displayMetrics = new DisplayMetrics();


    //Atributos para iniciar algún evento
    public String turnoJugador = "Turno del jugador 1";
    private boolean partidaIniciada = true;
    private boolean movimientoBienRealizado = true;

    //Estos atributos guardan la columna y la fila que se selecciona por primera vez

    int contadorTouch = 0;
    int primeraColumna = 0;
    int primeraFila = 0;
    int segundaFila = 0;
    int segundaColumna = 0;


    //Todas las listas de las casillas
    private boolean[][] piezaPeonBlanca;
    private boolean[][] piezaPeonNegra;
    private boolean[][] piezaDamaBlanca;
    private boolean[][] piezaDamaNegra;
    private boolean[][] casillaSeleccionada;
    private boolean[][] casillaOcupada;
    private boolean[][] casposblanca;
    private boolean[][] casposnegra;



    public Tablero(Context context) {
        this(context, null);
    }

    public Tablero(Context context, AttributeSet attrs) {
        super(context, attrs);
        pinturaNegra.setColor(Color.BLACK);
        pinturaAmarilla.setColor(Color.YELLOW);
        pinturaAzul.setColor(Color.CYAN);
        pinturaRoja.setColor(Color.RED);
        pinturaVerde.setColor(Color.GREEN);
    }

    //Setters y Getters
    public int getNumFilas() {
        return numFilas;
    }

    public int getNumColumnas() {
        return numColumnas;
    }

    public void setNumColumnas(int numColumnas) {
        this.numColumnas = numColumnas;
    }

    public void setNumFilas(int numFilas) {
        this.numFilas = numFilas;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    private void calculateDimensions() {
        if (numColumnas < 1 || numFilas < 1) {
            return;
        }

        celdaAnchura = (int) ((getWidth() / numColumnas) / proporcion);
        celdaAltura = (int) ((getHeight() / numFilas) / proporcion);

        casillaSeleccionada = new boolean[numColumnas][numFilas];
        casposblanca = new boolean[numColumnas][numFilas];
        casposnegra = new boolean[numColumnas][numFilas];
        piezaPeonBlanca = new boolean[numColumnas][numFilas];
        piezaPeonNegra = new boolean[numColumnas][numFilas];
        piezaDamaNegra = new boolean[numColumnas][numFilas];
        piezaDamaBlanca = new boolean[numColumnas][numFilas];
        casillaOcupada = new boolean[numColumnas][numFilas];


        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        comprobarCasillaOcupada();
        dibujarTablero(canvas);
        colorearCasillaSeleccionada(canvas);
        marcarMovimientoPosibleBlanca(canvas);
        marcarMovimientoPosibleNegra(canvas);
        dibujarPeonBlanca(canvas);
        dibujarPeonNegro(canvas);
        dibujarDamaBlanca(canvas);
        dibujarDamaNegra(canvas);
        if(partidaIniciada){
            inicarPartida();
            partidaIniciada = false;
        }
    }


    private void dibujarTablero(Canvas canvas){

        canvas.drawColor(Color.WHITE);


        int anchura = (int) (getWidth() / proporcion);
        int altura = (int) (getHeight() / proporcion);

        if (numColumnas == 0 || numFilas == 0) {
            return;
        }


        for (int i = 0; i < numColumnas; i++) {
            for (int j = 0; j < numFilas; j++) {
                if ((i + j) % 2 == 1) {
                    canvas.drawRect(i * celdaAnchura + poshorini, j * celdaAltura + posverini,
                            (i + 1) * celdaAnchura + poshorini, (j + 1) * celdaAltura + posverini,
                            pinturaNegra);
                }
            }
        }

        for (int i = 0; i < numColumnas + 1; i++) {
            canvas.drawLine(i * celdaAnchura + poshorini, posverini, i * celdaAnchura + poshorini, altura + posverini,
                    pinturaVerde);
        }

        for (int i = 0; i < numFilas + 1; i++) {
            canvas.drawLine(poshorini, i * celdaAltura + posverini, anchura + poshorini, i * celdaAltura + posverini,
                    pinturaVerde);
        }

    }

    public void inicarPartida(){
        //Dibujar Piezas Blancas
        for (int i = 0; i < numColumnas; i++) {
            for (int j = (numFilas - 2); j < numFilas; j++) {
                if ((i + j) % 2 == 1) {
                    piezaPeonBlanca[i][j] = true;
                }
            }
        }

        //Dibujar Piezas Negras
        for (int i = 0; i < numColumnas; i++) {
            for (int j = 0; j < 2; j++) {
                if ((i + j) % 2 == 1) {
                    piezaPeonNegra[i][j] = true;
                }
            }
        }
    }

    private void colorearCasillaSeleccionada(Canvas canvas){

        for (int i = 0; i < numColumnas; i++) {
            for (int j = 0; j < numFilas; j++) {
                    if (casillaSeleccionada[i][j]) {
                        canvas.drawRect(i * celdaAnchura + poshorini, j * celdaAltura + posverini,
                                (i + 1) * celdaAnchura + poshorini, (j + 1) * celdaAltura + posverini,
                                pinturaAmarilla);
                    }

                }
            }
    }



    private void marcarMovimientoPosibleBlanca(Canvas canvas){
        for (int i = 0; i < numColumnas; i++) {
            for (int j = 0; j < numFilas; j++) {
                if (casposblanca[i][j]) {
                    canvas.drawRect(i * celdaAnchura + poshorini, j * celdaAltura + posverini,
                            (i + 1) * celdaAnchura + poshorini, (j + 1) * celdaAltura + posverini,
                            pinturaAzul);
                }
            }
        }
    }

    private void marcarMovimientoPosibleNegra(Canvas canvas){
        for (int i = 0; i < numColumnas; i++) {
            for (int j = 0; j < numFilas; j++) {
                if (casposnegra[i][j]) {
                    canvas.drawRect(i * celdaAnchura + poshorini, j * celdaAltura + posverini,
                            (i + 1) * celdaAnchura + poshorini, (j + 1) * celdaAltura + posverini,
                            pinturaRoja);
                }
            }
        }
    }

    public void mostrarMovimientosNegras(int columna,int fila){

        if(casillaSeleccionada[columna][fila] == piezaPeonNegra[columna][fila]) {
            try{
                if(columna == (numColumnas-1)){
                    if(!casillaOcupada[columna-1][fila+1]){
                        casposnegra[columna-1][fila+1] = !casposnegra[columna-1][fila+1];
                    } if(piezaPeonBlanca[columna-1][fila+1]){
                        casposnegra[columna-2][fila+2] = !casposnegra[columna-2][fila+2];
                    }
                }
                else if(columna == 1){
                    Log.v("Comer", "entra aquí");
                    if(!casillaOcupada[columna-1][fila+1]){
                        casposnegra[columna-1][fila+1] = !casposnegra[columna-1][fila+1];
                        Log.v("Comer", "Primer if");
                    }
                    if(!casillaOcupada[columna+1][fila+1]) {
                        casposnegra[columna + 1][fila + 1] = !casposnegra[columna + 1][fila + 1];
                        Log.v("Comer", "segundo if");
                    }
                    if (!casillaOcupada[columna + 2][fila + 2] & piezaPeonBlanca[columna + 1][fila + 1] || piezaDamaBlanca[columna + 1][fila + 1]) {
                        casposnegra[columna + 2][fila + 2] = !casposnegra[columna + 2][fila + 2];
                        Log.v("Comer", "tercer if");
                    }
                }
                else if(columna == numColumnas-2){
                    if(!casillaOcupada[columna+1][fila+1])
                        casposnegra[columna+1][fila+1] = !casposnegra[columna+1][fila+1];
                    if(!casillaOcupada[columna-1][fila+1])
                        casposnegra[columna-1][fila+1] = !casposnegra[columna-1][fila+1];
                    if(!casillaOcupada[columna-2][fila+2] & piezaPeonBlanca[columna-1][fila+1] || piezaDamaBlanca[columna-1][fila+1]){
                        casposnegra[columna - 2][fila + 2] = !casposnegra[columna - 2][fila + 2];
                    }
                }else if(columna == 0){
                    //Log.v("entra", "probando 0");
                    if(!casillaOcupada[columna+1][fila+1]){
                        casposnegra[columna + 1][fila + 1] = !casposnegra[columna + 1][fila + 1];
                    } else if(!casillaOcupada[columna+2][fila+2] & piezaPeonBlanca[columna+1][fila+1] || piezaDamaBlanca[columna+1][fila+1]) {
                        casposnegra[columna + 2][fila + 2] = !casposnegra[columna + 2][fila + 2];
                    }
                } else {
                    //Log.v("entra", "probando 2");
                    if(!casillaOcupada[columna+1][fila+1]) {
                        casposnegra[columna + 1][fila + 1] = !casposnegra[columna + 1][fila + 1];
                    }if(!casillaOcupada[columna - 1][fila+1]) {
                        casposnegra[columna - 1][fila + 1] = !casposnegra[columna - 1][fila + 1];
                    }if(!casillaOcupada[columna - 2][fila + 2] & piezaPeonBlanca[columna - 1][fila + 1] || piezaDamaBlanca[columna-1][fila+1]){
                        casposnegra[columna - 2][fila + 2] = !casposnegra[columna - 2][fila + 2];
                    }if(!casillaOcupada[columna + 2][fila + 2] & piezaPeonBlanca[columna + 1][fila + 1]|| piezaDamaBlanca[columna + 1][fila + 1]){
                        casposnegra[columna + 2][fila + 2] = !casposnegra[columna + 2][fila + 2];
                    }
                }
        }catch (IndexOutOfBoundsException e){

            //Log.v("entra", "catch");
         }
        }else if(casillaSeleccionada[columna][fila] == piezaDamaNegra[columna][fila]){
            try {
                for(int i = 0; i < numColumnas; i++){
                    for(int j = 0; j < numFilas; j++){
                        if(((primeraColumna + primeraFila) == (i + j)) & (((i + j) % 2) == 1) & !casillaOcupada[i][j]){
                            casposnegra[i][j] = !casposnegra[i][j];
                        }else if(((primeraColumna + primeraFila) == (i + j)) & (((i + j) % 2) == 1) & piezaPeonBlanca[i][j]){
                            casposnegra[i][j] = true;
                            casposnegra[i + 1][j - 1] = false;
                            break;
                        }
                        /*if(((primeraColumna*10 + primeraFila)-(i*10+j))% 11 == 0 & (((i + j) % 2) == 1) & !casillaOcupada[i][j]){
                            casposnegra[i][j] = !casposnegra[i][j];
                        }*/
                    }
                }
            }catch (IndexOutOfBoundsException e){

            }
        }

    }


    public void mostrarMovimientosBlancas(int columna,int fila){

        if(casillaSeleccionada[columna][fila] == piezaPeonBlanca[columna][fila]) {
            try {
                if (columna == 0) {
                    //Log.v("entra", "probando 0");
                    if (!casillaOcupada[columna + 1][fila - 1]) {
                        casposblanca[columna + 1][fila - 1] = !casposblanca[columna + 1][fila - 1];
                    } else if (!casillaOcupada[columna + 2][fila - 2] & piezaPeonNegra[columna + 1][fila - 1] || piezaDamaNegra[columna + 1][fila - 1]) {
                        casposblanca[columna + 2][fila - 2] = !casposblanca[columna + 2][fila - 2];
                    }
                } else if (columna == 1) {
                    if (!casillaOcupada[columna - 1][fila - 1])
                        casposblanca[columna - 1][fila - 1] = !casposblanca[columna - 1][fila - 1];
                    if (!casillaOcupada[columna + 1][fila - 1])
                        casposblanca[columna + 1][fila - 1] = !casposblanca[columna + 1][fila - 1];
                    if (!casillaOcupada[columna + 2][fila - 2] & piezaPeonNegra[columna + 1][fila - 1] || piezaDamaNegra[columna + 1][fila - 1]) {
                        casposblanca[columna + 2][fila - 2] = !casposblanca[columna + 2][fila - 2];
                    }
                } else if (columna == numColumnas - 2) {
                    if (!casillaOcupada[columna + 1][fila - 1])
                        casposblanca[columna + 1][fila - 1] = !casposblanca[columna + 1][fila - 1];
                    if (!casillaOcupada[columna - 1][fila - 1])
                        casposblanca[columna - 1][fila - 1] = !casposblanca[columna - 1][fila - 1];
                    if (!casillaOcupada[columna - 2][fila - 2] & piezaPeonNegra[columna - 1][fila - 1] || piezaDamaNegra[columna - 1][fila - 1]) {
                        casposblanca[columna - 2][fila - 2] = !casposblanca[columna - 2][fila - 2];
                    }
                } else if (columna == (numColumnas - 1)) {
                    //Log.v("entra", "probando 1");
                    if (!casillaOcupada[columna - 2][fila - 2] & piezaPeonNegra[columna - 1][fila - 1] || piezaDamaNegra[columna - 1][fila - 1]) {
                        casposblanca[columna - 2][fila - 2] = !casposblanca[columna - 2][fila - 2];
                    } else if (!casillaOcupada[columna - 1][fila - 1]) {
                        casposblanca[columna - 1][fila - 1] = !casposblanca[columna - 1][fila - 1];
                    }
                } else {
                    //Log.v("entra", "probando 2");
                    if (!casillaOcupada[columna + 1][fila - 1]) {
                        casposblanca[columna + 1][fila - 1] = !casposblanca[columna + 1][fila - 1];
                    }
                    if (!casillaOcupada[columna - 1][fila - 1]) {
                        casposblanca[columna - 1][fila - 1] = !casposblanca[columna - 1][fila - 1];
                    }
                    if (!casillaOcupada[columna - 2][fila - 2] & piezaPeonNegra[columna - 1][fila - 1] || piezaDamaNegra[columna - 1][fila - 1]) {
                        casposblanca[columna - 2][fila - 2] = !casposblanca[columna - 2][fila - 2];
                    }
                    if (!casillaOcupada[columna + 2][fila - 2] & piezaPeonNegra[columna + 1][fila - 1] || piezaDamaNegra[columna + 1][fila - 1]) {
                        casposblanca[columna + 2][fila - 2] = !casposblanca[columna + 2][fila - 2];
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                //Log.v("entra", "catch");
            }
        }else if(casillaSeleccionada[columna][fila] == piezaDamaBlanca[columna][fila]){
            try {
                for(int i = 0; i < numColumnas; i++){
                    for(int j = 0; j < numFilas; j++){
                       /** OJO ESTA EN PROCESO **/
                        if(((primeraColumna + primeraFila) == (i + j)) & (((i + j) % 2) == 1) & !casillaOcupada[i][j]){
                            casposblanca[i][j] = !casposblanca[i][j];
                            /*if(!casillaOcupada[i][j] & primeraColumna < i){
                                casposblanca[i][j] = !casposblanca[i][j];
                                if(primeraColumna <= 2)
                                    casposblanca[i][j] = !casposblanca[i][j];
                            }else{

                            }*/
                        }

                        if(((primeraColumna*10 + primeraFila)-(i*10+j))% 11 == 0 & (((i + j) % 2) == 1) & !casillaOcupada[i][j]){
                           casposblanca[i][j] = !casposblanca[i][j];
                        }
                    }
                }
            }catch (IndexOutOfBoundsException e){

            }
        }
    }


    private boolean realizarMovimientoBlancas(boolean[][] arrayBlanca) {

        for (int i = 0; i < numColumnas; i++) {
            for (int j = 0; j < numFilas; j++)
                if (arrayBlanca[i][j] & arrayBlanca[segundaColumna][segundaFila]) {


                    //Realiza el movimiento
                    if (casillaSeleccionada[primeraColumna][primeraFila] == piezaPeonBlanca[primeraColumna][primeraFila]) {
                        piezaPeonBlanca[segundaColumna][segundaFila] = true;
                        piezaPeonBlanca[primeraColumna][primeraFila] = false;
                    } else {
                        piezaDamaBlanca[segundaColumna][segundaFila] = true;
                        piezaDamaBlanca[primeraColumna][primeraFila] = false;
                    }

                    //SE transforma en dama
                    if (segundaFila == 0 & piezaPeonBlanca[segundaColumna][0]) {
                        piezaPeonBlanca[segundaColumna][0] = !piezaPeonBlanca[segundaColumna][0];
                        piezaDamaBlanca[segundaColumna][segundaFila] = !piezaDamaBlanca[segundaColumna][segundaFila];
                    }
                    try {
                        if ((primeraColumna < segundaColumna) & (primeraFila < segundaFila)){
                            piezaPeonNegra[segundaColumna - 1][segundaFila - 1] = false;
                            piezaDamaNegra[segundaColumna - 1][segundaFila - 1] = false;
                        }
                        else if ((primeraColumna < segundaColumna) & (primeraFila > segundaFila)) {
                            piezaPeonNegra[segundaColumna - 1][segundaFila + 1] = false;
                            piezaDamaNegra[segundaColumna - 1][segundaFila + 1] = false;
                        } else if ((primeraColumna > segundaColumna) & (primeraFila < segundaFila)) {
                            piezaPeonNegra[segundaColumna + 1][segundaFila - 1] = false;
                            piezaDamaNegra[segundaColumna + 1][segundaFila - 1] = false;
                        } else if ((primeraColumna > segundaColumna) & (primeraFila > segundaFila)) {
                            piezaPeonNegra[segundaColumna + 1][segundaFila + 1] = false;
                            piezaDamaNegra[segundaColumna + 1][segundaFila + 1] = false;
                        }

                    } catch (IndexOutOfBoundsException e) {
                        Log.v("Error", "error al comer");
                    }

                    return movimientoBienRealizado = true;
                }
        }


        return movimientoBienRealizado = false;
    }

    private boolean realizarMovimientoNegras(boolean[][] arrayNegra){

        for (int i = 0; i < numColumnas; i++) {
            for (int j = 0; j < numFilas; j++) {
                if(arrayNegra[i][j] & arrayNegra[segundaColumna][segundaFila]){


                    //Realiza el movimiento
                    if (casillaSeleccionada[primeraColumna][primeraFila] == piezaPeonNegra[primeraColumna][primeraFila]) {
                        piezaPeonNegra[segundaColumna][segundaFila] = true;
                        piezaPeonNegra[primeraColumna][primeraFila] = false;
                    } else {
                        piezaDamaNegra[segundaColumna][segundaFila] = true;
                        piezaDamaNegra[primeraColumna][primeraFila] = false;
                    }

                    //Se transforma en negra
                    if(segundaFila == (numFilas-1) & piezaPeonNegra[segundaColumna][numFilas-1]){
                        piezaPeonNegra[segundaColumna][segundaFila] = !piezaPeonNegra[segundaColumna][segundaFila];
                        piezaDamaNegra[segundaColumna][segundaFila] = !piezaDamaNegra[segundaColumna][segundaFila];
                    }

                    try{
                        if ((primeraColumna < segundaColumna) & (primeraFila < segundaFila)){
                            piezaPeonBlanca[segundaColumna - 1][segundaFila - 1] = false;
                            piezaDamaBlanca[segundaColumna - 1][segundaFila - 1] = false;
                        }
                        else if ((primeraColumna < segundaColumna) & (primeraFila > segundaFila)) {
                            piezaPeonBlanca[segundaColumna - 1][segundaFila + 1] = false;
                            piezaDamaBlanca[segundaColumna - 1][segundaFila + 1] = false;
                        } else if ((primeraColumna > segundaColumna) & (primeraFila < segundaFila)) {
                            piezaPeonBlanca[segundaColumna + 1][segundaFila - 1] = false;
                            piezaDamaBlanca[segundaColumna + 1][segundaFila - 1] = false;
                        } else if ((primeraColumna > segundaColumna) & (primeraFila > segundaFila)) {
                            piezaPeonBlanca[segundaColumna + 1][segundaFila + 1] = false;
                            piezaDamaBlanca[segundaColumna + 1][segundaFila + 1] = false;
                        }
                    }catch (IndexOutOfBoundsException e){
                        Log.v("Error", "error al comer");
                    }

                    return movimientoBienRealizado = true;
                }
            }
        }

        return  movimientoBienRealizado = false;
    }




    private void dibujarPeonBlanca (Canvas canvas) {

        dibujo = getResources().getDrawable(R.mipmap.blancas, null);
        for(int i = 0; i < numFilas; i++) {
            for (int j = 0; j < numColumnas; j++) {
                if(piezaPeonBlanca[i][j]) {
                    dibujo.setBounds(i * celdaAnchura + poshorini, j * celdaAltura + posverini,
                            (i + 1) * celdaAnchura + poshorini, (j + 1) * celdaAltura + posverini);
                    dibujo.draw(canvas);
                }
            }
        }

    }


    private void dibujarDamaBlanca (Canvas canvas) {

        dibujo = getResources().getDrawable(R.mipmap.dama_blanca, null);
        for(int i = 0; i < numFilas; i++) {
            for (int j = 0; j < numColumnas; j++) {
                if(piezaDamaBlanca[i][j]) {
                    dibujo.setBounds(i * celdaAnchura + poshorini, j * celdaAltura + posverini,
                            (i + 1) * celdaAnchura + poshorini, (j + 1) * celdaAltura + posverini);
                    dibujo.draw(canvas);
                }
            }
        }

    }

    private void dibujarPeonNegro (Canvas canvas) {

        dibujo = getResources().getDrawable(R.mipmap.negras, null);
        for(int i = 0; i < numFilas; i++) {
            for (int j = 0; j < numColumnas; j++) {
                if(piezaPeonNegra[i][j]) {
                    dibujo.setBounds(i * celdaAnchura + poshorini, j * celdaAltura + posverini,
                            (i + 1) * celdaAnchura + poshorini, (j + 1) * celdaAltura + posverini);
                    dibujo.draw(canvas);
                }
            }
        }

    }


    private void dibujarDamaNegra (Canvas canvas) {

        dibujo = getResources().getDrawable(R.mipmap.dama_negra, null);
        for(int i = 0; i < numFilas; i++) {
            for (int j = 0; j < numColumnas; j++) {
                if(piezaDamaNegra[i][j]) {
                    dibujo.setBounds(i * celdaAnchura + poshorini, j * celdaAltura + posverini,
                            (i + 1) * celdaAnchura + poshorini, (j + 1) * celdaAltura + posverini);
                    dibujo.draw(canvas);
                }
            }
        }

    }

    private void comprobarCasillaOcupada(){

        for (int i = 0; i < numColumnas; i++){
            for(int j = 0; j < numFilas; j++){
                if(piezaPeonNegra[i][j] || piezaPeonBlanca[i][j] || piezaDamaBlanca[i][j] || piezaDamaNegra[i][j]){
                    casillaOcupada[i][j] = true;
                }else
                    casillaOcupada[i][j] = false;
            }
        }
    }

    public boolean contarPiezas(boolean[][] array){

        int contador = 0;
        for(int i = 0; i < numColumnas; i++){
            for(int j = 0; j < numFilas; j++){
                if(array[i][j])
                    contador++;
            }
        }

        if(contador == 0)
            return false;
        else
            return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int columna = (int) ((event.getX() - poshorini) / celdaAnchura);
            int fila = (int) ((event.getY() - posverini) / celdaAltura);

                try {
                    if ((columna + fila) % 2 == 1) {
                        if (contadorTouch == 0) {
                            casillaSeleccionada[columna][fila] = !casillaSeleccionada[columna][fila];
                            primeraColumna = columna;
                            primeraFila = fila;
                            switch (turnoJugador) {
                                case "Turno del jugador 1":
                                    if(piezaPeonBlanca[columna][fila] || piezaDamaBlanca[columna][fila]) {
                                        mostrarMovimientosBlancas(columna, fila);
                                        contadorTouch++;
                                    }
                                    else{
                                        casillaSeleccionada[columna][fila] = false;
                                    }
                                    break;
                                case "Turno del jugador 2":
                                    if(piezaPeonNegra[columna][fila] || piezaDamaNegra[columna][fila]){
                                        mostrarMovimientosNegras(columna, fila);
                                        contadorTouch++;
                                    }else{
                                        casillaSeleccionada[columna][fila] = false;
                                    }
                                    break;
                            }

                            //Toast.makeText(getContext(), "Estoy en la columna " + getColumnaString(columna) + getFilaString(fila), Toast.LENGTH_SHORT).show();
                        } else if (contadorTouch == 1) {
                            segundaColumna = columna;
                            segundaFila = fila;
                            switch (turnoJugador) {
                                case "Turno del jugador 1":
                                    if (realizarMovimientoBlancas(casposblanca)) {

                                        if(!contarPiezas(piezaPeonNegra))
                                            Toast.makeText(getContext(), "Jugador 2 HA GANADO", Toast.LENGTH_SHORT).show();

                                        turnoJugador = "Turno del jugador 2";
                                    } else {
                                        realizarMovimientoBlancas(casposblanca);
                                    }
                                    break;
                                case "Turno del jugador 2":
                                    if (realizarMovimientoNegras(casposnegra)) {

                                        if(!contarPiezas(piezaPeonBlanca))
                                            Toast.makeText(getContext(), "Jugador 2 HA GANADO", Toast.LENGTH_SHORT).show();
                                        turnoJugador = "Turno del jugador 1";
                                    } else {
                                        realizarMovimientoNegras(casposnegra);
                                    }
                                    break;
                            }

                            limpiarArray(casillaSeleccionada, casposblanca, casposnegra);
                            contadorTouch = 0;
                            Toast.makeText(getContext(), turnoJugador, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Se juegan por las casillas negras", Toast.LENGTH_SHORT).show();
                    }
                }catch (IndexOutOfBoundsException e){
                    //Log.v("array"," " +columna+fila);
                    //Toast.makeText(getContext(), "Estás tocando fuera del tablero", Toast.LENGTH_SHORT).show();
                }
            }
        invalidate();
        return true;
    }


    



    public void limpiarArray(boolean[][] array1, boolean[][] array2, boolean[][] array3){
        for(int i = 0; i < numFilas;i++){
            for(int j = 0; j < numColumnas; j++){
                array1[i][j] = false;
                array2[i][j] = false;
                array3[i][j] = false;
            }
        }
    }

    public String getColumnaString(int columna){
        switch (columna){
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

    public String getFilaString(int fila){
        return String.valueOf(fila + 1);
    }

}










