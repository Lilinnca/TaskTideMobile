package com.example.tasktide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Usuario;

import java.text.DecimalFormat;

public class PagamentoEvento extends AppCompatActivity {

    private EditText txtValorPagamento;
    private Button btnConfirmarPagamento;
    private ProgressBar progressBar;
    private RadioButton rbtnBoletoPagamento, rbtnPixPagamento;
    private double valorEvento;
    private long usuarioId;
    private long eventoId;
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento_evento);

        // Inicializa o DAO
        dao = new DAO(this);

        txtValorPagamento = findViewById(R.id.editTextValorPagamento);
        btnConfirmarPagamento = findViewById(R.id.btnConfirmarPagamento);
        progressBar = findViewById(R.id.progressBar);

        rbtnBoletoPagamento = findViewById(R.id.rbtnBoletoPagamento);
        rbtnPixPagamento = findViewById(R.id.rbtnPixPagamento);

        Button btnIncrementar = findViewById(R.id.btnIncrementar);
        Button btnDiscremento = findViewById(R.id.btnDiscremento);
        final EditText edtxtEscolherQuantIngressos = findViewById(R.id.edtxtEscolherQuantIngressos);

        txtValorPagamento.setEnabled(false);
        edtxtEscolherQuantIngressos.setEnabled(false);

        btnIncrementar.setOnClickListener(v -> {
            String text = edtxtEscolherQuantIngressos.getText().toString();
            int currentValue = 1;
            if (!text.isEmpty()) {
                currentValue = Integer.parseInt(text);
            }
            edtxtEscolherQuantIngressos.setText(String.valueOf(currentValue + 1));
            atualizarValorEvento();
        });

        btnDiscremento.setOnClickListener(v -> {
            String text = edtxtEscolherQuantIngressos.getText().toString();
            int currentValue = 0;
            if (!text.isEmpty()) {
                currentValue = Integer.parseInt(text);
            }
            if (currentValue > 0) {
                edtxtEscolherQuantIngressos.setText(String.valueOf(currentValue - 1));
                atualizarValorEvento();
            }
        });

        // Recebe os valores via Intent
        Intent intent = getIntent();
        valorEvento = intent.getDoubleExtra("VALOR_EVENTO", 0.0);
        eventoId = intent.getLongExtra("evento_id", -1);

        // Verifica se o eventoId foi recebido corretamente
        if (eventoId == -1) {
            Toast.makeText(this, "Erro: ID do evento não encontrado!", Toast.LENGTH_SHORT).show();
        }

        usuarioId = getUsuarioId();

        // Exibe o valor do evento inicialmente no campo
        DecimalFormat df = new DecimalFormat("0.00");
        txtValorPagamento.setText(df.format(valorEvento));

        // Seleção de forma de pagamento
        rbtnBoletoPagamento.setOnClickListener(v -> {
            if (rbtnBoletoPagamento.isChecked()) {
                rbtnPixPagamento.setChecked(false);
            }
        });

        rbtnPixPagamento.setOnClickListener(v -> {
            if (rbtnPixPagamento.isChecked()) {
                rbtnBoletoPagamento.setChecked(false);
            }
        });

        btnConfirmarPagamento.setOnClickListener(v -> simularPagamento());
    }

    private void atualizarValorEvento() {
        EditText edtxtEscolherQuantIngressos = findViewById(R.id.edtxtEscolherQuantIngressos);
        String ingressosText = edtxtEscolherQuantIngressos.getText().toString();
        int quantidadeIngressos = 1;

        if (!ingressosText.isEmpty()) {
            quantidadeIngressos = Integer.parseInt(ingressosText);
        }

        double valorTotal = valorEvento * quantidadeIngressos;

        // Atualiza o valor no EditText
        DecimalFormat df = new DecimalFormat("0.00");
        txtValorPagamento.setText(df.format(valorTotal));
    }

    private void simularPagamento() {
        if (!rbtnBoletoPagamento.isChecked() && !rbtnPixPagamento.isChecked()) {
            Toast.makeText(this, "Escolha uma forma de pagamento", Toast.LENGTH_SHORT).show();
            return;
        }

        String formaPagamento = rbtnBoletoPagamento.isChecked() ? "Boleto Bancário" : "Pix";

        Toast.makeText(this, "Processando pagamento via " + formaPagamento, Toast.LENGTH_SHORT).show();

        progressBar.setVisibility(View.VISIBLE);
        btnConfirmarPagamento.setEnabled(false);

        new Handler().postDelayed(() -> {
            confirmarPagamento();
            inscreverUsuarioNoEvento();
        }, 3000);
    }

    private void inscreverUsuarioNoEvento() {
        if (eventoId == -1) {
            Toast.makeText(PagamentoEvento.this, "Erro: Evento não encontrado!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dao.verificarInscricao(usuarioId, eventoId)) {
            Toast.makeText(PagamentoEvento.this, "Você já está inscrito neste evento!", Toast.LENGTH_SHORT).show();
        } else {
            try {
                dao.inscreverNoEvento(usuarioId, eventoId);
                Toast.makeText(PagamentoEvento.this, "Pagamento confirmado! Inscrição realizada.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(PagamentoEvento.this, "Erro ao realizar a inscrição: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        // Reabilita o botão após a inscrição
        new Handler().postDelayed(() -> btnConfirmarPagamento.setEnabled(true), 1000);
    }

    private long getUsuarioId() {
        String usuarioEmail = getEmailUsuario();
        if (usuarioEmail == null) {
            return -1;
        }

        Usuario usuario = dao.buscarUsuarioPorEmail(usuarioEmail);
        return usuario != null ? usuario.getId() : -1;
    }

    private String getEmailUsuario() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getString("email", null);
    }

    private void confirmarPagamento() {
        progressBar.setVisibility(View.GONE);

        Intent intent = new Intent(this, TelaInicial.class);
        startActivity(intent);
    }
}
