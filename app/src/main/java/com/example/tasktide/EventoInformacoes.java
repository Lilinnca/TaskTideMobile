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
import com.example.tasktide.Objetos.Informacoes;

public class EventoInformacoes extends AppCompatActivity {

    private EditText edtxtDataPrevista, edtxtDataFim, edtxHorarioInicio, edtxHorarioFim, edtxtPrazo, edtxtLocal, edtxtValorEvento;
    private RadioGroup radioGroup;
    private RadioButton rdbSim, rdbNao;
    private long idEvento;
    private Button btnVoltarCriarEvento, btnTelaIrSobreParticipantes;
    private static final String TAG = "OutrasInformacoes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_informacoes);

        edtxtDataPrevista = findViewById(R.id.editTextDataPrevista);
        edtxtDataFim = findViewById(R.id.editTextDataFim);
        edtxHorarioInicio = findViewById(R.id.editTextHorarioInicio);
        edtxHorarioFim = findViewById(R.id.editTextHorarioFim);
        edtxtPrazo = findViewById(R.id.editTextPrazoInformacoes);
        edtxtLocal = findViewById(R.id.editTextLocalInformacoes);
        edtxtValorEvento = findViewById(R.id.editTextValorInformacoes);

        radioGroup = findViewById(R.id.radioGroup);
        rdbSim = findViewById(R.id.rbtnSimInformacoes);
        rdbNao = findViewById(R.id.rbtnNaoInformacoes);

        edtxtDataPrevista.addTextChangedListener(new IncluirMascara(edtxtDataPrevista));
        edtxtDataFim.addTextChangedListener(new IncluirMascara(edtxtDataFim));
        edtxtPrazo.addTextChangedListener(new IncluirMascara(edtxtPrazo));

        edtxHorarioInicio.addTextChangedListener(new HorarioTextWatcher(edtxHorarioInicio));
        edtxHorarioFim.addTextChangedListener(new HorarioTextWatcher(edtxHorarioFim));

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbtnSimInformacoes) {
                edtxtValorEvento.setEnabled(true);
                edtxtValorEvento.setHint("Insira o valor do evento");
            } else if (checkedId == R.id.rbtnNaoInformacoes) {
                edtxtValorEvento.setEnabled(false);
                edtxtValorEvento.setHint("Não é necessário adicionar valor");
            }
        });

        TextWatcher dateTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verificarDatas();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        edtxtDataPrevista.addTextChangedListener(dateTextWatcher);
        edtxtDataFim.addTextChangedListener(dateTextWatcher);

        // Recuperar o idEvento do Intent
        idEvento = getIntent().getLongExtra("ID_EVENTO", -1);
        Log.d(TAG, "ID do evento recebido: " + idEvento);

        findViewById(R.id.btnAvancarInformacoes).setOnClickListener(this::inserirInformacoesEIrParaSobreParticipantes);
    }

    private void verificarDatas() {
        String dataInicio = edtxtDataPrevista.getText().toString();
        String dataFim = edtxtDataFim.getText().toString();

        TextView txtHorarioInicio = findViewById(R.id.txtHorarioInicio);
        TextView txtHorarioFim = findViewById(R.id.txtHorarioFim);

        if (dataInicio.equals(dataFim)) {
            edtxHorarioInicio.setVisibility(View.VISIBLE);
            edtxHorarioFim.setVisibility(View.VISIBLE);
            txtHorarioInicio.setVisibility(View.VISIBLE);
            txtHorarioFim.setVisibility(View.VISIBLE);
        } else {
            edtxHorarioInicio.setVisibility(View.GONE);
            edtxHorarioFim.setVisibility(View.GONE);
            txtHorarioInicio.setVisibility(View.GONE);
            txtHorarioFim.setVisibility(View.GONE);
        }
    }

    public void inserirInformacoesEIrParaSobreParticipantes(View view) {
        // Capturar os valores dos campos
        String dataPrevista = edtxtDataPrevista.getText().toString();
        String dataFim = edtxtDataFim.getText().toString();
        String horarioInicio = edtxHorarioInicio.getText().toString();
        String horarioFim = edtxHorarioFim.getText().toString();
        String prazo = edtxtPrazo.getText().toString();
        String local = edtxtLocal.getText().toString();
        String valorEventoString = edtxtValorEvento.getText().toString();

        double valorEvento = 0.0;

        // Verificar se o evento é pago e o campo valor foi preenchido corretamente
        if (rdbSim.isChecked()) {
            // Se rdbSim está selecionado, então o valor deve ser um número válido
            try {
                valorEvento = Double.parseDouble(valorEventoString);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valor do evento deve ser um número válido.", Toast.LENGTH_SHORT).show();
                return; // Saia do método se o valor não for válido
            }
        } else {
            // Se rdbNao está selecionado, o valor do evento não deve ser obrigatório
            valorEvento = 0.0; // Ou mantenha a variável com valor padrão 0.0
        }

        String pago = rdbSim.isChecked() ? "Sim" : "Não";

        // Criar um objeto Informacoes com os dados capturados
        Informacoes informacoes = new Informacoes(dataPrevista, dataFim, horarioInicio, horarioFim, prazo, local, valorEvento, pago);

        // Inserir os dados na tabela informacoes
        DAO dao = new DAO(this);
        long id = dao.inserirInformacoes(informacoes, idEvento);

        // Se a inserção for bem-sucedida, navegar para a próxima tela
        if (id != -1) {
            IrParaTelaSobreParticipantes(view);
        }
    }


    public void IrParaTelaSobreParticipantes(View view) {
        Intent in = new Intent(EventoInformacoes.this, EventoParticipante.class);
        in.putExtra("ID_EVENTO", idEvento);
        startActivity(in);
    }

    public void voltarParaTelaCriarEvento(View view) {
        Intent in = new Intent(EventoInformacoes.this, CriarEvento.class);
        in.putExtra("ID_EVENTO", idEvento);
        startActivity(in);
    }


}