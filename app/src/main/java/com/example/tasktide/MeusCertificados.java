package com.example.tasktide;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private LinearLayout linearLayoutCertificadosEventos;
    private TextView txtHoras;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_certificados);

        // Inicializando os componentes da UI
        linearLayoutCertificados = findViewById(R.id.linearLayoutCertificados);
        linearLayoutCertificadosEventos = findViewById(R.id.linearLayoutEventoCertificados);
        txtHoras = findViewById(R.id.txtHoras);

        dao = new DAO(this);

        // Verificando se o idEvento foi passado no Intent
        int idEvento = getIntent().getIntExtra("id_evento", -1);  // Aqui o id_evento é recuperado do Intent
        if (idEvento != -1) {
            // Se o idEvento estiver presente, carrega os certificados para o evento específico
            carregarCertificadosGerados(idEvento);
        } else {
            // Se o idEvento não for encontrado, exibe os certificados gerais
            carregarCertificados();
        }
    }

    private void carregarCertificadosGerados(int idEvento) {
        List<Certificado> certificados = dao.getCertificadosPorEvento(idEvento); // Buscar certificados por evento
        linearLayoutCertificadosEventos.removeAllViews(); // Limpa certificados antigos

        if (certificados == null || certificados.isEmpty()) {
            Log.e("MeusCertificados", "Nenhum certificado gerado encontrado para o evento.");
            TextView noCertificadosText = new TextView(this);
            noCertificadosText.setText("Nenhum certificado gerado encontrado para este evento.");
            linearLayoutCertificadosEventos.addView(noCertificadosText);
            return;
        }

        for (Certificado certificado : certificados) {
            Log.d("MeusCertificados", "Certificado gerado recuperado: " + certificado.getNomeCertificado());
            adicionarNovoCertificadoGerado(certificado);
        }
    }


    private void adicionarNovoCertificadoGerado(Certificado certificado) {
        try {
            Log.d("MeusCertificados", "Adicionando certificado gerado: " + certificado.getNomeCertificado());

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
                // Aqui, você já tem os dados do certificado
                Intent intent = new Intent(this, InfoCertificado.class);
                intent.putExtra("id_certificado", certificado.getIdCertificado());
                intent.putExtra("nome_certificado", certificado.getNomeCertificado());
                intent.putExtra("tipo_certificado", certificado.getTipoCertificado());
                intent.putExtra("data_emissao", certificado.getDataEmissao());
                intent.putExtra("horas_certificado", certificado.getHorasCertificado());
                startActivity(intent);
            });

            linearLayoutCertificadosEventos.addView(imageButton);
        } catch (Exception e) {
            Log.e("MeusCertificados", "Erro ao adicionar certificado gerado", e);
        }
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

        // Inicializar soma de horas como um decimal
        double totalHoras = 0.0;

        for (Certificado certificado : certificados) {
            Log.d("MeusCertificados", "Certificado recuperado: " + certificado.getNomeCertificado());

            String horasStr = certificado.getHorasCertificado();
            if (horasStr == null || horasStr.isEmpty()) {
                Log.e("MeusCertificados", "Horas inválidas para certificado: " + certificado.getNomeCertificado());
                continue; // Pula certificados com valores inválidos
            }

            try {
                // Converte string para número decimal e soma
                totalHoras += Double.parseDouble(horasStr);
            } catch (NumberFormatException e) {
                Log.e("MeusCertificados", "Erro ao converter horas para número no certificado: " + certificado.getNomeCertificado(), e);
            }

            adicionarNovoCertificado(certificado);
        }

        // Formatar o total de horas para exibição
        String totalHorasFormatado = formatarHorasExibicao(String.valueOf(totalHoras));
        txtHoras.setText(totalHorasFormatado);

        // Salvar o total de horas em SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("horas", totalHorasFormatado); // Salva o valor formatado (ex.: "5 horas e 30 minutos")
        editor.apply();
    }


    private String formatarHorasExibicao(String horasStr) {
        if (horasStr == null || horasStr.isEmpty()) {
            return "Horas inválidas";
        }
        try {
            double horasDecimais = Double.parseDouble(horasStr);
            int horas = (int) horasDecimais; // Parte inteira (horas)
            int minutos = (int) Math.round((horasDecimais - horas) * 60); // Parte decimal convertida em minutos

            if (minutos == 0) {
                return horas + " horas";
            } else {
                return horas + " horas e " + minutos + " minutos";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "Horas inválidas";
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
                    intent.putExtra("id_certificado", certificadoCompleto.getIdCertificado());
                    intent.putExtra("nome_certificado", certificadoCompleto.getNomeCertificado());
                    intent.putExtra("tipo_certificado", certificadoCompleto.getTipoCertificado());
                    intent.putExtra("data_emissao", certificadoCompleto.getDataEmissao());
                    intent.putExtra("horas_certificado", certificadoCompleto.getHorasCertificado());
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

    public void inicialMC(View view) {
        Intent intent = new Intent(MeusCertificados.this, TelaInicial.class);
        startActivity(intent);
    }

    public void localizacaoMC(View view){
        Intent in = new Intent(this, Localizacao.class);
        startActivity(in);
    }

    public void addEventoMC(View view) {
        Intent intent = new Intent(this, CriarEvento.class);
        startActivity(intent);
    }

    public void perfilMC(View view) {
        Intent intent = new Intent(MeusCertificados.this, MinhaConta.class);
        startActivity(intent);
    }

    public void meusEventosMC(View view){
        Intent in = new Intent(this, MeusEventosParticipante.class);
        startActivity(in);
    }

    public void adicionarcertificado(View view) {
        Intent intent = new Intent(this, MeusCertificadosInseridos.class);
        startActivity(intent);
    }
}