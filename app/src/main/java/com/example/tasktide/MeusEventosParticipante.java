package com.example.tasktide;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Evento;
import com.example.tasktide.Objetos.Usuario;


import java.util.List;


public class MeusEventosParticipante extends AppCompatActivity {


    private DAO dao;
    private Button btnCriador;
    private LinearLayout eventosContainerSemana;
    private LinearLayout  eventosContainerMes;

    private long usuarioId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_eventos_participante);

        // Inicializar elementos da interface
        btnCriador = findViewById(R.id.btnCriador);
        eventosContainerSemana = findViewById(R.id.eventosContainerSemana);
        eventosContainerMes = findViewById(R.id.eventosContainerMes);

        // Inicializar o DAO
        dao = new DAO(this);

        // Obter o ID do usuário logado
        usuarioId = getUsuarioId();

        // Carregar eventos aos quais o usuário está inscrito
        EventosSemana();
        EventosMes();
        verificarPermissaoBtnCriador();
    }

    private void verificarPermissaoBtnCriador() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String cargo = prefs.getString("cargo", "");

        if (cargo.equals("Docente") || cargo.equals("Administrador")) {
            // Permitir acesso
            btnCriador.setOnClickListener(v -> {
                Intent intent = new Intent(MeusEventosParticipante.this, MeusEventosCriador.class);
                startActivity(intent);
            });
        } else {
            // Desativar o botão para cargos sem permissão
            btnCriador.setEnabled(false);
            btnCriador.setAlpha(0.5f); // Visualmente indicar que está desativado
            Toast.makeText(this, "Apenas Administradores ou Docentes podem acessar o botão criador.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para carregar eventos da semana, mostrando apenas eventos inscritos
    private void EventosSemana() {
        if (usuarioId == -1) {
            // Caso o usuário não esteja logado, exibe uma mensagem
            Toast.makeText(this, "Usuário não logado!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Evento> eventosInscritos = dao.getEventosInscritos(usuarioId);
        LayoutInflater inflater = LayoutInflater.from(this);

        // Limpar o container de eventos antes de adicionar novos
        eventosContainerSemana.removeAllViews();

        for (Evento evento : eventosInscritos) {
            // Infla a visualização para cada evento
            View eventoView = inflater.inflate(R.layout.mostrar_evento_semana, eventosContainerSemana, false);

            // Configura o nome ou outros detalhes na visualização (opcional)
            Button btnInformacoes = eventoView.findViewById(R.id.btnInformacoes);

            btnInformacoes.setOnClickListener(v -> {
                // Buscar detalhes completos do evento pelo ID
                Evento eventoCompleto = dao.buscarEventoPorId(evento.getId());
                if (eventoCompleto != null) {
                    boolean isPago = dao.isEventoPago(eventoCompleto.getId());
                    if (isPago) {
                        // Redireciona para a tela de pagamento se o evento for pago
                        Intent intent = new Intent(MeusEventosParticipante.this, InfoEvento.class);
                        intent.putExtra("evento_id", eventoCompleto.getId());
                        startActivity(intent);
                    } else {
                        // Redireciona para a tela de informações se o evento não for pago
                        Intent intent = new Intent(MeusEventosParticipante.this, EventoInfoTelaInicial.class);
                        intent.putExtra("evento_id", eventoCompleto.getId());
                        intent.putExtra("evento_nome", eventoCompleto.getNomeEvento());
                        intent.putExtra("evento_local", eventoCompleto.getLocalEvento());
                        intent.putExtra("evento_data", eventoCompleto.getDataEvento());
                        intent.putExtra("evento_descricao", eventoCompleto.getDescricao());
                        startActivity(intent);
                    }
                } else {
                    // Exibe uma mensagem caso o evento não seja encontrado
                    Toast.makeText(MeusEventosParticipante.this, "Detalhes do evento não encontrados.", Toast.LENGTH_SHORT).show();
                }
            });

            // Adiciona a visualização ao container
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
                        Intent intent = new Intent(MeusEventosParticipante.this, EventoInfoTelaInicial.class);
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

    // Método para obter o ID do usuário
    private long getUsuarioId() {
        String usuarioEmail = getEmailUsuario();

        if (usuarioEmail == null) {
            return -1;  // Se o e-mail não estiver salvo, o usuário não está logado
        }

        Usuario usuario = dao.buscarUsuarioPorEmail(usuarioEmail);

        if (usuario != null) {
            return usuario.getId();  // Retorna o ID do usuário
        } else {
            return -1;  // Se não encontrar o usuário, retorna -1
        }
    }

    // Método para obter o e-mail do usuário armazenado nas preferências compartilhadas
    private String getEmailUsuario() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getString("email", null);
    }


    public void IrTelaCriador(View view) {
        Intent in = new Intent(MeusEventosParticipante.this, MeusEventosCriador.class);
        startActivity(in);
    }


}