package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void telaboasvindas(View view) {
        Intent in = new Intent(Login.this, TelaBoasVindas.class);
        startActivity(in);
    }

    public void telainicial(View view) {
        Intent in = new Intent(Login.this, TelaInicial.class);
        startActivity(in);
    }
}