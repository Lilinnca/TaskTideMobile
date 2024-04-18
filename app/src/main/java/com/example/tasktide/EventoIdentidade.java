package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EventoIdentidade extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_identidade);
    }

    public void eventoinformacoesx(View view) {
        Intent in = new Intent(EventoIdentidade.this, EventoInformacoes.class);
        startActivity(in);
    }

    public void eventoconfirmacao(View view) {
        Intent in = new Intent(EventoIdentidade.this, EventoConfirmacao.class);
        startActivity(in);
    }
}