package com.example.tasktide;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MeusCertificadosInseridos extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST_CODE = 1;
    private static final int REQUEST_PERMISSION_CODE = 2;
    private TextView selectedFileTextView;
    private ArrayList<String> certificados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_certificados_inseridos);

        certificados = new ArrayList<>();

        Button selectPdfButton = findViewById(R.id.button_select_pdf);
        selectedFileTextView = findViewById(R.id.text_selected_file);

        selectPdfButton.setOnClickListener(v -> {
            // Verificar permissões
            if (ContextCompat.checkSelfPermission(MeusCertificadosInseridos.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MeusCertificadosInseridos.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
            } else {
                // Iniciar o seletor de arquivos
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(Intent.createChooser(intent, "Escolha um arquivo PDF"), PICK_PDF_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                String fileName = getFileName(uri);
                selectedFileTextView.setText(fileName);
                // Fazer o upload do arquivo
                uploadFile(uri);
            }
        }
    }

    private String getFileName(Uri uri) {
        String fileName = null;
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (cursor.moveToFirst()) {
                        fileName = cursor.getString(nameIndex);
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        return fileName;
    }

    private void uploadFile(Uri fileUri) {
        OkHttpClient client = new OkHttpClient();

        // Obter o caminho do arquivo
        String filePath = fileUri.getPath();
        File file = new File(filePath);

        RequestBody requestFile = RequestBody.create(file, MediaType.parse("application/pdf"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Request.Builder builder = new Request.Builder();
        Request request = builder
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(MeusCertificadosInseridos.this, "Falha no upload", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(MeusCertificadosInseridos.this, "Upload bem-sucedido", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(MeusCertificadosInseridos.this, "Falha no upload", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, iniciar seletor de arquivos
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(Intent.createChooser(intent, "Escolha um arquivo PDF"), PICK_PDF_REQUEST_CODE);
            } else {
                Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void voltarmeuscertificados(View view) {
        Intent in = new Intent(MeusCertificadosInseridos.this, MeusCertificados.class);
        startActivity(in);
    }


        // Método chamado ao clicar no botão de adicionar certificado
        public void adicionarCertificado(View view) {
            // Aqui você pode abrir um seletor de arquivos para o usuário escolher o certificado (PDF)
            // Simulando a adição de um certificado
            certificados.add("caminho/do/certificado.pdf");

            // Depois de adicionar, vamos enviar para a tela de Meus Certificados
            Intent intent = new Intent(this, MeusCertificados.class);
            intent.putStringArrayListExtra("certificados", certificados);
            startActivity(intent);
        }
    }

