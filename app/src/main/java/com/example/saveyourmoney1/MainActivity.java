package com.example.saveyourmoney1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Método para dar el salto a la pantalla Inicio Sesión
    public void irInicioSesion(View view){
        Intent i = new Intent(this, InicioSesion.class);
        startActivity(i);
    }

    //Método para dar el salto a la pantala Registro.
    public void irRegistro(View view){
        Intent i = new Intent(this, Registro.class);
        startActivity(i);
    }
}