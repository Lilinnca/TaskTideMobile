package com.example.tasktide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Certificado;

import java.util.List;

public class MeusCertificados extends AppCompatActivity {

    private LinearLayout linearLayoutCertificados;
    private DAO dao;
    private List<Certificado> certificados;
    private HorizontalScrollView certificadosContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_certificados);

        try {
            certificadosContainer = findViewById(R.id.certificadosContainer);
            dao = new DAO(this);

            carregarCertificados();

        } catch (Exception e) {
            Log.e("MeusCertificados", "Erro ao inicializar MeusCertificados", e);
        }
    }

    private void carregarCertificados() {
        try {
            List<Certificado> certificados = dao.getAllCertificados();
            Log.d("MeusCertificados", "Certificados carregados: " + certificados.size());
            for (Certificado certificado : certificados) {
                adicionarNovoCertificado(certificado);
            }
        } catch (Exception e) {
            Log.e("MeusCertificados", "Erro ao carregar certificados", e);
        }
    }

    private void adicionarNovoCertificado(Certificado certificado) {
        try {
            Log.d("MeusCertificados", "Adicionando certificado: " + certificado.getNomeCertificado());

            Button button = new Button(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(170), dpToPx(110));
            params.setMargins(dpToPx(7), dpToPx(10), dpToPx(7), dpToPx(10));
            button.setLayoutParams(params);
            button.setText(certificado.getNomeCertificado());

            button.setOnClickListener(v -> {
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

            certificadosContainer.addView(button);

        } catch (Exception e) {
            Log.e("MeusCertificados", "Erro ao adicionar certificado", e);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void adicionarCertificado(View view) {
        Intent intent = new Intent(MeusCertificados.this, MeusCertificadosInseridos.class);
        startActivity(intent);
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

    public void adicionarcertificado(View view) {
        Intent in = new Intent(this, MeusCertificadosInseridos.class);
        startActivity(in);
    }
}
