package com.example.tasktide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MeusCertificados extends AppCompatActivity {
    private LinearLayout linearLayoutCertificados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_certificados);

        linearLayoutCertificados = findViewById(R.id.linearLayoutCertificados);

        // Recebe os certificados enviados pela outra activity
        ArrayList<String> certificados = getIntent().getStringArrayListExtra("certificados");

        if (certificados != null) {
            for (String certificado : certificados) {
                adicionarCertificadoAoLayout(certificado);
            }
        }
    }

    private void adicionarCertificadoAoLayout(String certificado) {
        // Cria uma nova visualização de certificado
        TextView certificadoView = new TextView(this);
        certificadoView.setText(certificado);
        certificadoView.setPadding(10, 10, 10, 10);

        // Adiciona ao LinearLayout dentro do ScrollView
        linearLayoutCertificados.addView(certificadoView);
    }


    private boolean verificarPdfSelecionado() {
        // Lógica para verificar se um PDF foi selecionado
        return false;
    }

    public void telainicial(View view) {
        Intent in = new Intent(MeusCertificados.this, TelaInicial.class);
        startActivity(in);
    }

    public void meuscertificadosinseridos(View view) {
        Intent in = new Intent(MeusCertificados.this, MeusCertificadosInseridos.class);
        startActivity(in);
    }

    public void telacriarevento(View view) {
        Intent in = new Intent(MeusCertificados.this, CriarEvento.class);
        startActivity(in);
    }

    public void telaperfil(View view) {
        Intent in = new Intent(MeusCertificados.this, MinhaConta.class);
        startActivity(in);
    }

    public void infoCertificado(View view) {
        Intent in = new Intent(MeusCertificados.this, InfoCertificado.class);
        startActivity(in);
    }
}