package com.example.tasktide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class MeusEventosCriador extends AppCompatActivity {

    private DAO dao;
    private LinearLayout eventosContainer;
    private ImageButton imgBtnDeletar;
    private ImageView imgBannerEvento;
    private Button btnParticipante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meus_eventos_criador);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        btnParticipante = findViewById(R.id.btnParticipante);
        eventosContainer = findViewById(R.id.eventosContainer);


        dao = new DAO(this);
        loadEventos();


        ViewGroup container = findViewById(R.id.eventosContainer);


        imgBannerEvento = container.findViewById(R.id.imgBannerEvento);


        verificarPermissaoBtnParticipante();
    }


    private void verificarPermissaoBtnParticipante() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String cargo = prefs.getString("cargo", "");


        if (cargo.equals("Docente") || cargo.equals("Administrador")) {
            btnParticipante.setOnClickListener(v -> {
                Intent intent = new Intent(MeusEventosCriador.this, MeusEventosParticipante.class);
                startActivity(intent);
            });
        } else {
            btnParticipante.setEnabled(false);
            btnParticipante.setAlpha(0.5f);
            Toast.makeText(this, "Apenas Administradores ou Docentes podem acessar esta funcionalidade.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadEventos() {
        List<Evento> eventos = dao.getAllEventos();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Evento evento : eventos) {
            View eventoView = inflater.inflate(R.layout.mostrar_evento, eventosContainer, false);

            ImageView imgBannerEvento = eventoView.findViewById(R.id.imgBannerEvento);
            ImageButton imgBtnDeletar = eventoView.findViewById(R.id.imgBtnDeletar);
            ImageButton imgbuttonVisaoGeral = eventoView.findViewById(R.id.imgbuttonVisaoGeral);
            TextView nomeEvento = eventoView.findViewById(R.id.nomeEvento);

            nomeEvento.setText(evento.getNomeEvento());

            byte[] bannerImagemBytes = dao.obterBannerEvento(evento.getId());


            if (bannerImagemBytes != null && bannerImagemBytes.length > 0) {
                Bitmap bannerBitmap = BitmapFactory.decodeByteArray(bannerImagemBytes, 0, bannerImagemBytes.length);
                imgBannerEvento.setImageBitmap(bannerBitmap);

                imgBannerEvento.setScaleType(ImageView.ScaleType.FIT_XY);

                nomeEvento.setVisibility(View.GONE);
            } else {
                imgBannerEvento.setImageResource(R.drawable.bannerpadrao);

                imgBannerEvento.setScaleType(ImageView.ScaleType.FIT_XY);

                nomeEvento.setVisibility(View.VISIBLE);
            }

            eventosContainer.addView(eventoView);


            imgBtnDeletar.setOnClickListener(v -> {
                showDeleteConfirmationDialog(evento.getId(), eventoView);
            });


            imgbuttonVisaoGeral.setOnClickListener(v -> {
                SharedPreferences prefs = getSharedPreferences("EventPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong("EVENTO_ID", evento.getId());
                editor.apply();


                Intent intent = new Intent(MeusEventosCriador.this, VisaoGeral.class);
                startActivity(intent);
            });
        }
    }


    private void showDeleteConfirmationDialog(long eventoId, View eventoView) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Você realmente deseja excluir este evento?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    deleteEvento(eventoId, eventoView);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }


    private void deleteEvento(long eventoId, View eventoView) {
        dao.deleteEvento(eventoId);
        eventosContainer.removeView(eventoView);
        Toast.makeText(MeusEventosCriador.this, "Evento deletado com sucesso.", Toast.LENGTH_SHORT).show();
    }


    public void CriarMEC(View view) {
        Intent in = new Intent(this, CriarEvento.class);
        startActivity(in);
    }

    public void inicialMEC(View view) {
        Intent in = new Intent(this, TelaInicial.class);
        startActivity(in);
    }

    public void localizacaoMEC(View view) {
        Intent in = new Intent(this, Localizacao.class);
        startActivity(in);
    }

    public void MeusEventosMEC(View view) {
        Intent in = new Intent(this, MeusEventosParticipante.class);
        startActivity(in);
    }

    public void PerfilMEC(View view) {
        Intent in = new Intent(this, MinhaConta.class);
        startActivity(in);
    }

    public void IrTelaParticipante(View view) {
        Intent in = new Intent(this, MeusEventosParticipante.class);
        startActivity(in);
    }


}
