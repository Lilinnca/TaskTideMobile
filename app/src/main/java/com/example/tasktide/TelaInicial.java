package com.example.tasktide;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Evento;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TelaInicial extends AppCompatActivity {

    private LinearLayout eventosContainer;  // Container para os banners dos eventos.
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        try {
            // Inicialização das variáveis
            eventosContainer = findViewById(R.id.eventosContainer);
            dao = new DAO(this);

            // Carregar eventos inicialmente
            carregarEventos();

            // Configuração da barra de pesquisa
            SearchView searchView = findViewById(R.id.searchViewInicial);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // Filtra os eventos conforme o texto digitado
                    filtrarEventos(newText);
                    return false;
                }
            });

        } catch (Exception e) {
            Log.e("TelaInicial", "Erro ao inicializar TelaInicial", e);
        }
    }

    // Carregar todos os eventos na tela
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

    // Adicionar novo evento ao container
    private void adicionarNovoEvento(Evento evento) {
        try {
            Log.d("TelaInicial", "Adicionando evento: " + evento.getNomeEvento());

            // Criação do ImageButton para exibir o banner
            ImageButton imageButton = new ImageButton(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(170), dpToPx(110));
            params.setMargins(dpToPx(7), dpToPx(10), dpToPx(7), dpToPx(10));
            imageButton.setLayoutParams(params);
            imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Carregar o banner do evento ou uma imagem padrão
            if (evento.getBanner() != null) {
                imageButton.setImageBitmap(evento.getBanner());
            } else {
                Picasso.get()
                        .load(R.drawable.banner_evento)
                        .resize(dpToPx(170), dpToPx(110))
                        .centerCrop()
                        .into(imageButton);
            }

            // Ao clicar no banner, exibir informações detalhadas do evento
            imageButton.setOnClickListener(v -> {
                Evento eventoCompleto = dao.buscarEventoPorId(evento.getId());
                if (eventoCompleto != null) {
                    Intent intent = new Intent(this, EventoInfoTelaInicial.class);
                    intent.putExtra("evento_id", eventoCompleto.getId());
                    intent.putExtra("evento_nome", eventoCompleto.getNomeEvento());
                    intent.putExtra("evento_local", eventoCompleto.getLocalEvento());
                    intent.putExtra("evento_data", eventoCompleto.getDataEvento());
                    intent.putExtra("evento_descricao", eventoCompleto.getDescricao());
                    startActivity(intent);
                }
            });

            // Adicionar o ImageButton ao container de eventos
            eventosContainer.addView(imageButton);

        } catch (Exception e) {
            Log.e("TelaInicial", "Erro ao adicionar evento", e);
        }
    }

    // Filtrar eventos pelo nome
    private void filtrarEventos(String query) {
        String queryLowerCase = query.toLowerCase();
        eventosContainer.removeAllViews();  // Limpar a tela antes de adicionar eventos filtrados

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

    public void abrirFiltros(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filtrar Eventos");

        String[] tiposDeEvento = getResources().getStringArray(R.array.tipos_evento);  // Pegando as categorias do arquivo string.xml
        builder.setItems(tiposDeEvento, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tipoSelecionado = tiposDeEvento[which];
                filtrarEventosPorTipo(tipoSelecionado);
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    // Filtrar eventos por tipo
    private void filtrarEventosPorTipo(String tipo) {
        eventosContainer.removeAllViews();  // Limpar a tela antes de adicionar eventos filtrados

        try {
            List<Evento> eventos = dao.getAllEventos();
            boolean algumEventoEncontrado = false;
            for (Evento evento : eventos) {
                if ("Todos".equals(tipo) || evento.getTipoEvento().equals(tipo)) {
                    Log.d("TelaInicial", "Evento encontrado: " + evento.getNomeEvento());
                    adicionarNovoEvento(evento);
                    algumEventoEncontrado = true;
                }
            }
            if (!algumEventoEncontrado) {
                Log.d("TelaInicial", "Nenhum evento encontrado para o tipo: " + tipo);
            }
        } catch (Exception e) {
            Log.e("TelaInicial", "Erro ao filtrar eventos por tipo", e);
        }
    }

    // Converter DP para PX
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    // Métodos de navegação
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
