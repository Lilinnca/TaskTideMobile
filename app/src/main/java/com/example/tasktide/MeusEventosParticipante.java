package com.example.tasktide;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Evento;

import java.util.List;

public class MeusEventosParticipante extends AppCompatActivity {

    private DAO dao;
    private Button btnCriador;
    private LinearLayout eventosContainerSemana;
    private LinearLayout eventosContainerMes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meus_eventos_participante);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnCriador = findViewById(R.id.btnCriador);

        eventosContainerSemana = findViewById(R.id.eventosContainerSemana);
        eventosContainerMes = findViewById(R.id.eventosContainerMes);

        dao = new DAO(this);
        EventosSemana();
        EventosMes();
        verificarPermissaoBtnCriador();
    }

    private void verificarPermissaoBtnCriador() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String cargo = prefs.getString("cargo", "");

        if (cargo.equals("Docente") || cargo.equals("Administrador")) {
            btnCriador.setOnClickListener(v -> {
                Intent intent = new Intent(MeusEventosParticipante.this, MeusEventosCriador.class);
                startActivity(intent);
            });
        } else {
            btnCriador.setEnabled(false);
            btnCriador.setAlpha(0.5f);
            Toast.makeText(this, "Apenas Administradores ou Docentes podem acessar o botão criador.", Toast.LENGTH_SHORT).show();
        }
    }

    private void EventosSemana() {
        List<Evento> eventos = dao.getAllEventos();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Evento evento : eventos) {
            View eventoView = inflater.inflate(R.layout.mostrar_evento_semana, eventosContainerSemana, false);

            Button btnInformacoes = eventoView.findViewById(R.id.btnInformacoes);
            btnInformacoes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showConfirmDialog();
                }
            });

            eventosContainerSemana.addView(eventoView);
        }
    }

    private void EventosMes() {
        List<Evento> eventos = dao.getAllEventos();

        LayoutInflater inflater = LayoutInflater.from(this);

        for (Evento evento : eventos) {
            View eventoView = inflater.inflate(R.layout.mostrar_evento_mes, eventosContainerMes, false);

            eventosContainerMes.addView(eventoView);
        }
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar")
                .setMessage("Você deseja ver mais informações sobre este evento?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MeusEventosParticipante.this, VisaoGeral.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    public void IrTelaCriador(View view) {
        Intent in = new Intent(this, MeusEventosCriador.class);
        startActivity(in);
    }

    public void criarMEP(View view) {
        Intent in = new Intent(this, CriarEvento.class);
        startActivity(in);
    }

    public void inicialMEP(View view){
        Intent in = new Intent(this,TelaInicial.class);
        startActivity(in);
    }

    public void localizacaoMEP(View view){
        Intent in = new Intent(this,Localizacao.class);
        startActivity(in);
    }

    public void meusEventosMEP(View view){
        Intent in = new Intent(this, MeusEventosParticipante.class);
        startActivity(in);
    }

    public void perfilMEP(View view){
        Intent in = new Intent(this, MinhaConta.class);
        startActivity(in);
    }

}
