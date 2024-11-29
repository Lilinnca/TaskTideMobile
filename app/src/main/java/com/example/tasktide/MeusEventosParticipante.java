package com.example.tasktide;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Evento;
import com.example.tasktide.Objetos.Usuario;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MeusEventosParticipante extends AppCompatActivity {

    private DAO dao;
    private Button btnCriador;
    private LinearLayout eventosContainerSemana;
    private LinearLayout eventosContainerMes;
    private long usuarioId;

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

        usuarioId = getUsuarioId();

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
        if (usuarioId == -1) {
            Toast.makeText(this, "Usuário não logado!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Evento> eventosInscritos = dao.getEventosInscritos(usuarioId);
        LayoutInflater inflater = LayoutInflater.from(this);

        eventosContainerSemana.removeAllViews();

        for (Evento evento : eventosInscritos) {
            View eventoView = inflater.inflate(R.layout.mostrar_evento_semana, eventosContainerSemana, false);

            ImageView imgBannerEvento = eventoView.findViewById(R.id.imgBannerEvento);


            byte[] bannerImagemBytes = dao.obterBannerEvento(evento.getId());
            if (bannerImagemBytes != null && bannerImagemBytes.length > 0) {
                Bitmap bannerBitmap = BitmapFactory.decodeByteArray(bannerImagemBytes, 0, bannerImagemBytes.length);
                imgBannerEvento.setImageBitmap(bannerBitmap);
                imgBannerEvento.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
                imgBannerEvento.setImageResource(R.drawable.bannerpadrao);
                imgBannerEvento.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            Button btnInformacoes = eventoView.findViewById(R.id.btnInformacoes);
            btnInformacoes.setOnClickListener(v -> {

                SharedPreferences prefs = getSharedPreferences("EventPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong("EVENTO_ID", evento.getId());
                editor.apply();

                Intent intent = new Intent(this, VisaoGeral.class);
                intent.putExtra("evento_id", evento.getId());
                intent.putExtra("VEM_DA_TELA_INICIAL", true);  // Pass the flag to indicate this came from the main screen
                startActivity(intent);
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


    public void IrTelaCriador(View view) {
        Intent in = new Intent(MeusEventosParticipante.this, MeusEventosCriador.class);
        startActivity(in);
    }

    public void inicialMEP(View view){
        Intent in = new Intent(this, TelaInicial.class);
        startActivity(in);
    }

    public void localizacaoMEP(View view){
        Intent in = new Intent(this, Localizacao.class);
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
