package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EventoParticipante extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_participante);
    }

    public void criarevento(View view) {
        Intent in = new Intent(EventoParticipante.this, CriarEvento.class);
        startActivity(in);
    }

    public void eventoinformacoes(View view) {
        Intent in = new Intent(EventoParticipante.this, EventoInformacoes.class);
        startActivity(in);
    }
}