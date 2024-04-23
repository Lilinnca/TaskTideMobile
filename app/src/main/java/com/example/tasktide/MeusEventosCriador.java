package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MeusEventosCriador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_eventos_criador);
    }

    public void telaperfil(View view) {
        Intent in = new Intent(MeusEventosCriador.this, MinhaConta.class);
        startActivity(in);
    }

    public void telameuseventos(View view) {
        Intent in = new Intent(MeusEventosCriador.this, MeusEventos.class);
        startActivity(in);
    }

    public void telacriarevento(View view) {
        Intent in = new Intent(MeusEventosCriador.this, CriarEvento.class);
        startActivity(in);
    }

    public void telafavoritos(View view) {
    }

    public void telainicial(View view) {
        Intent in = new Intent(MeusEventosCriador.this, TelaInicial.class);
        startActivity(in);
    }
}