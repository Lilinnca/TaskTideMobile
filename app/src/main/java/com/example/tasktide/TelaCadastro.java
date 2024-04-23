package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TelaCadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);
    }

    public void telaprimeirospassos(View view) {
        Intent in = new Intent(TelaCadastro.this, TelaPrimeirosPassos.class);
        startActivity(in);
    }

    public void telaboasvindas(View view) {
        Intent in = new Intent(TelaCadastro.this, TelaBoasVindas.class);
        startActivity(in);
    }
}