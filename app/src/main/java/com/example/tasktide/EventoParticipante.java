package com.example.tasktide;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Participantes;


public class EventoParticipante extends AppCompatActivity {


    private CheckBox chbDiscentesEM, chbDiscentesES, chbDocentes, chbServidoresGeral;
    private Button btnVoltar, btnAvancar;
    private Spinner spnQuantParticipantes;
    private long idEvento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_participante);

        chbDiscentesEM = findViewById(R.id.CheckBoxDiscentesEM);
        chbDiscentesES = findViewById(R.id.checkBoxDiscentesES);
        chbDocentes = findViewById(R.id.checkBoxDocentes);
        chbServidoresGeral = findViewById(R.id.checkBoxServidores);
        btnVoltar = findViewById(R.id.btnVoltarParticipantes);
        btnAvancar = findViewById(R.id.btnAvancarParticipantes);
        spnQuantParticipantes = findViewById(R.id.spnQntdParticipantes);

        idEvento = getIntent().getLongExtra("ID_EVENTO", -1);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.QuantidadePessoas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnQuantParticipantes.setAdapter(adapter);

        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarParticipantes();
                Intent intent = new Intent(EventoParticipante.this, EventoConfirmacao.class);
                intent.putExtra("ID_EVENTO", idEvento);
                startActivity(intent);
                finish();
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventoParticipante.this, EventoConfirmacao.class));
            }
        });
    }


    private boolean isAnyCheckBoxChecked() {
        return chbDiscentesEM.isChecked() || chbDiscentesES.isChecked() || chbDocentes.isChecked() || chbServidoresGeral.isChecked();
    }


    private void salvarParticipantes() {
        String quantParticipantes = spnQuantParticipantes.getSelectedItem().toString();

        // Extrai a primeira parte numérica da string
        String[] partes = quantParticipantes.split(" ");
        int participantes = 0;
        try {
            participantes = Integer.parseInt(partes[0]); // Exemplo: "1" de "1 - 100"
        } catch (NumberFormatException e) {
            Log.e("EventoParticipante", "Erro ao converter número de participantes", e);
        }

        DAO dao = new DAO(this);
        Participantes participantesObj = new Participantes(participantes, isAnyCheckBoxChecked());
        dao.inserirParticipantes(participantesObj, idEvento);
    }

}

