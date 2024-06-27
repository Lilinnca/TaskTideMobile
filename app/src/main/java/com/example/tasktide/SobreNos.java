package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SobreNos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_nos);
    }

    public void minhacontasn(View view) {
        Intent in = new Intent(SobreNos.this, MinhaConta.class);
        startActivity(in);
    }
}