package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EventoInfoTelaInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_info_tela_inicial);
    }

    public void notificacao(View view){
    }
    public void voltarInfoInicial(View view){
        Intent in = new Intent(this, TelaInicial.class);
        startActivity(in);
    }

    public void inscreverInfoInicial(View view) {
        Toast.makeText(this, "Você se inscreveu no evento!", Toast.LENGTH_SHORT).show();
    }
}
