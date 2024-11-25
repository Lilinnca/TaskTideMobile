package com.example.tasktide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktide.DAO.DAO;

public class PagamentoEvento extends AppCompatActivity {

    private EditText txtValorPagamento;  // Altere para EditText
    private RadioGroup radioGroupPagamento;
    private Button btnConfirmarPagamento;
    private ProgressBar progressBar;

    private double valorEvento;
    private long eventoId;
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento_evento);

        txtValorPagamento = findViewById(R.id.editTextValorPagamento);
        radioGroupPagamento = findViewById(R.id.radioGroupPagamento);
        btnConfirmarPagamento = findViewById(R.id.btnConfirmarPagamento);
        progressBar = findViewById(R.id.progressBar);

        valorEvento = getIntent().getDoubleExtra("valorEvento", 0.0);
        eventoId = getIntent().getLongExtra("evento_id", -1);

        // Exibe o valor no EditText
        txtValorPagamento.setText("R$ " + valorEvento);

        btnConfirmarPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simularPagamento();
            }
        });
    }

    private void simularPagamento() {
        int selectedId = radioGroupPagamento.getCheckedRadioButtonId();
        RadioButton radioButtonSelecionado = findViewById(selectedId);

        if (radioButtonSelecionado == null) {
            Toast.makeText(this, "Escolha uma forma de pagamento", Toast.LENGTH_SHORT).show();
            return;
        }

        String formaPagamento = radioButtonSelecionado.getText().toString();
        Toast.makeText(this, "Processando pagamento via " + formaPagamento, Toast.LENGTH_SHORT).show();

        progressBar.setVisibility(View.VISIBLE);
        btnConfirmarPagamento.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                confirmarPagamento();
            }
        }, 3000);
    }

    private void confirmarPagamento() {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Pagamento confirmado! Inscrição realizada.", Toast.LENGTH_LONG).show();

        // Lógica de inscrição após confirmação de pagamento
        long usuarioId = 12345;  // Aqui você deve pegar o ID do usuário autenticado (exemplo fictício)

        if (eventoId != -1) {
            // Verifica se o usuário já está inscrito
            if (dao.verificarInscricao(usuarioId, eventoId)) {
                Toast.makeText(this, "Você já está inscrito neste evento!", Toast.LENGTH_SHORT).show();
            } else {
                // Realiza a inscrição no evento
                dao.inscreverNoEvento(usuarioId, eventoId);
                Toast.makeText(this, "Inscrição realizada com sucesso!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Evento não encontrado!", Toast.LENGTH_SHORT).show();
        }

        // Redireciona para a tela de Meus Eventos
        Intent intent = new Intent(this, MeusEventos.class);
        startActivity(intent);
        finish();
    }
}

