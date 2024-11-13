package com.example.tasktide;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EventoConfirmacao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_confirmacao);

        Button btnCriarEvento = findViewById(R.id.btnCriarEvento);
        btnCriarEvento.setOnClickListener(v -> criarEvento());
    }

    public void criarEvento() {
        // Exibe uma mensagem ao criar o evento
        Toast.makeText(this, "Evento criado com sucesso!", Toast.LENGTH_SHORT).show();

        // Cria a intenção para ir para a TelaInicial
        Intent intent = new Intent(EventoConfirmacao.this, TelaInicial.class);

        // Você pode passar informações adicionais, como o ID do evento, se necessário
        intent.putExtra("EVENTO_NOME", "Novo Evento");

        // Inicia a TelaInicial
        startActivity(intent);

        // Finaliza a atividade atual
        finish();
    }
}
