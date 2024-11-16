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


    private EditText editTextDataPrevista, editTextDataFim, editTextHorarioInicio, editTextHorarioFim, editTextPrazo, editTextLocal, editTextValorEvento;
    private RadioGroup radioGroup;
    private RadioButton rbtnSim, rbtnNao;
    private long idEvento;
    private Button btnVoltarInformacoes, btnAvancarInformacoes;
    private static final String TAG = "OutrasInformacoes";
    double valorEvento;
    DAO dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_informacoes);


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


        editTextDataPrevista.addTextChangedListener(new IncluirMascara(editTextDataPrevista));
        editTextDataFim.addTextChangedListener(new IncluirMascara(editTextDataFim));
        editTextPrazo.addTextChangedListener(new IncluirMascara(editTextPrazo));


        editTextHorarioInicio.addTextChangedListener(new HorarioTextWatcher(editTextHorarioInicio));
        editTextHorarioFim.addTextChangedListener(new HorarioTextWatcher(editTextHorarioFim));


        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbtnSimInformacoes) {
                editTextValorEvento.setEnabled(true);
                editTextValorEvento.setHint("Insira o valor do evento");
            } else if (checkedId == R.id.rbtnNaoInformacoes) {
                editTextValorEvento.setEnabled(false);
                editTextValorEvento.setHint("Não é necessário adicionar valor");
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


        editTextDataPrevista.addTextChangedListener(dateTextWatcher);
        editTextDataFim.addTextChangedListener(dateTextWatcher);


        // Recuperar o idEvento do Intent
        idEvento = getIntent().getLongExtra("ID_EVENTO", -1);
        Log.d(TAG, "ID do evento recebido: " + idEvento);


        findViewById(R.id.btnAvancarInformacoes).setOnClickListener(this::avancarInformacoes);
        findViewById(R.id.btnVoltarInformacoes).setOnClickListener(this::voltarInformacoes);
    }


    private void verificarDatas() {
        String dataInicio = editTextDataPrevista.getText().toString();
        String dataFim = editTextDataFim.getText().toString();


        TextView txtHorarioInicio = findViewById(R.id.txtHorarioInicio);
        TextView txtHorarioFim = findViewById(R.id.txtHorarioFim);


        if (dataInicio.equals(dataFim)) {
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




    public void avancarInformacoes(View view) {
        String dataPrevis = editTextDataPrevista.getText().toString();
        String dataFim = editTextDataFim.getText().toString();
        String horarioInicio = editTextHorarioInicio.getText().toString();
        String horarioFim = editTextHorarioFim.getText().toString();
        String prazo = editTextPrazo.getText().toString();
        String local = editTextLocal.getText().toString();
        String valorEventoString = editTextValorEvento.getText().toString();


        String pago = rbtnSim.isChecked() ? "Sim" : "Não";

        double valorEvento = 0.0;
        if (rbtnSim.isChecked()) {
            try {
                valorEvento = Double.parseDouble(valorEventoString);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valor do evento deve ser um número válido.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Create the Informacoes object
        Informacoes informacoes = new Informacoes(dataPrevis, dataFim, horarioInicio, horarioFim, prazo, local, valorEvento, pago);

        // Save the information to the database
        DAO dao = new DAO(this);
        dao.inserirInformacoes(informacoes, idEvento);

        // Navigate to the next screen
        Intent intent = new Intent(this, EventoParticipante.class);
        intent.putExtra("ID_EVENTO", idEvento);
        startActivity(intent);
        finish();
    }

    private void carregarInformacoesEvento() {
        Evento evento = dao.buscarEventoPorId(idEvento);
        if (evento != null) {
            editTextDataPrevista.setText(evento.getDataEvento());
            editTextLocal.setText(evento.getLocalEvento());
        }
    }


    public void voltarInformacoes(View view) {
        Intent in = new Intent(this, CriarEvento.class);
        in.putExtra("ID_EVENTO", idEvento);
        startActivity(in);
    }




}
