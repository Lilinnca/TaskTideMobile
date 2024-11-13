package com.example.tasktide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventoProgramacao extends AppCompatActivity {

    private LinearLayout dynamicLayoutProgramacao;
    private Button btnVoltarProgramacao, btnAvancarProgramacao;
    private String dataInicioEvento = "07/10/2023";
    private String dataFimEvento = "09/10/2023";
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_programacao);

        dynamicLayoutProgramacao = findViewById(R.id.dynamicLayoutProgramacao);
        btnVoltarProgramacao = findViewById(R.id.btnVoltarProgramacao);
        btnAvancarProgramacao = findViewById(R.id.btnAvancarProgramacao);

        try {
            adicionarDiasProgramacao();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        btnVoltarProgramacao.setOnClickListener(v -> {
                Intent in = new Intent(this, EventoInformacoes.class);
                startActivity(in);
        });

        btnAvancarProgramacao.setOnClickListener(v -> {
            Intent in = new Intent(this, EventoParticipante.class);
            startActivity(in);
        });
    }

    private void adicionarDiasProgramacao() throws ParseException {
        Date dataInicio = sdf.parse(dataInicioEvento);
        Date dataFim = sdf.parse(dataFimEvento);

        Calendar inicio = Calendar.getInstance();
        inicio.setTime(dataInicio);

        Calendar fim = Calendar.getInstance();
        fim.setTime(dataFim);

        int numDias = 0;
        while (!inicio.after(fim)) {
            numDias++;

            TextView textViewDia = new TextView(this);
            textViewDia.setText("Dia " + sdf.format(inicio.getTime()));
            textViewDia.setTextSize(18);
            textViewDia.setPadding(0, 16, 0, 8);
            dynamicLayoutProgramacao.addView(textViewDia);

            EditText editTextProgramacaoDia = new EditText(this);
            editTextProgramacaoDia.setHint("Programação do Dia " + numDias);
            editTextProgramacaoDia.setMinLines(3);
            editTextProgramacaoDia.setMaxLines(6);
            editTextProgramacaoDia.setBackground(getDrawable(android.R.drawable.editbox_background_normal)); // Opcional: Personalize a aparência
            dynamicLayoutProgramacao.addView(editTextProgramacaoDia);

            inicio.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
}
