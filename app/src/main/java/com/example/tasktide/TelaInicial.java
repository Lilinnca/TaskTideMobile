package com.example.tasktide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Evento;

import java.util.List;

public class TelaInicial extends AppCompatActivity {

    private LinearLayout eventosContainer;
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        ImageButton btnMeusEventosInicial = findViewById(R.id.btnMeusEventosInicial);
        ImageButton btnNovoEventoInicial = findViewById(R.id.btnNovoEventoInicial);

        btnNovoEventoInicial.setOnClickListener(v -> verificarPermissaoCriarEvento());
        btnMeusEventosInicial.setOnClickListener(this::abrirMeusEventos);

        try {
            eventosContainer = findViewById(R.id.eventosContainer);
            dao = new DAO(this);

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

            // Verificar se foi passado um nome de evento via Intent
            Intent intent = getIntent();
            String eventoNome = intent.getStringExtra("EVENTO_NOME");

            if (eventoNome != null) {
                // Criar um novo objeto Evento
                Evento novoEvento = new Evento();
                novoEvento.setNomeEvento(eventoNome);

                // Adicionar o novo evento no banco de dados e obter o ID do evento
                long eventoId = dao.inserirEvento(novoEvento);  // Método para adicionar o evento no banco e retornar o ID

                if (eventoId != -1) { // Verifica se o evento foi adicionado corretamente
                    adicionarNovoEvento(eventoId); // Passando o ID do evento
                } else {
                    Log.e("TelaInicial", "Erro ao adicionar o evento.");
                }
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

    // Método para abrir a tela dos eventos
    public void abrirMeusEventos(View view) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String cargo = prefs.getString("cargo", "");

        Intent intent;
        if (cargo.equals("Discente - Ensino Médio") || cargo.equals("Discentes - Ensino Superior")) {
            intent = new Intent(TelaInicial.this, MeusEventosParticipante.class);
        } else if (cargo.equals("Docente") || cargo.equals("Administrador")) {
            intent = new Intent(TelaInicial.this, MeusEventosCriador.class);
        } else {
            Toast.makeText(this, "Cargo inválido ou não reconhecido!", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(intent);
    }

    private void carregarEventos() {
        try {
            // Pega todos os eventos do banco
            List<Evento> eventos = dao.getAllEventos();
            Log.d("TelaInicial", "Eventos carregados: " + eventos.size());

            for (Evento evento : eventos) {
                // Aqui adicionamos cada evento ao eventosContainer
                adicionarNovoEvento(evento.getId());
            }
        } catch (Exception e) {
            Log.e("TelaInicial", "Erro ao carregar eventos", e);
        }
    }

    private void adicionarNovoEvento(long eventoId) {
        try {
            // Buscar o evento pelo ID usando o DAO
            Evento evento = dao.buscarEventoPorId(eventoId);

            if (evento == null) {
                Log.e("TelaInicial", "Evento não encontrado.");
                return;
            }

            // Inflate o layout do evento (cria uma nova instância para cada container)
            View eventoView = getLayoutInflater().inflate(R.layout.mostrar_evento_tela_inicial, null, false);

            // Referenciar os componentes do layout
            ImageView imgBannerEvento = eventoView.findViewById(R.id.imgBannerEvento);
            TextView nomeEvento = eventoView.findViewById(R.id.nomeEvento);
            Button btnInformacoesTelaInicial = eventoView.findViewById(R.id.btnInformacoesTelaInicial);

            // Configurar os valores
            nomeEvento.setText(evento.getNomeEvento());

            // Obtém a imagem do banner do banco de dados
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
                intent.putExtra("VEM_DA_TELA_INICIAL", true); // Passa a flag
                startActivity(intent);
            });

            // Adicionar margens entre os eventos
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 0, 24); // Margem inferior
            eventoView.setLayoutParams(layoutParams);

            // Verifica a categoria do evento e seleciona o container correto
            String categoria = evento.getCategoria();
            LinearLayout container;

            if (categoria != null) {
                if (categoria.equalsIgnoreCase("Entretenimento")) {
                    container = findViewById(R.id.entretenimentoContainer);
                } else if (categoria.equalsIgnoreCase("Educação")) {
                    container = findViewById(R.id.educacaoContainer);
                } else {
                    container = findViewById(R.id.outrosContainer);
                }
            } else {
                container = findViewById(R.id.outrosContainer); // Se a categoria for null, adiciona no container de "outros"
            }

            // Adicionar ao container da categoria
            container.addView(eventoView);

        } catch (Exception e) {
            Log.e("TelaInicial", "Erro ao adicionar evento", e);
        }
    }


    private void copiarDadosDoEvento(Evento evento, View eventoViewCopy) {
        // Método auxiliar para copiar os dados do evento para o novo eventoView
        TextView nomeEventoCopy = eventoViewCopy.findViewById(R.id.nomeEvento);
        nomeEventoCopy.setText(evento.getNomeEvento());

        ImageView imgBannerEventoCopy = eventoViewCopy.findViewById(R.id.imgBannerEvento);
        byte[] bannerImagemBytes = dao.obterBannerEvento(evento.getId());

        if (bannerImagemBytes != null && bannerImagemBytes.length > 0) {
            Bitmap bannerBitmap = BitmapFactory.decodeByteArray(bannerImagemBytes, 0, bannerImagemBytes.length);
            imgBannerEventoCopy.setImageBitmap(bannerBitmap);
            imgBannerEventoCopy.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            imgBannerEventoCopy.setImageResource(R.drawable.bannerpadrao);
            imgBannerEventoCopy.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    // Filtra os eventos com base na pesquisa do usuário
    private void filtrarEventos(String query) {
        String queryLowerCase = query.toLowerCase();
        eventosContainer.removeAllViews();

        try {
            List<Evento> eventos = dao.getAllEventos();
            boolean algumEventoEncontrado = false;
            for (Evento evento : eventos) {
                if (evento.getNomeEvento().toLowerCase().contains(queryLowerCase)) {
                    Log.d("TelaInicial", "Evento encontrado: " + evento.getNomeEvento());
                    adicionarNovoEvento(evento.getId());
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