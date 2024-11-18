package com.example.tasktide;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Certificado;

import java.util.List;

public class MeusCertificados extends AppCompatActivity {

    private LinearLayout linearLayoutCertificados;
    private DAO dao;
    private HorizontalScrollView certificadosContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_certificados);

        certificadosContainer = findViewById(R.id.certificadosContainer);
        linearLayoutCertificados = findViewById(R.id.linearLayoutCertificados);

        dao = new DAO(this);

        carregarCertificados();
    }

    private void carregarCertificados() {
        List<Certificado> certificados = dao.getAllCertificados();
        linearLayoutCertificados.removeAllViews(); // Limpa quaisquer certificados antigos antes de carregar.

        if (certificados == null || certificados.isEmpty()) {
            Log.e("MeusCertificados", "Nenhum certificado encontrado.");
            TextView noCertificadosText = new TextView(this);
            noCertificadosText.setText("Nenhum certificado encontrado.");
            linearLayoutCertificados.addView(noCertificadosText);
            return;
        }

        for (Certificado certificado : certificados) {
            Log.d("MeusCertificados", "Certificado recuperado: " + certificado.getNomeCertificado());
            adicionarNovoCertificado(certificado);
        }

    }

    private void adicionarNovoCertificado(Certificado certificado) {
        try {
            Log.d("MeusCertificados", "Adicionando certificado: " + certificado.getNomeCertificado());

            ImageButton imageButton = new ImageButton(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dpToPx(170),
                    dpToPx(110)
            );
            params.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
            imageButton.setLayoutParams(params);
            imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageButton.setImageResource(R.drawable.certificados_meuscertificados);
            imageButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            imageButton.setOnClickListener(v -> {
                Certificado certificadoCompleto = dao.buscarCertificadoPorId(certificado.getIdCertificado());
                if (certificadoCompleto != null) {
                    Intent intent = new Intent(this, InfoCertificado.class);
                    intent.putExtra("certificado_id", certificadoCompleto.getIdCertificado());
                    intent.putExtra("certificado_nome", certificadoCompleto.getNomeCertificado());
                    intent.putExtra("certificado_tipo", certificadoCompleto.getTipoCertificado());
                    intent.putExtra("certificado_data", certificadoCompleto.getDataEmissao());
                    intent.putExtra("certificado_horas", certificadoCompleto.getHorasCertificado());
                    startActivity(intent);
                }
            });

            linearLayoutCertificados.addView(imageButton);

        } catch (Exception e) {
            Log.e("MeusCertificados", "Erro ao adicionar certificado", e);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void telainicial(View view) {
        Intent intent = new Intent(MeusCertificados.this, TelaInicial.class);
        startActivity(intent);
    }

    public void telacriarevento(View view) {
        Intent intent = new Intent(MeusCertificados.this, CriarEvento.class);
        startActivity(intent);
    }

    public void telaperfil(View view) {
        Intent intent = new Intent(MeusCertificados.this, MinhaConta.class);
        startActivity(intent);
    }

    public void adicionarcertificado(View view) {
        Intent intent = new Intent(this, MeusCertificadosInseridos.class);
        startActivity(intent);
    }
}