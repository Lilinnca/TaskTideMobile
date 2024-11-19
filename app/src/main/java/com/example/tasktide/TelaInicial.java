package com.example.tasktide;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationCompat;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Evento;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
                    filtrarEventos(newText);  // Chama a função de filtro com o texto atual
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
            Log.d("TelaInicial", "Adicionando evento: " + evento.getNomeEvento());


            ImageButton imageButton = new ImageButton(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(170), dpToPx(110));
            params.setMargins(dpToPx(7), dpToPx(10), dpToPx(7), dpToPx(10));
            imageButton.setLayoutParams(params);
            imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);


            if (evento.getBanner() != null) {
                imageButton.setImageBitmap(evento.getBanner());
            } else {
                Picasso.get()
                        .load(R.drawable.banner_evento)
                        .resize(dpToPx(170), dpToPx(110))
                        .centerCrop()
                        .into(imageButton);
            }


            imageButton.setOnClickListener(v -> {
                Evento eventoCompleto = dao.buscarEventoPorId(evento.getId());
                if (eventoCompleto != null) {
                    // Verifique se o evento está pago
                    boolean isPago = dao.isEventoPago(eventoCompleto.getId());
                    if (isPago) {
                        // Navegar para a tela de pagamento
                        Intent intent = new Intent(this, InfoEvento.class); // Tela de pagamento
                        intent.putExtra("evento_id", eventoCompleto.getId());
                        startActivity(intent);
                    } else {
                        // Navegar para a tela de informações do evento
                        Intent intent = new Intent(this, EventoInfoTelaInicial.class);
                        intent.putExtra("evento_id", eventoCompleto.getId());
                        intent.putExtra("evento_nome", eventoCompleto.getNomeEvento());
                        intent.putExtra("evento_local", eventoCompleto.getLocalEvento());
                        intent.putExtra("evento_data", eventoCompleto.getDataEvento());
                        intent.putExtra("evento_descricao", eventoCompleto.getDescricao());
                        startActivity(intent);
                    }
                }
            });




            eventosContainer.addView(imageButton);

        } catch (Exception e) {
            Log.e("TelaInicial", "Erro ao adicionar evento", e);
        }
    }

    private void filtrarEventos(String query) {
        String queryLowerCase = query.toLowerCase();  // Converte o texto da pesquisa para minúsculo
        eventosContainer.removeAllViews();  // Limpa os eventos exibidos para mostrar apenas os eventos filtrados

        try {
            List<Evento> eventos = dao.getAllEventos();  // Obtém todos os eventos
            boolean algumEventoEncontrado = false;  // Flag para verificar se algum evento foi encontrado

            for (Evento evento : eventos) {
                // Verifica se o nome do evento contém o texto da pesquisa (ignora maiúsculas/minúsculas)
                if (evento.getNomeEvento().toLowerCase().contains(queryLowerCase)) {
                    Log.d("TelaInicial", "Evento encontrado: " + evento.getNomeEvento());
                    adicionarNovoEvento(evento);  // Adiciona o evento que corresponde à pesquisa
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

    public void filtrarPorCategoria(View view) {
        String categoria = (String) view.getTag(); // Obtém a tag associada ao botão
        if (categoria != null) {
            Log.d("TelaInicial", "Filtrando por categoria: " + categoria);
            filtrarPorCategoria(categoria);  // Chama o método de lógica com a categoria
        } else {
            Log.e("TelaInicial", "Tag de categoria está nula!");
            Toast.makeText(this, "Categoria inválida!", Toast.LENGTH_SHORT).show();
        }
    }

    private void filtrarPorCategoria(String categoria) {
        eventosContainer.removeAllViews();  // Limpa a exibição para mostrar apenas os eventos filtrados

        try {
            List<Evento> eventos = dao.getAllEventos();
            boolean algumEventoEncontrado = false;

            for (Evento evento : eventos) {
                if (evento.getCategoria().equals(categoria)) {
                    Log.d("TelaInicial", "Evento encontrado: " + evento.getNomeEvento());
                    adicionarNovoEvento(evento);  // Adiciona o evento que corresponde à categoria
                    algumEventoEncontrado = true;
                }
            }

            if (!algumEventoEncontrado) {
                Log.d("TelaInicial", "Nenhum evento encontrado para a categoria: " + categoria);
                Toast.makeText(this, "Nenhum evento encontrado para esta categoria.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("TelaInicial", "Erro ao filtrar eventos por categoria", e);
            Toast.makeText(this, "Erro ao filtrar eventos.", Toast.LENGTH_SHORT).show();
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

    public void telafavoritos(View view) {
        startActivity(new Intent(this, Localizacao.class));
    }
}
