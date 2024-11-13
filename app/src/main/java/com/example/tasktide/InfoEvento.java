package com.example.tasktide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class InfoEvento extends AppCompatActivity {

    private TextView tvQuantidade;
    private int quantidade = 0; // Quantidade inicial de ingressos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_evento);

        // Vincula os elementos do layout com as variáveis locais
        tvQuantidade = findViewById(R.id.txtQuantidadeInfoEvento);
        Button btnIncrease = findViewById(R.id.btnAdicionarInfoEvento);
        Button btnDecrease = findViewById(R.id.btnRemoverInfoEvento);

        // Configura o listener para o botão de aumentar
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aumentarQuantidade();
            }
        });

        // Configura o listener para o botão de diminuir
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diminuirQuantidade();
            }
        });
    }

    // Método para aumentar a quantidade de ingressos
    private void aumentarQuantidade() {
        quantidade++;
        atualizarQuantidade();
    }

    // Método para diminuir a quantidade de ingressos
    private void diminuirQuantidade() {
        if (quantidade > 0) {
            quantidade--;
            atualizarQuantidade();
        }
    }

    // Atualiza o TextView com a nova quantidade de ingressos
    private void atualizarQuantidade() {
        tvQuantidade.setText(String.valueOf(quantidade));
    }

    public void telaperfil(View view) {
        Intent in = new Intent(InfoEvento.this, MinhaConta.class);
        startActivity(in);
    }


    public void avancarpagamento(View view){
        Intent in = new Intent(InfoEvento.this, PagamentoEvento.class);
        startActivity(in);
    }


    public void telameuseventos(View view) {
    }

    public void telacriarevento(View view) {
        Intent in = new Intent(InfoEvento.this, CriarEvento.class);
        startActivity(in);
    }

    public void telafavoritos(View view) {
    }

    public void telainicial(View view) {
        Intent in = new Intent(InfoEvento.this, TelaInicial.class);
        startActivity(in);
    }

    public void telameuseventosb(View view){
        Intent in = new Intent(InfoEvento.this, MeusEventos.class);
        startActivity(in);
    }
}