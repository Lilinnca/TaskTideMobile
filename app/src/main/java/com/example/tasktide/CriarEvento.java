package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CriarEvento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_evento);
    }

    public void eventoparticipante(View view) {
        Intent in = new Intent(CriarEvento.this, EventoParticipante.class);
        startActivity(in);
    }

    public void telainicial(View view) {
        Intent in = new Intent(CriarEvento.this, TelaInicial.class);
        startActivity(in);
    }
}