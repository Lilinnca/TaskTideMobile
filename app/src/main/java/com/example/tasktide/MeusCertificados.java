package com.example.tasktide;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MeusCertificados extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_certificados);
    }


    public void telainicial(View view) {
        Intent in = new Intent(MeusCertificados.this, TelaInicial.class);
        startActivity(in);
    }


    public void telacriarevento(View view) {
        Intent in = new Intent(MeusCertificados.this, CriarEvento.class);
        startActivity(in);
    }


    public void telaperfil(View view) {
        Intent in = new Intent(MeusCertificados.this, MinhaConta.class);
        startActivity(in);
    }
}


