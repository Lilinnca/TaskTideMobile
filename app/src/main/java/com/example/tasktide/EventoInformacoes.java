package com.example.tasktide;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Evento;
import com.example.tasktide.Objetos.Informacoes;

public class EventoInformacoes extends AppCompatActivity {

    // Declaração de variáveis
    private EditText editTextDataPrevista, editTextDataFim, editTextHorarioInicio, editTextHorarioFim, editTextPrazo, editTextLocal, editTextValorEvento;
    private RadioGroup radioGroup;
    private RadioButton rbtnSim, rbtnNao;
    private long idEvento;
    private static final String TAG = "EventoInformacoes";
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_informacoes);

        // Inicialização dos componentes da interface
        editTextDataPrevista = findViewById(R.id.editTextDataPrevista);
        editTextDataFim = findViewById(R.id.editTextDataFim);
        editTextHorarioInicio = findViewById(R.id.editTextHorarioInicio);
        editTextHorarioFim = findViewById(R.id.editTextHorarioFim);
        editTextPrazo = findViewById(R.id.editTextPrazoInformacoes);
        editTextLocal = findViewById(R.id.editTextLocalInformacoes);
        editTextValorEvento = findViewById(R.id.editTextValorInformacoes);

        radioGroup = findViewById(R.id.radioGroup);
        rbtnSim = findViewById(R.id.rbtnSimInformacoes);
        rbtnNao = findViewById(R.id.rbtnNaoInformacoes);

        // Adiciona máscaras e validadores aos campos
        editTextDataPrevista.addTextChangedListener(new IncluirMascara(editTextDataPrevista));
        editTextDataFim.addTextChangedListener(new IncluirMascara(editTextDataFim));
        editTextPrazo.addTextChangedListener(new IncluirMascara(editTextPrazo));
        editTextHorarioInicio.addTextChangedListener(new HorarioTextWatcher(editTextHorarioInicio));
        editTextHorarioFim.addTextChangedListener(new HorarioTextWatcher(editTextHorarioFim));

        // Listener para RadioGroup para habilitar/desabilitar o campo de valor do evento
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbtnSimInformacoes) {
                editTextValorEvento.setEnabled(true);
                editTextValorEvento.setHint("Insira o valor do evento");
            } else if (checkedId == R.id.rbtnNaoInformacoes) {
                editTextValorEvento.setEnabled(false);
                editTextValorEvento.setHint("Não é necessário adicionar valor");
            }
        });

        // Adiciona validador para sincronizar a visibilidade dos campos de horário com base nas datas
        TextWatcher dateTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verificarDatas();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        editTextDataPrevista.addTextChangedListener(dateTextWatcher);
        editTextDataFim.addTextChangedListener(dateTextWatcher);

        // Obtém o ID do evento recebido da Intent
        idEvento = getIntent().getLongExtra("ID_EVENTO", -1);
        Log.d(TAG, "ID do evento recebido: " + idEvento);

        dao = new DAO(this); // Inicializa a DAO
        carregarInformacoesEvento(); // Carrega informações do evento se disponíveis

        // Configura os botões de avançar e voltar
        findViewById(R.id.btnAvancarInformacoes).setOnClickListener(this::avancarInformacoes);
        findViewById(R.id.btnVoltarInformacoes).setOnClickListener(this::voltarInformacoes);
    }

    // Método para verificar se as datas coincidem e ajustar a visibilidade dos campos de horário
    private void verificarDatas() {
        String dataPrevista = editTextDataPrevista.getText().toString();
        String dataFim = editTextDataFim.getText().toString();

        TextView txtHorarioInicio = findViewById(R.id.txtHorarioInicio);
        TextView txtHorarioFim = findViewById(R.id.txtHorarioFim);

        if (dataPrevista.equals(dataFim)) {
            editTextHorarioInicio.setVisibility(View.VISIBLE);
            editTextHorarioFim.setVisibility(View.VISIBLE);
            txtHorarioInicio.setVisibility(View.VISIBLE);
            txtHorarioFim.setVisibility(View.VISIBLE);
        } else {
            editTextHorarioInicio.setVisibility(View.GONE);
            editTextHorarioFim.setVisibility(View.GONE);
            txtHorarioInicio.setVisibility(View.GONE);
            txtHorarioFim.setVisibility(View.GONE);
        }
    }

    // Método para avançar as informações
    public void avancarInformacoes(View view) {
        // Coleta e valida os dados
        String dataPrevista = editTextDataPrevista.getText().toString().trim();
        String dataFim = editTextDataFim.getText().toString().trim();
        String prazo = editTextPrazo.getText().toString().trim();
        String local = editTextLocal.getText().toString().trim();
        String valorEventoString = editTextValorEvento.getText().toString().trim();

        String horarioInicio = editTextHorarioInicio.getVisibility() == View.VISIBLE
                ? editTextHorarioInicio.getText().toString().trim()
                : "";
        String horarioFim = editTextHorarioFim.getVisibility() == View.VISIBLE
                ? editTextHorarioFim.getText().toString().trim()
                : "";

        double valorEvento = 0.0;

        // Valida se o valor do evento é válido caso "Sim" esteja selecionado
        if (rbtnSim.isChecked()) {
            try {
                valorEvento = Double.parseDouble(valorEventoString);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valor do evento deve ser um número válido.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Erro ao tentar converter o valor do evento para número: " + valorEventoString);
                return;
            }
        }

        // Define se o evento é pago
        String pago = rbtnSim.isChecked() ? "Sim" : "Não";

        // Logando os dados antes de salvar
        Log.d(TAG, "Dados coletados para salvar: ");
        Log.d(TAG, "Data Prevista: " + dataPrevista);
        Log.d(TAG, "Data Fim: " + dataFim);
        Log.d(TAG, "Horário Início: " + horarioInicio);
        Log.d(TAG, "Horário Fim: " + horarioFim);
        Log.d(TAG, "Prazo: " + prazo);
        Log.d(TAG, "Local: " + local);
        Log.d(TAG, "Valor Evento: " + valorEvento);
        Log.d(TAG, "Pago: " + pago);

        // Cria o objeto Informacoes e insere no banco de dados
        Informacoes informacoes = new Informacoes();
        informacoes.setDataPrevista(dataPrevista);
        informacoes.setDataFim(dataFim);
        informacoes.setHorarioInicio(horarioInicio);
        informacoes.setHorarioTermino(horarioFim);
        informacoes.setPrazo(prazo);
        informacoes.setLocal(local);
        informacoes.setValorEvento(valorEvento);
        informacoes.setPago(pago);

        // Log antes de inserir no banco
        Log.d(TAG, "Inserindo informações no banco...");
        dao.inserirInformacoes(informacoes, idEvento);

        Log.d(TAG, "Informações inseridas com sucesso no banco.");

        // Inicia a nova atividade
        Intent intent = new Intent(this, EventoParticipante.class);
        intent.putExtra("ID_EVENTO", idEvento);
        startActivity(intent);

        finish();
    }


    // Método para carregar as informações do evento no formulário
    private void carregarInformacoesEvento() {
        Evento evento = dao.buscarEventoPorId(idEvento);
        if (evento != null) {
            editTextDataPrevista.setText(evento.getDataEvento());
            editTextLocal.setText(evento.getLocalEvento());
        }
    }

    // Método para voltar para a tela anterior
    public void voltarInformacoes(View view) {
        Intent intent = new Intent(this, CriarEvento.class);
        intent.putExtra("ID_EVENTO", idEvento);
        startActivity(intent);
        finish();
    }
}
