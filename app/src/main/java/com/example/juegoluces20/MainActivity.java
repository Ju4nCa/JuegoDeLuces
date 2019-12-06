package com.example.juegoluces20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity{
    ConstraintLayout main;
    GridLayout malla;
    Button botones[][];
    int row=3, colum=3;
    Boolean bandera=true;
    TextView cronometro;
    Thread tiempo;
    Runnable hilo;
    boolean reiniciar=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main= (ConstraintLayout)findViewById(R.id.main);
        main.setBackgroundColor(Color.BLACK);
        malla= (GridLayout)findViewById(R.id.malla);
        cronometro=(TextView)findViewById(R.id.cronometro);

        malla.removeAllViews();
        generarBotones(row,colum);

        hilo = new Runnable() {
            @Override
            public void run() {
                try {
                    iniciar();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        tiempo = new Thread(hilo);
    }


    public void generarBotones(int row, int colum){

        malla.setRowCount(row);
        malla.setColumnCount(colum);
        botones= new Button[row][colum];
        RelativeLayout.LayoutParams param;


        for(int a=0;a<row;a++){
            for(int b=0;b<row;b++) {
                Button boton = new Button(this);
                boton.setTextColor(Color.RED);
                boton.setBackgroundColor(Color.RED);

                param = new RelativeLayout.LayoutParams(0,0);
                param.width = (getResources().getDisplayMetrics().widthPixels)/(row+3);
                param.height = (getResources().getDisplayMetrics().heightPixels)/(colum+4);
                param.setMargins(5,5,5,5);
                boton.setLayoutParams(param);

                malla.addView(boton);
                botones[a][b]=boton;
                cambiarLuces(a,b);
            }
        }

    }


    public void cambiarLuces(final int i, final int j){

        botones[i][j].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.setBackgroundColor(Color.BLACK);
                cronometro.setTextColor(Color.WHITE);
                if(reiniciar){
                    tiempo.start();
                }
                if(i!= 0 && j!=0 && i!= row-1 && j!=colum-1) { //CUADROS EN LOS CENTROS

                    if (i==(row-1)/2 && j==(colum-1)/2){
                        cambio(botones[i][j]);
                        cambio(botones[i][j+1]);
                        cambio(botones[i-1][j]);
                        cambio(botones[i+1][j]);
                        cambio(botones[i][j-1]);
                    }else {
                        cambio(botones[i][j]);
                        cambio(botones[i][j + 1]);
                        cambio(botones[i - 1][j]);
                        cambio(botones[i + 1][j]);
                        cambio(botones[i][j - 1]);
                        cambio(botones[i + 1][j + 1]);
                        cambio(botones[i - 1][j - 1]);
                        cambio(botones[i + 1][j - 1]);
                        cambio(botones[i - 1][j + 1]);
                    }

                }
                if(i==0 && j ==0){  //ESQUINA SUPERIOR IZQUIERDA
                    cambio(botones[i][j]);
                    cambio(botones[i][j+1]);
                    cambio(botones[i+1][j]);
                    cambio(botones[i+1][j+1]);
                }
                if(i==row-1 && j==colum-1){    //ESQUINA INFERIOR DERECHA
                    cambio(botones[i][j]);
                    cambio(botones[i][j-1]);
                    cambio(botones[i-1][j]);
                    cambio(botones[i-1][j-1]);
                }
                if(i==row-1 && j ==0){      //ESQUINA INFERIOR IZQUIERDA
                    cambio(botones[i][j]);
                    cambio(botones[i][j+1]);
                    cambio(botones[i-1][j]);
                    cambio(botones[i-1][j+1]);
                }
                if(i==0 && j ==colum-1){    //ESQUINA SUPERIOR ISQUIERDA
                    cambio(botones[i][j]);
                    cambio(botones[i][j-1]);
                    cambio(botones[i+1][j]);
                    cambio(botones[i+1][j-1]);
                }
                if(i!=0 && i!=row-1 && j ==0){    //LATERAL IZQUIERDO
                    cambio(botones[i-1][j]);
                    cambio(botones[i][j]);
                    cambio(botones[i+1][j]);
                }
                if(i==0 && j !=0 &&  j!=colum-1){    //LATERAL SUPERIOR
                    cambio(botones[i][j-1]);
                    cambio(botones[i][j]);
                    cambio(botones[i][j+1]);
                }
                if(i!=0 && i!=row-1 && j ==colum-1){  //LATERAL DERECHO
                    cambio(botones[i-1][j]);
                    cambio(botones[i][j]);
                    cambio(botones[i+1][j]);
                }
                if(i==row-1 && j!=colum-1 && j !=0){  //LATERAL INFERIOR
                    cambio(botones[i][j-1]);
                    cambio(botones[i][j]);
                    cambio(botones[i][j+1]);
                }
                reiniciar=false;
                try {
                    comprobar();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void cambio(Button boton){
        if(boton.getText()!="*"){
            boton.setBackgroundColor(Color.BLACK);
            boton.setTextColor(Color.BLACK);
            boton.setText("*");
        }else{
            boton.setText("/");
            boton.setTextColor(Color.RED);
            boton.setBackgroundColor(Color.RED);
        }
    }

    @SuppressLint("Range")
    public void comprobar() throws InterruptedException {
        if(botones[(row-1)/2][(colum-1)/2].getText()=="/")
            for (int i=0;i<row;i++){
                for(int j=0;j<colum;j++){
                    if(i==(row-1)/2 && j==(colum-1)/2){
                        j++;
                    }
                    if(botones[i][j].getText()!="*"){
                        bandera=false;
                        j=colum;
                        i=row;
                    }
                }
            }
        if(botones[(row-1)/2][(colum-1)/2].getText()=="/" && bandera)
        {
            main.setBackgroundColor(Color.WHITE);
            cronometro.setTextColor(Color.BLACK);
            if(row<=7)
            {
                row+=2;
                colum+=2;
                malla.removeAllViews();
                generarBotones(row,colum);
            }else {
                tiempo.interrupt();
                tiempo = new Thread(hilo);
                reiniciar=true;
                row=3;
                colum=3;
                malla.removeAllViews();
                generarBotones(row,colum);
            }


        }
        bandera=true;
    }

    private void iniciar() throws InterruptedException {
            for (int a=0; a<60;a++)
            {
                for(int b=0; b<60;b++){
                        cronometro.setText(String.format("%02d : %02d",a, b));
                        Thread.sleep(1000);
                }
                cronometro.setText(String.format("%02d : 00", a));
            }
        cronometro.setText("NO LO LOGRASTE!!");
        }
}

