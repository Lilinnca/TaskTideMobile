package com.example.tasktide;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;


import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Identidade;
import com.example.tasktide.Objetos.Informacoes;


public class EventoInformacoes extends AppCompatActivity {


    private EditText edtxtDataPrevis, edtxtTimeInicio, edtxtTimeTermino, edtxtLocal;
    private long idEvento;
    private static final String TAG = "OutrasInformacoes";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_informacoes);


        edtxtDataPrevis = findViewById(R.id.edtxtDataPrevis);
        edtxtTimeInicio = findViewById(R.id.etxtxTimeInicio);
        edtxtTimeTermino = findViewById(R.id.edtxtTimeTermino);
        edtxtLocal = findViewById(R.id.edtxtLocal);


        // Recuperar o idEvento do Intent
        idEvento = getIntent().getLongExtra("ID_EVENTO", -1);
        Log.d("OutrasInformacoes", "ID do evento recebido: " + idEvento);


        // Configuração dos botões
        // findViewById(R.id.btnIrParaTelaConfirmarIdentidade).setOnClickListener(this::telaIrConfirmarIdentidade);
        findViewById(R.id.btnVoltarParaTelaSobreParticipantes).setOnClickListener(this::voltarParaTelaSobreParticipantes);


        idEvento = getIntent().getLongExtra("ID_EVENTO", -1);


        findViewById(R.id.btnIrParaTelaConfirmarIdentidade).setOnClickListener(v -> {
            String dataPrevis = edtxtDataPrevis.getText().toString().trim();
            String horarioInicio = edtxtTimeInicio.getText().toString().trim();
            String horarioTermino = edtxtTimeTermino.getText().toString().trim();
            String local = edtxtLocal.getText().toString().trim();


            Informacoes informacoes = new Informacoes(dataPrevis, horarioInicio, horarioTermino, local);
            long id = new DAO(EventoInformacoes.this).inserirInformacoes(informacoes, idEvento);


            if (id != -1) {
                Log.i(TAG, "Dados de identidade inseridos com sucesso.");
                startActivity(new Intent(EventoInformacoes.this, EventoIdentidade.class));
            } else {
                Log.e(TAG, "Erro ao inserir dados de identidade.");
            }
        });


    }


    /** public void inserirInformacoes() {
     String dataPrevis = edtxtDataPrevis.getText().toString().trim();
     String horarioInicio = edtxtTimeInicio.getText().toString().trim();
     String horarioTermino = edtxtTimeTermino.getText().toString().trim();
     String local = edtxtLocal.getText().toString().trim();


     if (idEvento == -1) {
     Log.e("OutrasInformacoes", "ID do evento inválido");
     return;
     }


     DAO dao = new DAO(this);
     Informacoes informacoes = new Informacoes(dataPrevis, horarioInicio, horarioTermino, local);
     long result = dao.inserirInformacoes(informacoes, idEvento);


     if (result != -1) {
     Log.i("OutrasInformacoes", "Informações inseridas com sucesso. ID: " + result);
     } else {
     Log.e("OutrasInformacoes", "Erro ao inserir informações.");
     }
     } **/


    /** public void telaIrConfirmarIdentidade(View view) {
     // Inserir informações antes de mudar para a próxima tela
     inserirInformacoes();


     Intent intent = new Intent(OutrasInformacoes.this, ConfirmarIdentidade.class);
     intent.putExtra("ID_EVENTO", idEvento);
     startActivity(intent);
     } **/


    public void voltarParaTelaSobreParticipantes(View view) {
        Intent in = new Intent(EventoInformacoes.this, EventoParticipante.class);
        startActivity(in);
    }
}


