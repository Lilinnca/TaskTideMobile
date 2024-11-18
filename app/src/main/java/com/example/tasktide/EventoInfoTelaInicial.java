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
    private long usuarioId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_info_tela_inicial);


        txtNomeEventoInfo = findViewById(R.id.txtNomeEventoInfo);
        txtLocalEventoInfo = findViewById(R.id.txtLocalEventoInfo);
        txtDataEventoInfo = findViewById(R.id.txtDataEventoInfo);
        txtDescricaoEventoInfo = findViewById(R.id.txtDescricaoEventoInfo);


        dao = new DAO(this);


        Intent intent = getIntent();
        long eventoId = intent.getLongExtra("evento_id", -1);


        usuarioId = getUsuarioId();


        Evento evento = dao.buscarEventoPorId(eventoId);
        if (evento != null) {
            txtNomeEventoInfo.setText(evento.getNomeEvento());
            txtDescricaoEventoInfo.setText(evento.getDescricao());

            String[] informacoes = dao.buscarInformacoesPorEvento(eventoId);
            txtDataEventoInfo.setText("De " + formatarData(informacoes[0]) + " até " + formatarData(informacoes[1]));
            txtLocalEventoInfo.setText(!informacoes[2].isEmpty() ? informacoes[2] : "Local não definido");
        }

    }


    public void inscreverInfoInicial(View view) {
        view.setEnabled(false);


        Intent intent = getIntent();
        long eventoId = intent.getLongExtra("evento_id", -1);


        if (eventoId != -1) {
            if (dao.verificarInscricao(usuarioId, eventoId)) {
                Toast.makeText(this, "Você já está inscrito neste evento!", Toast.LENGTH_SHORT).show();
            } else {
                dao.inscreverNoEvento(usuarioId, eventoId);
                Toast.makeText(this, "Inscrição realizada com sucesso!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Evento não encontrado!", Toast.LENGTH_SHORT).show();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 1000);
    }


    private long getUsuarioId() {
        String usuarioEmail = getEmailUsuario();


        if (usuarioEmail == null) {
            return -1;
        }


        Usuario usuario = dao.buscarUsuarioPorEmail(usuarioEmail);


        if (usuario != null) {
            return usuario.getId();
        } else {
            return -1;
        }
    }


    private String getEmailUsuario() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getString("email", null);
    }


    private String formatarData(String data) {
        if (data == null || data.isEmpty()) {
            return "Data inválida";
        }
        try {
            // Verifique se o formato da data é "dd/MM/yyyy"
            SimpleDateFormat sdfEntrada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = sdfEntrada.parse(data);  // Parse a data no formato esperado

            SimpleDateFormat sdfSaida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdfSaida.format(date);  // Retorne a data formatada
        } catch (Exception e) {
            e.printStackTrace();
            return "Data inválida";
        }
    }

}