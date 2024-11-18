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

        txtValorPagamento = findViewById(R.id.txtValorPagamento);
        radioGroupPagamento = findViewById(R.id.radioGroupPagamento);
        btnConfirmarPagamento = findViewById(R.id.btnConfirmarPagamento);
        progressBar = findViewById(R.id.progressBar);

        valorEvento = getIntent().getDoubleExtra("valorEvento", 0.0);
        txtValorPagamento.setText("Valor: R$ " + valorEvento);

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

        // Redireciona para a tela de Meus Eventos
        Intent intent = new Intent(this, MeusEventos.class);
        startActivity(intent);
        finish();
    }
}
