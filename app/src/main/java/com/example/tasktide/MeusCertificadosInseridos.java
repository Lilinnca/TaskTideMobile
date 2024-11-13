package com.example.tasktide;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.annotation.Nullable;


import java.util.ArrayList;


public class MeusCertificadosInseridos extends Activity {


    private static final int PICK_PDF_FILE = 2;
    private LinearLayout horizontalScrollViewLayout;
    private ArrayList<Integer> certificados; // Lista de certificados


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_certificados_inseridos);


        // Recuperar a lista de certificados já existentes (se houver)
        certificados = getIntent().getIntegerArrayListExtra("certificados");
        if (certificados == null) {
            certificados = new ArrayList<>(); // Inicializar se for null
        }


        horizontalScrollViewLayout = findViewById(R.id.horizontal_scroll_view_layout);


        // Botão para adicionar certificado
        Button btnAdicionarCertificado = findViewById(R.id.btnAdicionarCertificado);
        btnAdicionarCertificado.setOnClickListener(view -> openFilePicker());


        // Atualiza o layout com os certificados existentes
        for (int certificado : certificados) {
            addCertificadoToView(certificado);
        }
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
        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    addCertificadoToView(R.drawable.certificados_meuscertificados); // Exemplo de certificado
                    certificados.add(R.drawable.certificados_meuscertificados);


                    // Salvar a lista de certificados e enviar para a próxima tela
                    Intent intent = new Intent(MeusCertificadosInseridos.this, MeusCertificados.class);
                    intent.putIntegerArrayListExtra("certificados", certificados);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }


    private void addCertificadoToView(int certificadoResId) {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(certificadoResId);


        // Definir tamanho da imagem corretamente
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                300, 400); // Largura e altura fixas
        imageView.setLayoutParams(layoutParams);


        // Adiciona a imagem ao layout da HorizontalScrollView
        horizontalScrollViewLayout.addView(imageView);
    }

    public void voltarMeusCertificadosInseridos(View view){
        Intent in = new Intent(this, MeusCertificados.class);
        startActivity(in);
    }


    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
