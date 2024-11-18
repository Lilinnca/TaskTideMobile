package com.example.tasktide;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.tasktide.DAO.DAO;

import java.io.File;
import java.util.ArrayList;

public class MeusCertificadosInseridos extends Activity {

    private static final int PICK_PDF_REQUEST = 1;
    private EditText editTextNomeCertificado, editTextHoras, editTextData;
    private Spinner spinnerTipoCertificado;
    private TextView txtSelectedFile;
    private String selectedPdfPath = null;
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_certificados_inseridos);

        editTextNomeCertificado = findViewById(R.id.editTextNomeCertificado);
        editTextHoras = findViewById(R.id.editTextHoras);
        editTextData = findViewById(R.id.editTextData);
        spinnerTipoCertificado = findViewById(R.id.spinnerTipoCertificado);
        txtSelectedFile = findViewById(R.id.txt_selected_file);

        Button btnSelecionarPDF = findViewById(R.id.btnSelecionarPDF);
        Button btnAdicionarCertificado = findViewById(R.id.btnAdicionarCertificado);

        dao = new DAO(this);

        editTextData.addTextChangedListener(new IncluirMascara(editTextData));
        editTextHoras.addTextChangedListener(new HorarioTextWatcher(editTextHoras));

        btnSelecionarPDF.setOnClickListener(v -> selectPdfFile());

        btnAdicionarCertificado.setOnClickListener(v -> insertCertificate());
    }

    private void selectPdfFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Selecione um arquivo PDF"), PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            selectedPdfPath = uri.getPath();
            txtSelectedFile.setText(selectedPdfPath != null ? selectedPdfPath : "Arquivo não identificado");
        }
    }


    private void insertCertificate() {
        String nomeCertificado = editTextNomeCertificado.getText().toString().trim();
        String tipoCertificado = spinnerTipoCertificado.getSelectedItem().toString();
        String dataEmissao = editTextData.getText().toString().trim();
        String horasCertificado = editTextHoras.getText().toString().trim();

        if (nomeCertificado.isEmpty() || tipoCertificado.isEmpty() || dataEmissao.isEmpty() || horasCertificado.isEmpty() || selectedPdfPath == null) {
            Toast.makeText(this, "Preencha todos os campos e selecione um arquivo PDF!", Toast.LENGTH_SHORT).show();
            return;
        }

        insertCertificateToDatabase(nomeCertificado, tipoCertificado, dataEmissao, horasCertificado);

        savePdfName(selectedPdfPath);

        Toast.makeText(this, "Certificado inserido com sucesso!", Toast.LENGTH_SHORT).show();

        ArrayList<Integer> certificados = new ArrayList<>();



        Intent intent = new Intent(this, MeusCertificados.class);
        intent.putIntegerArrayListExtra("certificados", certificados);
        startActivity(intent);
        finish();
    }

    private void insertCertificateToDatabase(String nomeCertificado, String tipoCertificado, String dataEmissao, String horasCertificado) {
        SQLiteDatabase db = dao.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome_certificado", nomeCertificado);
        values.put("tipo_certificado", tipoCertificado);
        values.put("data_emissao", dataEmissao);
        values.put("horas_certificado", horasCertificado);

        long resultado = db.insert("Certificados", null, values);
        if (resultado != -1) {
            Log.i("DAO", "Certificado inserido com sucesso.");
        } else {
            Log.e("DAO", "Erro ao inserir certificado.");
        }
        db.close();
    }

    private void savePdfName(String pdfName) {
        // Verifica se o nome do PDF não está vazio ou nulo
        if (pdfName == null || pdfName.trim().isEmpty()) {
            Log.e("DAO", "O nome do PDF está vazio ou nulo. Operação cancelada.");
            return;
        }

        SQLiteDatabase db = null;

        try {
            // Obtém a instância do banco de dados para escrita
            db = dao.getWritableDatabase();

            // Prepara os valores para inserção
            ContentValues values = new ContentValues();
            values.put("nome_pdf", pdfName);

            // Insere o registro no banco de dados
            long resultado = db.insert("tabela_pdfs", null, values);

            if (resultado != -1) {
                Log.i("DAO", "PDF salvo com sucesso com ID: " + resultado);
            } else {
                Log.e("DAO", "Erro ao salvar o PDF.");
            }
        } catch (Exception e) {
            Log.e("DAO", "Erro ao salvar o PDF: " + e.getMessage(), e);
        } finally {
            // Fecha o banco de dados, garantindo a liberação de recursos
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

}