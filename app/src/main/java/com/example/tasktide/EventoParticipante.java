package com.example.tasktide;


import android.content.Intent;
import android.os.Bundle;
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
    private Button btnVoltarParaTelaCriarEvento, btnIrParaTelaOutrasInformacoes;
    private Spinner spnQuantParticipantes;
    private long idEvento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_participante);


        // Inicialização dos componentes
        chbDiscentesEM = findViewById(R.id.chbDiscentesEM);
        chbDiscentesES = findViewById(R.id.chbDiscentesES);
        chbDocentes = findViewById(R.id.chbDocentes);
        chbServidoresGeral = findViewById(R.id.chbServidoresGeral);
        btnVoltarParaTelaCriarEvento = findViewById(R.id.btnVoltarParaTelaCriarEvento);
        btnIrParaTelaOutrasInformacoes = findViewById(R.id.btnIrParaTelaOutrasInformacoes);
        spnQuantParticipantes = findViewById(R.id.spnQuantParticipantes);




        // Recuperar o idEvento do Intent
        idEvento = getIntent().getLongExtra("ID_EVENTO", -1);


        // Configuração do Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.QuantidadePessoas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnQuantParticipantes.setAdapter(adapter);


        // Listener para o botão próximo
        btnIrParaTelaOutrasInformacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnyCheckBoxChecked()) {
                    Toast.makeText(EventoParticipante.this, "Selecione pelo menos um participante.", Toast.LENGTH_SHORT).show();
                } else {
                    // Salvar participantes selecionados
                    salvarParticipantes();
                    // Navegar para a próxima tela (OutrasInformacoes)
                    Intent intent = new Intent(EventoParticipante.this, EventoInformacoes.class);
                    intent.putExtra("ID_EVENTO", idEvento);  // Passar idEvento para a próxima atividade
                    startActivity(intent);
                }
            }
        });


        // Listener para o botão voltar
        btnVoltarParaTelaCriarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Voltar para a tela CriarEvento
                startActivity(new Intent(EventoParticipante.this, CriarEvento.class));
            }
        });
    }


    private boolean isAnyCheckBoxChecked() {
        return chbDiscentesEM.isChecked() || chbDiscentesES.isChecked() || chbDocentes.isChecked() || chbServidoresGeral.isChecked();
    }


    private void salvarParticipantes() {
        String quantParticipantes = spnQuantParticipantes.getSelectedItem().toString();
        DAO dao = new DAO(this);
        Participantes participantes = new Participantes(quantParticipantes);
        dao.inserirParticipantes(participantes, idEvento);
    }
}
