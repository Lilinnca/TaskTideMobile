package com.example.tasktide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EventoConfirmacao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_confirmacao);

        Button btnCriarEvento = findViewById(R.id.btnCriarEvento);
        btnCriarEvento.setOnClickListener(v -> criarEvento());

    }

    public void voltarConfirmacao(View view){
        Intent in = new Intent(this, EventoParticipante.class);
        startActivity(in);
    }

    public void criarEvento() {
        Toast.makeText(this, "Evento criado com sucesso!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, TelaInicial.class);
        startActivity(intent);
        finish();
    }
}

