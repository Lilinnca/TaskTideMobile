package com.example.tasktide;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.util.Log;


public class EventoConfirmacao extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_evento_confirmacao);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void telaVoltarConfirmarIdentidade(View view) {
        Intent in = new Intent(EventoConfirmacao.this, EventoIdentidade.class);
        startActivity(in);
    }


    public void criarEvento(View view) {
        finishAffinity();
        Log.d("Confirmacao", "Evento criado com sucesso!");
    }
}
