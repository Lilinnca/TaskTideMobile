package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TelaPrimeirosPassos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_primeiros_passos);
    }

    public void telacadastro(View view) {
        Intent in = new Intent(TelaPrimeirosPassos.this, TelaCadastro.class);
        startActivity(in);
    }

    public void telaboasvindas(View view){
        Intent in = new Intent(TelaPrimeirosPassos.this, TelaBoasVindas.class);
        startActivity(in);
    }
}