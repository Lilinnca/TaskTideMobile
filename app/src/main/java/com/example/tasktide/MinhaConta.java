package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MinhaConta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);
    }

    public void telaperfil(View view) {
        Intent in = new Intent(MinhaConta.this, MinhaConta.class);
        startActivity(in);
    }

    public void telameuseventos(View view) {
    }

    public void telacriarevento(View view) {
        Intent in = new Intent(MinhaConta.this, CriarEvento.class);
        startActivity(in);
    }

    public void telafavoritos(View view) {
    }

    public void telainicial(View view) {
        Intent in = new Intent(MinhaConta.this, TelaInicial.class);
        startActivity(in);
    }

    public void meuscerticados(View view){
        Intent in = new Intent(MinhaConta.this, MeusCertificados.class);
        startActivity(in);
    }

    public void configuracoes(View view){
        Intent in = new Intent(MinhaConta.this, Configuracoes.class);
        startActivity(in);
    }

    public void sobrenos(View view){
        Intent in = new Intent(MinhaConta.this, SobreNos.class);
        startActivity(in);
    }
}