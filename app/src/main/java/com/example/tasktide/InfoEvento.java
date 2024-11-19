package com.example.tasktide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Evento;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InfoEvento extends AppCompatActivity {

    private TextView tvQuantidade, txtNomeEventoInfo, txtLocalEventoInfo, txtDataEventoInfo, txtDescricaoEventoInfo;
    private int quantidade = 0; // Quantidade inicial de ingressos
    private DAO dao;
    private double valorEvento = 0.0; // Valor do evento

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_evento);

        // Vincula os elementos do layout com as variáveis locais
        tvQuantidade = findViewById(R.id.txtQuantidadeInfoEvento);
        Button btnIncrease = findViewById(R.id.btnAdicionarInfoEvento);
        Button btnDecrease = findViewById(R.id.btnRemoverInfoEvento);

        txtNomeEventoInfo = findViewById(R.id.txtNomeEventoInfo);
        txtLocalEventoInfo = findViewById(R.id.txtLocalEventoInfo);
        txtDataEventoInfo = findViewById(R.id.txtDataEventoInfo);
        txtDescricaoEventoInfo = findViewById(R.id.txtDescricaoEventoInfo);

        dao = new DAO(this);

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

        // Carregar informações do evento
        Intent intent = getIntent();
        long eventoId = intent.getLongExtra("evento_id", -1);
        carregarDadosEvento(eventoId);
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

    // Carrega os dados do evento
    private void carregarDadosEvento(long eventoId) {
        if (eventoId == -1) {
            Toast.makeText(this, "Erro ao carregar informações do evento!", Toast.LENGTH_SHORT).show();
            return;
        }

        Evento evento = dao.buscarEventoPorId(eventoId);
        if (evento != null) {
            txtNomeEventoInfo.setText(evento.getNomeEvento());
            txtDescricaoEventoInfo.setText(evento.getDescricao());

            String[] informacoes = dao.buscarInformacoesPorEvento(eventoId);
            txtDataEventoInfo.setText("De " + formatarData(informacoes[0]) + " até " + formatarData(informacoes[1]));
            txtLocalEventoInfo.setText(!informacoes[2].isEmpty() ? informacoes[2] : "Local não definido");

            // Obtendo o valor do evento
            try {
                valorEvento = Double.parseDouble(informacoes[3]); // informacoes[3] contém o valor do evento
            } catch (NumberFormatException e) {
                valorEvento = 0.0; // Caso haja erro na conversão
            }
        } else {
            Toast.makeText(this, "Evento não encontrado!", Toast.LENGTH_SHORT).show();
        }
    }

    // Formata a data para exibição
    private String formatarData(String data) {
        if (data == null || data.isEmpty()) {
            return "Data inválida";
        }
        try {
            SimpleDateFormat sdfEntrada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = sdfEntrada.parse(data);

            SimpleDateFormat sdfSaida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdfSaida.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Data inválida";
        }
    }

    // Método para avançar para a tela de pagamento
    public void avancarpagamento(View view) {
        Intent intent = getIntent();
        long eventoId = intent.getLongExtra("evento_id", -1); // Recupera o ID do evento

        if (eventoId != -1 && quantidade > 0) {
            Intent pagamentoIntent = new Intent(this, PagamentoEvento.class);

            // Passa informações necessárias para a tela de pagamento
            pagamentoIntent.putExtra("evento_id", eventoId);
            pagamentoIntent.putExtra("nome_evento", txtNomeEventoInfo.getText().toString());
            pagamentoIntent.putExtra("valor_evento", valorEvento); // Valor do evento obtido de Informações
            pagamentoIntent.putExtra("quantidade", quantidade); // Quantidade de ingressos
            startActivity(pagamentoIntent);
        } else if (quantidade == 0) {
            Toast.makeText(this, "Selecione pelo menos 1 ingresso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao obter informações do evento!", Toast.LENGTH_SHORT).show();
        }
    }

    // Métodos de navegação entre telas
    public void inicialIE(View view) {
        Intent in = new Intent(this, TelaInicial.class);
        startActivity(in);
    }

    public void localizacaoIE(View view) {
        Intent in = new Intent(this, Localizacao.class);
        startActivity(in);
    }

    public void addEventoIE(View view) {
        Intent in = new Intent(this, CriarEvento.class);
        startActivity(in);
    }

    public void meusEventosIE(View view) {
        Intent in = new Intent(this, MeusEventosParticipante.class);
        startActivity(in);
    }

    public void perfilIE(View view) {
        Intent in = new Intent(this, MinhaConta.class);
        startActivity(in);
    }
}
