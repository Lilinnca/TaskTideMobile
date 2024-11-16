package com.example.tasktide;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.tasktide.DAO.DAO;

public class MeusCertificadosInseridos extends Activity {
    private static final int PICK_PDF_FILE = 2;
    private LinearLayout horizontalScrollViewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_certificados_inseridos);

        horizontalScrollViewLayout = findViewById(R.id.horizontal_scroll_view_layout);

        // Botão para adicionar certificado
        Button btnAdicionarCertificado = findViewById(R.id.btnAdicionarCertificado);
        btnAdicionarCertificado.setOnClickListener(view -> openFilePicker());
    }

    private void openFilePicker() {
        // Intent para abrir seletor de arquivo
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_PDF_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK && data != null) {
            DAO dao = new DAO(this);
            int userId = getUsuarioId();
            String nomeCertificado = "Certificado Exemplo";
            String tipoCertificado = "Tipo Exemplo";
            String dataEmissao = "2024-11-15";

            dao.inserirCertificado(userId, nomeCertificado, tipoCertificado, dataEmissao);  // Adiciona no banco de dados

            Intent intent = new Intent(MeusCertificadosInseridos.this, MeusCertificados.class);
            startActivity(intent);
            finish();
        }
    }

    // Recupera o ID do usuário logado
    private int getUsuarioId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getInt("userId", -1); // Retorna -1 caso não encontre
    }

    public void voltarMeusCertificadosInseridos(View view) {
        startActivity(new Intent(this, MeusCertificados.class));
    }
}
