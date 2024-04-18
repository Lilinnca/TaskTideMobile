package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EventoConfirmacao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_confirmacao);
    }

    public void eventoidentidadex(View view) {
        Intent in = new Intent(EventoConfirmacao.this, EventoIdentidade.class);
        startActivity(in);
    }
}