package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InfoCertificado extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_certificado);
    }

    public void voltaCertificado(View view){
        Intent in = new Intent(InfoCertificado.this, MeusCertificados.class);
        startActivity(in);
    }
}