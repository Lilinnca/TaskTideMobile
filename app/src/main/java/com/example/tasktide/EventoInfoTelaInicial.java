package com.example.tasktide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Evento;
import com.example.tasktide.Objetos.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventoInfoTelaInicial extends AppCompatActivity {

    private TextView txtNomeEventoInfo, txtLocalEventoInfo, txtDataEventoInfo, txtDescricaoEventoInfo;
    private DAO dao;
    private long usuarioId; // Variável para armazenar o ID do usuário

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_info_tela_inicial);

        // Inicializar as views
        txtNomeEventoInfo = findViewById(R.id.txtNomeEventoInfo);
        txtLocalEventoInfo = findViewById(R.id.txtLocalEventoInfo);
        txtDataEventoInfo = findViewById(R.id.txtDataEventoInfo);
        txtDescricaoEventoInfo = findViewById(R.id.txtDescricaoEventoInfo);

        // Inicializar o DAO
        dao = new DAO(this);

        // Recuperar o ID do evento passado pela Intent
        Intent intent = getIntent();
        long eventoId = intent.getLongExtra("evento_id", -1); // Recuperar o ID do evento

        // Aqui você deve obter o ID do usuário, pode ser salvo em algum lugar ao fazer login
        usuarioId = getUsuarioId(); // Supondo que você tenha uma maneira de pegar o ID do usuário logado

        // Buscar o evento do banco de dados
        Evento evento = dao.buscarEventoPorId(eventoId);
        if (evento != null) {
            // Preencher os TextView com as informações do evento
            txtNomeEventoInfo.setText(evento.getNomeEvento());
            txtDescricaoEventoInfo.setText(evento.getDescricao());

            // Informações sobre a data e o local
            String[] informacoes = dao.buscarInformacoesPorEvento(eventoId);
            txtDataEventoInfo.setText("De " + formatarData(informacoes[0]) + " até " + formatarData(informacoes[1]));
            txtLocalEventoInfo.setText(informacoes[2]);
        }
    }

    public void inscreverInfoInicial(View view) {
        // Desabilitar o botão para evitar múltiplos cliques
        view.setEnabled(false);

        // Recuperar o ID do evento (já foi passado pela Intent)
        Intent intent = getIntent();
        long eventoId = intent.getLongExtra("evento_id", -1);

        if (eventoId != -1) {
            // Verificar se o usuário já está inscrito no evento
            if (dao.verificarInscricao(usuarioId, eventoId)) {
                // Usuário já está inscrito
                Toast.makeText(this, "Você já está inscrito neste evento!", Toast.LENGTH_SHORT).show();
            } else {
                // Se não estiver inscrito, realizar a inscrição
                dao.inscreverNoEvento(usuarioId, eventoId);
                Toast.makeText(this, "Inscrição realizada com sucesso!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Caso o ID do evento não seja válido
            Toast.makeText(this, "Evento não encontrado!", Toast.LENGTH_SHORT).show();
        }

        // Reabilitar o botão após um curto tempo (para evitar cliques subsequentes rapidamente)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 1000); // 1000ms = 1 segundo de espera
    }

    private long getUsuarioId() {
        // Obter o e-mail do usuário logado a partir do SharedPreferences
        String usuarioEmail = getEmailUsuario();

        if (usuarioEmail == null) {
            // Caso o e-mail não esteja disponível (usuário não logado)
            return -1; // ID inválido
        }

        // Buscar o usuário no banco de dados utilizando o e-mail
        Usuario usuario = dao.buscarUsuarioPorEmail(usuarioEmail);

        if (usuario != null) {
            // Retorna o ID do usuário encontrado
            return usuario.getId();
        } else {
            // Caso o usuário não seja encontrado
            return -1; // ID inválido
        }
    }

    private String getEmailUsuario() {
        // Obter o e-mail do usuário logado a partir do SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getString("email", null); // Retorna null caso o e-mail não esteja salvo
    }



    private String formatarData(String data) {
        if (data == null || data.isEmpty()) {
            return "Data inválida";
        }
        try {
            SimpleDateFormat sdfEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdfEntrada.parse(data);
            SimpleDateFormat sdfSaida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdfSaida.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Data inválida";
        }
    }
}
