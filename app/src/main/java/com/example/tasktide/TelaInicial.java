package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TelaInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);
    }

    public void telaperfil(View view) {
        Intent in = new Intent(TelaInicial.this, MinhaConta.class);
        startActivity(in);
    }

    public void telameuseventos(View view) {
    }

    public void telacriarevento(View view) {
        Intent in = new Intent(TelaInicial.this, CriarEvento.class);
        startActivity(in);
    }

    public void telafavoritos(View view) {
    }

    public void telainicial(View view) {
        Intent in = new Intent(TelaInicial.this, TelaInicial.class);
        startActivity(in);
    }
}