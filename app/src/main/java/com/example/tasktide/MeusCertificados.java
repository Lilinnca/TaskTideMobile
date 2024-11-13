package com.example.tasktide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MeusCertificados extends AppCompatActivity {
    private LinearLayout linearLayoutCertificados;
    private ArrayList<Integer> certificados; // Lista de IDs das imagens dos certificados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_certificados);

        linearLayoutCertificados = findViewById(R.id.linearLayoutCertificados);

        // Recebe os certificados enviados pela outra activity ou salva o estado
        if (savedInstanceState != null) {
            certificados = savedInstanceState.getIntegerArrayList("certificados");
        } else {
            certificados = getIntent().getIntegerArrayListExtra("certificados");
        }

        // Adicionar certificados ao layout
        if (certificados != null) {
            for (int certificado : certificados) {
                adicionarCertificadoAoLayout(certificado);
            }
        } else {
            certificados = new ArrayList<>();
        }
    }

    // Adiciona a imagem do certificado ao layout
    private void adicionarCertificadoAoLayout(int certificadoResId) {
        ImageButton imageButtonCertificado = new ImageButton(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                convertDpToPx(150), // Largura em pixels
                convertDpToPx(130)  // Altura em pixels
        );
        imageButtonCertificado.setLayoutParams(layoutParams);
        imageButtonCertificado.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        imageButtonCertificado.setPadding(10, 10, 10, 10);
        imageButtonCertificado.setImageResource(certificadoResId);
        imageButtonCertificado.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        imageButtonCertificado.setContentDescription("certificado");

        // Ação de clicar para abrir a tela de InfoCertificado
        imageButtonCertificado.setOnClickListener(v -> {
            Intent intent = new Intent(MeusCertificados.this, InfoCertificado.class);
            intent.putExtra("certificadoResId", certificadoResId);
            startActivity(intent);
        });

        linearLayoutCertificados.addView(imageButtonCertificado);
    }

    // Função auxiliar para converter dp para pixels
    private int convertDpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    // Salva o estado atual da lista de certificados
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("certificados", certificados);
    }

    // Navegação para outras telas
    public void adicionarCertificado(View view) {
        Intent intent = new Intent(MeusCertificados.this, MeusCertificadosInseridos.class);
        intent.putIntegerArrayListExtra("certificados", certificados);
        startActivity(intent);
        finish();
    }

    public void telainicial(View view) {
        Intent in = new Intent(MeusCertificados.this, TelaInicial.class);
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
}
