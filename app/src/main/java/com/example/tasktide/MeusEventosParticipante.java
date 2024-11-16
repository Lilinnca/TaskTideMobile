package com.example.tasktide;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


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
    private LinearLayout eventosContainerSemana;
    private LinearLayout  eventosContainerMes;


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


        Button btnCriador = findViewById(R.id.btnCriador);
        // btnCriador.setClickable(false);


        /** btnCriador.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        Toast.makeText(MeusEventosParticipante.this, "Somente os criadores de eventos podem acessar.", Toast.LENGTH_SHORT).show();
        }
        });**/


        eventosContainerSemana = findViewById(R.id.eventosContainerSemana);
        eventosContainerMes = findViewById(R.id.eventosContainerMes);




        dao = new DAO(this);
        EventosSemana();
        EventosMes();
    }


    private void EventosSemana() {
        List<Evento> eventos = dao.getAllEventos();


        LayoutInflater inflater = LayoutInflater.from(this);


        for (Evento evento : eventos) {
            View eventoView = inflater.inflate(R.layout.mostrar_evento_semana, eventosContainerSemana, false);


            eventosContainerSemana.addView(eventoView);
        }


        Button btnInformacoes = findViewById(R.id.btnInformacoes);
        btnInformacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });


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
                        // Navegar para a TelaVisaoGeral
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
    public void localizacaoMeusEventosP(View view){
        Intent in = new Intent(this, Localizacao.class);
        startActivity(in);
    }
    public void addEventoMeusEventosP(View view){
        Intent in = new Intent(this, CriarEvento.class);
        startActivity(in);
    }
    public void meusEventosP(View view){
        Intent in = new Intent(this, MeusEventosParticipante.class);
        startActivity(in);
    }
    public void perfilMeusEventosP(View view){
        Intent in = new Intent(this, MinhaConta.class);
        startActivity(in);
    }



}
