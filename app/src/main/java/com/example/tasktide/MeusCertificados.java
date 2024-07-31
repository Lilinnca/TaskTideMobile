package com.example.tasktide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktide.R;

public class MeusCertificados extends AppCompatActivity {

    private LinearLayout linearLayoutCertificados;
    private Button buttonAddCertificado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_certificados);

        linearLayoutCertificados = findViewById(R.id.linearLayoutCertificados);
        buttonAddCertificado = findViewById(R.id.buttonAddCertificado);

        buttonAddCertificado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCertificado();
            }
        });
    }

    private void addCertificado() {
        final ImageButton newCertificado = new ImageButton(this);
        newCertificado.setLayoutParams(new LinearLayout.LayoutParams(150, 130));
        newCertificado.setImageResource(R.drawable.certificados_meuscertificados);
        newCertificado.setContentDescription("Novo Certificado");

        newCertificado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCertificado(newCertificado);
            }
        });

        linearLayoutCertificados.addView(newCertificado);
    }

    private void openCertificado(ImageButton certificado) {
        // Abra a atividade ou exiba o certificado
        Toast.makeText(this, "Certificado Clicado", Toast.LENGTH_SHORT).show();

        // Exemplo para abrir uma nova atividade
        Intent intent = new Intent(this, CertificadoActivity.class);
        // Passe informações adicionais se necessário
        startActivity(intent);
    }
}
