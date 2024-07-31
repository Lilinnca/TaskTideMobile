package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class CriarEvento extends AppCompatActivity {

    private EditText etxtNomeEvento;
    private EditText etxtTipoEvento;
    private EditText etxtnHoras;
    private RadioButton rbtnOnline;
    private RadioButton rbtnPresencial;
    private RadioButton rbtnHibrido;
    private Button btnAvancar;
    private Button btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_evento);

        etxtNomeEvento = findViewById(R.id.etxtNomeEvento);
        etxtTipoEvento = findViewById(R.id.etxtmTipoEvento);
        etxtnHoras = findViewById(R.id.etxtnHoras);
        rbtnOnline = findViewById(R.id.rbtnOnline);
        rbtnPresencial = findViewById(R.id.rbtnPresencial);
        rbtnHibrido = findViewById(R.id.rbtnHibrido);
        btnAvancar = findViewById(R.id.btnAvancar);
        btnVoltar = findViewById(R.id.btnVoltar);

        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    eventoparticipante();
                } else {
                    Toast.makeText(CriarEvento.this, "Por favor, preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telainicial();
            }
        });
    }

    private boolean validateFields() {
        String nomeEvento = etxtNomeEvento.getText().toString().trim();
        String tipoEvento = etxtTipoEvento.getText().toString().trim();
        String nHoras = etxtnHoras.getText().toString().trim();

        boolean isRadioButtonChecked = rbtnOnline.isChecked() || rbtnPresencial.isChecked() || rbtnHibrido.isChecked();

        return !nomeEvento.isEmpty() && !tipoEvento.isEmpty() && !nHoras.isEmpty() && isRadioButtonChecked;
    }

    public void eventoparticipante() {
        Intent in = new Intent(CriarEvento.this, EventoParticipante.class);
        startActivity(in);
    }

    public void telainicial() {
        Intent in = new Intent(CriarEvento.this, TelaInicial.class);
        startActivity(in);
    }
}
