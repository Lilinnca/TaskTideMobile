package com.example.tasktide;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Evento;
//import com.squareup.picasso.Picasso;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;


import java.util.List;

public class TelaInicial extends AppCompatActivity {

    private static final String CHANNEL_ID = "tasktide_channel_id";
    private static final String CHANNEL_NAME = "TaskTide Notifications";
    private LinearLayout eventosContainer;
    private DAO dao;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        ImageButton btnMeusEventosInicial = findViewById(R.id.btnMeusEventosInicial);

        ImageButton btnNovoEventoInicial = findViewById(R.id.btnNovoEventoInicial);
        btnNovoEventoInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarPermissaoCriarEvento();
            }
        });

        // Configure o evento de clique
        btnMeusEventosInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMeusEventos(v);
            }
        });

        try {
            eventosContainer = findViewById(R.id.eventosContainer);
            dao = new DAO(this);
            //bottomNavigationView = findViewById(R.id.bottom_navigation);

            createNotificationChannel();

            sendNotification("Bem-vindo ao TaskTide", "Explore novos eventos e atividades!");

            carregarEventos();

            SearchView searchView = findViewById(R.id.searchViewInicial);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filtrarEventos(newText);
                    return false;
                }
            });

            Intent intent = getIntent();
            String eventoNome = intent.getStringExtra("EVENTO_NOME");

            if (eventoNome != null) {
                Evento novoEvento = new Evento();
                novoEvento.setNomeEvento(eventoNome);
                adicionarNovoEvento(novoEvento);
            }

        } catch (Exception e) {
            Log.e("TelaInicial", "Erro ao inicializar TelaInicial", e);
        }
    }

    // Método para verificar o cargo do usuário e direcionar ou exibir mensagem
    private void verificarPermissaoCriarEvento() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String cargo = prefs.getString("cargo", "");

        if (cargo.equals("Administrador") || cargo.equals("Docente")) {
            // Usuário tem permissão, redireciona para CriarEvento
            Intent intent = new Intent(TelaInicial.this, CriarEvento.class);
            startActivity(intent);
        } else {
            // Usuário não tem permissão, exibe Toast
            Toast.makeText(this, "Apenas Administradores e Docentes podem criar eventos.", Toast.LENGTH_SHORT).show();
        }
    }

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

    public void abrirMeusEventos(View view) {
        // obtem as preferências compartilhadas
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String cargo = prefs.getString("cargo", "");


        // Verifica o cargo e redirecione para a tela correspondente
        Intent intent;
        if (cargo.equals("Discente - Ensino Médio") || cargo.equals("Discentes - Ensino Superior")) {
            intent = new Intent(TelaInicial.this, MeusEventosParticipante.class);
        } else if (cargo.equals("Docente") || cargo.equals("Administrador")) {
            intent = new Intent(TelaInicial.this, MeusEventosCriador.class);
        } else {
            // Caso não seja reconhecido, mostra a mensagem de erro
            Toast.makeText(this, "Cargo inválido ou não reconhecido!", Toast.LENGTH_SHORT).show();
            return;
        }


        startActivity(intent);
    }


     private void sendNotification(String title, String message) {
     NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
     .setSmallIcon(R.mipmap.icone_tasktide_foreground)
     .setContentTitle(title)
     .setContentText(message)
     .setPriority(NotificationCompat.PRIORITY_DEFAULT)
     .setAutoCancel(true);


     NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
     if (notificationManager != null) {
     notificationManager.notify(1, builder.build());
     }
     }

    private void carregarEventos() {
        try {
            List<Evento> eventos = dao.getAllEventos();
            Log.d("TelaInicial", "Eventos carregados: " + eventos.size());
            for (Evento evento : eventos) {
                adicionarNovoEvento(evento);
            }
        } catch (Exception e) {
            Log.e("TelaInicial", "Erro ao carregar eventos", e);
        }
    }

    private void adicionarNovoEvento(Evento evento) {
        try {
            // Inflate o layout 'mostrar_evento_tela_inicial'
            View eventoView = getLayoutInflater().inflate(R.layout.mostrar_evento_tela_inicial, eventosContainer, false);

            // Referencie os componentes do layout
            ImageView imgBannerEvento = eventoView.findViewById(R.id.imgBannerEvento);
            TextView nomeEvento = eventoView.findViewById(R.id.nomeEvento);
            Button btnInformacoesTelaInicial = eventoView.findViewById(R.id.btnInformacoesTelaInicial);

            // Configure os valores
            nomeEvento.setText(evento.getNomeEvento());

            // Obtém a imagem do banner do banco de dados
            byte[] bannerBytes = dao.obterBannerEvento(evento.getId());
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

            // Configurar evento de clique no botão "Informações"
            btnInformacoesTelaInicial.setOnClickListener(v -> {
                // Salvar o ID do evento no SharedPreferences
                SharedPreferences prefs = getSharedPreferences("EventPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong("EVENTO_ID", evento.getId());
                editor.apply();

                // Passar a informação de que a VisaoGeral foi acessada a partir da Tela Inicial
                Intent intent = new Intent(this, VisaoGeral.class);
                intent.putExtra("evento_id", evento.getId());
                intent.putExtra("VEM_DA_TELA_INICIAL", true);  // Passa a flag
                startActivity(intent);
            });


            // Adicionar margens entre os eventos
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 0, 24); // margem inferior de 24dp
            eventoView.setLayoutParams(layoutParams);

            // Adicione o layout inflado ao container
            eventosContainer.addView(eventoView);

        } catch (Exception e) {
            Log.e("TelaInicial", "Erro ao adicionar evento", e);
        }
    }

    private void filtrarEventos(String query) {
        String queryLowerCase = query.toLowerCase();
        eventosContainer.removeAllViews();


        try {
            List<Evento> eventos = dao.getAllEventos();
            boolean algumEventoEncontrado = false;
            for (Evento evento : eventos) {
                if (evento.getNomeEvento().toLowerCase().contains(queryLowerCase)) {
                    Log.d("TelaInicial", "Evento encontrado: " + evento.getNomeEvento());
                    adicionarNovoEvento(evento);
                    algumEventoEncontrado = true;
                }
            }
            if (!algumEventoEncontrado) {
                Log.d("TelaInicial", "Nenhum evento encontrado para a pesquisa");
            }
        } catch (Exception e) {
            Log.e("TelaInicial", "Erro ao filtrar eventos", e);
        }
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    public void telaperfil(View view) {
     startActivity(new Intent(this, MinhaConta.class));
     }


     public void telameuseventos(View view) {
     startActivity(new Intent(this, MeusEventosParticipante.class));
     }


     public void telacriarevento(View view) {
     startActivity(new Intent(this, CriarEvento.class));
     }


     public void telalocalizacao(View view) {
     startActivity(new Intent(this, Localizacao.class));
     }

}