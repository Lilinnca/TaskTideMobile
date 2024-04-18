package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EventoInformacoes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_informacoes);
    }

    public void eventoparticipantex(View view) {
        Intent in = new Intent(EventoInformacoes.this, EventoParticipante.class);
                startActivity(in);
    }

    public void eventoidentidade(View view) {
        Intent in = new Intent(EventoInformacoes.this, EventoIdentidade.class);
        startActivity(in);
    }

}