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

        idEvento = getIntent().getLongExtra("ID_EVENTO", -1);
        Log.d(TAG, "ID do evento recebido: " + idEvento);




        findViewById(R.id.btnAvancarInformacoes).setOnClickListener(this::avancarInformacoes);
        findViewById(R.id.btnVoltarInformacoes).setOnClickListener(this::voltarInformacoes);
    }


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

    public void avancarInformacoes(View view) {
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

        String pago = rbtnSim.isChecked() ? "Sim" : "Não";

        int pagoInt = "Sim".equalsIgnoreCase(pago) ? 1 : 0;

        double valorEvento = 0.0;
        if (!valorEventoString.isEmpty()) {
            try {
                valorEvento = Double.parseDouble(valorEventoString);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valor do evento deve ser um número válido.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Informacoes informacoes = new Informacoes();
        informacoes.setDataPrevista(dataPrevista);
        informacoes.setDataFim(dataFim);
        informacoes.setHorarioInicio(horarioInicio);
        informacoes.setHorarioTermino(horarioFim);
        informacoes.setPrazo(prazo);
        informacoes.setLocal(local);
        informacoes.setValorEvento(valorEvento);
        informacoes.setPago(pagoInt);

        DAO dao = new DAO(this);
        dao.inserirInformacoes(informacoes, idEvento);

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
