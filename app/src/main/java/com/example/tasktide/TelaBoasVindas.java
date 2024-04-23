package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TelaBoasVindas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_boas_vindas);
    }

    public void telaprimeirospassos(View view) {
        Intent in = new Intent(TelaBoasVindas.this, TelaPrimeirosPassos.class);
        startActivity(in);
    }

    public void telalogin(View view) {
        Intent in = new Intent(TelaBoasVindas.this, Login.class);
        startActivity(in);
    }
}