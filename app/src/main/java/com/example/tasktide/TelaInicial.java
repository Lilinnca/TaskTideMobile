package com.example.tasktide;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Evento;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TelaInicial extends AppCompatActivity {

    private static final String CHANNEL_ID = "tasktide_channel_id";
    private static final String CHANNEL_NAME = "TaskTide Notifications";
    private LinearLayout eventosContainer;
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        try {
            eventosContainer = findViewById(R.id.eventosContainer);
            dao = new DAO(this);

            // Cria o canal de notificação
            createNotificationChannel();

            // Envia uma notificação
            sendNotification("Bem-vindo ao TaskTide", "Explore novos eventos e atividades!");

            carregarEventos();

            Intent intent = getIntent();
            String eventoNome = intent.getStringExtra("EVENTO_NOME");

            if (eventoNome != null) {
                adicionarNovoEvento(eventoNome); // Adiciona o evento passado pela intent
            }
        } catch (Exception e) {
            e.printStackTrace(); // Para verificar o que pode estar causando o problema
        }
    }

    /**
     * Método para criar o canal de notificação (necessário para Android 8.0 e superior).
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Canal de notificações para o TaskTide");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Método para enviar uma notificação com um ícone personalizado do mipmap.
     */
    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.icone_tasktide_foreground) // Ícone do diretório mipmap
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build()); // "1" é o ID da notificação
        }
    }

    /**
     * Método para adicionar um novo evento sem banner personalizado.
     * @param eventoNome Nome do evento
     */
    private void adicionarNovoEvento(String eventoNome) {
        try {
            ImageButton imageButton = new ImageButton(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dpToPx(170),
                    dpToPx(110)
            );
            params.setMargins(dpToPx(7), 0, dpToPx(7), 0);
            imageButton.setLayoutParams(params);
            imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Picasso.get()
                    .load(R.drawable.banner_evento)
                    .resize(170, 110)
                    .centerCrop()
                    .into(imageButton);

            imageButton.setOnClickListener(v -> {
                Intent intent = new Intent(TelaInicial.this, EventoInfoTelaInicial.class);
                startActivity(intent);
            });

            eventosContainer.addView(imageButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para adicionar um novo evento com detalhes completos e banner personalizado.
     * @param evento Objeto Evento contendo todos os detalhes do evento
     */
    private void adicionarNovoEvento(Evento evento) {
        try {
            ImageButton imageButton = new ImageButton(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dpToPx(170),
                    dpToPx(110)
            );
            params.setMargins(dpToPx(7), 0, dpToPx(7), 0);
            imageButton.setLayoutParams(params);
            imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

            if (evento.getBannerBitmap() != null) {
                imageButton.setImageBitmap(evento.getBannerBitmap());
            } else {
                Picasso.get()
                        .load(R.drawable.banner_evento)
                        .resize(170, 110)
                        .centerCrop()
                        .into(imageButton);
            }

            imageButton.setOnClickListener(v -> {
                Intent intent = new Intent(TelaInicial.this, EventoInfoTelaInicial.class);
                intent.putExtra("EVENTO_ID", evento.getId());
                startActivity(intent);
            });

            eventosContainer.addView(imageButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para carregar eventos da base de dados e adicionar à tela.
     */
    private void carregarEventos() {
        try {
            List<Evento> eventos = dao.getAllEventos();
            for (Evento evento : eventos) {
                adicionarNovoEvento(evento);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método utilitário para converter DP para Pixels.
     * @param dp Valor em DP (Density-independent Pixels)
     * @return Valor em Pixels
     */
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    // Métodos de navegação para outras telas
    public void telaperfil(View view) {
        Intent in = new Intent(TelaInicial.this, MinhaConta.class);
        startActivity(in);
    }

    public void telameuseventos(View view) {
        Intent in = new Intent(TelaInicial.this, MeusEventos.class);
        startActivity(in);
    }

    public void telacriarevento(View view) {
        Intent in = new Intent(TelaInicial.this, CriarEvento.class);
        startActivity(in);
    }

    public void telafavoritos(View view) {
        Intent in = new Intent(TelaInicial.this, Localizacao.class);
        startActivity(in);
    }
}
