package com.example.tasktide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PagamentoEvento extends AppCompatActivity {

    private TextView txtValorPagamento;
    private RadioGroup radioGroupPagamento;
    private Button btnConfirmarPagamento;
    private ProgressBar progressBar;

    private double valorEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento_evento);

        // Vincula os componentes da tela
        txtValorPagamento = findViewById(R.id.txtValorPagamento);
        radioGroupPagamento = findViewById(R.id.radioGroupPagamento);
        btnConfirmarPagamento = findViewById(R.id.btnConfirmarPagamento);
        progressBar = findViewById(R.id.progressBar);

        // Recebe o valor do evento passado pela Intent
        valorEvento = getIntent().getDoubleExtra("valorEvento", 0.0);
        txtValorPagamento.setText("Valor: R$ " + valorEvento);

        // Ação ao clicar no botão Confirmar Pagamento
        btnConfirmarPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simularPagamento();
            }
        });
    }

    // Simula o pagamento com um tempo de espera
    private void simularPagamento() {
        int selectedId = radioGroupPagamento.getCheckedRadioButtonId();
        RadioButton radioButtonSelecionado = findViewById(selectedId);

        if (radioButtonSelecionado == null) {
            Toast.makeText(this, "Escolha uma forma de pagamento", Toast.LENGTH_SHORT).show();
            return;
        }

        String formaPagamento = radioButtonSelecionado.getText().toString();
        Toast.makeText(this, "Processando pagamento via " + formaPagamento, Toast.LENGTH_SHORT).show();

        // Exibe a ProgressBar e desabilita o botão
        progressBar.setVisibility(View.VISIBLE);
        btnConfirmarPagamento.setEnabled(false);

        // Simula um tempo de espera de 3 segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                confirmarPagamento();
            }
        }, 3000);
    }

    // Confirma o pagamento e redireciona para a tela de "Meus Eventos"
    private void confirmarPagamento() {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Pagamento confirmado! Inscrição realizada.", Toast.LENGTH_LONG).show();

        // Redireciona para a tela de Meus Eventos
        Intent intent = new Intent(this, MeusEventos.class);
        startActivity(intent);
        finish();
    }
}
