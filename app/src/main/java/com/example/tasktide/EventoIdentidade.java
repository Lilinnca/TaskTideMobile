package com.example.tasktide;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;


import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Identidade;


public class EventoIdentidade extends AppCompatActivity {


    private static final String TAG = "ConfirmarIdentidade";
    private EditText edtxtNome, edtxtCargo, edtxtDepartamento, edtxtContato;
    private long idEvento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_identidade);


        edtxtNome = findViewById(R.id.edtxtNome);
        edtxtCargo = findViewById(R.id.edtxtCargo);
        edtxtDepartamento = findViewById(R.id.edtxtDepartamento);
        edtxtContato = findViewById(R.id.edtxtContato);


        // Recuperar o idEvento do Intent
        idEvento = getIntent().getLongExtra("ID_EVENTO", -1);


        findViewById(R.id.btnIrParaTelaQuaseLa).setOnClickListener(v -> {
            String nome = edtxtNome.getText().toString().trim();
            String cargo = edtxtCargo.getText().toString().trim();
            String departamento = edtxtDepartamento.getText().toString().trim();
            String contato = edtxtContato.getText().toString().trim();


            Identidade identidade = new Identidade(nome, cargo, departamento, contato);
            long id = new DAO(EventoIdentidade.this).inserirIdentidade(identidade, idEvento);


            if (id != -1) {
                Log.i(TAG, "Dados de identidade inseridos com sucesso.");
                startActivity(new Intent(EventoIdentidade.this, EventoConfirmacao.class));
            } else {
                Log.e(TAG, "Erro ao inserir dados de identidade.");
            }
        });


        findViewById(R.id.btnVoltarParaTelaOutrasInformacoes).setOnClickListener(v -> {
            startActivity(new Intent(EventoIdentidade.this, EventoInformacoes.class));
        });
    }
}
