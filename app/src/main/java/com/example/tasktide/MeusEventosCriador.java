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
        eventosContainer = findViewById(R.id.eventosContainer); // Container onde os eventos serão adicionados


        dao = new DAO(this);
        loadEventos();


        //limparTabelas dataCleaner = new limparTabelas(this);
        //dataCleaner.clearAllData();


        ViewGroup container = findViewById(R.id.eventosContainer);


        imgBannerEvento = container.findViewById(R.id.imgBannerEvento);


        verificarPermissaoBtnParticipante();
    }


    private void verificarPermissaoBtnParticipante() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String cargo = prefs.getString("cargo", "");


        if (cargo.equals("Docente") || cargo.equals("Administrador")) {
            // Permitir acesso ao botão Participante
            btnParticipante.setOnClickListener(v -> {
                Intent intent = new Intent(MeusEventosCriador.this, MeusEventosParticipante.class);
                startActivity(intent);
            });
        } else {
            // Desativar o botão Participante para cargos sem permissão
            btnParticipante.setEnabled(false);
            btnParticipante.setAlpha(0.5f); // Visualmente indicar que está desativado
            Toast.makeText(this, "Apenas Administradores ou Docentes podem acessar esta funcionalidade.", Toast.LENGTH_SHORT).show();
        }
    }




    private void loadEventos() {
        List<Evento> eventos = dao.getAllEventos();  // Recupera a lista de eventos do banco de dados
        LayoutInflater inflater = LayoutInflater.from(this);


        for (Evento evento : eventos) {
            // Infla a view para o evento
            View eventoView = inflater.inflate(R.layout.mostrar_evento, eventosContainer, false);


            // Encontra os componentes da view
            ImageView imgBannerEvento = eventoView.findViewById(R.id.imgBannerEvento);
            ImageButton imgBtnDeletar = eventoView.findViewById(R.id.imgBtnDeletar);
            ImageButton imgbuttonVisaoGeral = eventoView.findViewById(R.id.imgbuttonVisaoGeral);
            TextView nomeEvento = eventoView.findViewById(R.id.nomeEvento);


            // Preenche os dados do evento
            nomeEvento.setText(evento.getNomeEvento());


            // Verifique se o evento possui imagem
            byte[] bannerImagemBytes = dao.obterBannerEvento(evento.getId()); // Recupera a imagem do banco como byte[]


            if (bannerImagemBytes != null && bannerImagemBytes.length > 0) {
                // Converte o array de bytes em Bitmap
                Bitmap bannerBitmap = BitmapFactory.decodeByteArray(bannerImagemBytes, 0, bannerImagemBytes.length);
                imgBannerEvento.setImageBitmap(bannerBitmap);  // Define a imagem no ImageView


                // Ajusta o tamanho da imagem para caber no ImageView sem deformação
                imgBannerEvento.setScaleType(ImageView.ScaleType.FIT_XY);  // Ajusta a imagem para preencher o ImageView


                // Se o banner existir, o nome do evento vai ser ocultado
                nomeEvento.setVisibility(View.GONE);  // Esconde o nome do evento
            } else {
                imgBannerEvento.setImageResource(R.drawable.bannerpadrao);  // Imagem padrão caso não haja imagem


                // Ajusta o tamanho da imagem padrão para caber no ImageView
                imgBannerEvento.setScaleType(ImageView.ScaleType.FIT_XY);


                nomeEvento.setVisibility(View.VISIBLE);  // Exibe o nome do evento
            }


            // Adiciona a view do evento ao container
            eventosContainer.addView(eventoView);


            imgBtnDeletar.setOnClickListener(v -> {
                // Lógica para deletar o evento
                showDeleteConfirmationDialog(evento.getId(), eventoView);
            });


            imgbuttonVisaoGeral.setOnClickListener(v -> {
                Intent intent = new Intent(MeusEventosCriador.this, VisaoGeral.class);
                intent.putExtra("evento_id", evento.getId()); // Passa o ID do evento
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


    public void IrCriarEvento(View view) {
        Intent in = new Intent(MeusEventosCriador.this, CriarEvento.class);
        startActivity(in);
    }


    public void IrTelaParticipante(View view) {
        Intent in = new Intent(MeusEventosCriador.this, MeusEventosParticipante.class);
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

    public void perfilMEC(View view) {
        Intent in = new Intent(this, MinhaConta.class);
        startActivity(in);
    }







}