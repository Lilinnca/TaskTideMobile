package com.example.tasktide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Certificado;

import java.util.ArrayList;

public class MeusCertificados extends AppCompatActivity {
    private LinearLayout linearLayoutCertificados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_certificados);

        linearLayoutCertificados = findViewById(R.id.linearLayoutCertificados);

        // Recuperar os certificados do banco de dados
        DAO dao = new DAO(this);
        ArrayList<Certificado> listaCertificados = dao.listarCertificados(getUsuarioId());

        // Adicionar certificados ao layout
        for (Certificado certificado : listaCertificados) {
            adicionarCertificadoAoLayout(certificado);
        }
    }

    // Adiciona a imagem do certificado ao layout com base nos dados do banco
    private void adicionarCertificadoAoLayout(Certificado certificado) {
        ImageButton imageButtonCertificado = new ImageButton(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                convertDpToPx(150), // Largura
                convertDpToPx(130)  // Altura
        );
        imageButtonCertificado.setLayoutParams(layoutParams);
        imageButtonCertificado.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        imageButtonCertificado.setPadding(10, 10, 10, 10);
        imageButtonCertificado.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        // Exibe uma imagem genérica ou com base no tipo
        imageButtonCertificado.setImageResource(R.drawable.certificados_meuscertificados);

        // Clique no certificado para visualizar detalhes ou PDF
        imageButtonCertificado.setOnClickListener(view -> {
            Intent intent = new Intent(MeusCertificados.this, InfoCertificado.class);
            intent.putExtra("certificado_id", certificado.getIdCertificado()); // Passar ID do certificado
            startActivity(intent);
        });

        linearLayoutCertificados.addView(imageButtonCertificado);
    }

    // Recupera o ID do usuário logado
    private int getUsuarioId() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getInt("userId", -1); // Retorna -1 caso não encontre
    }

    // Função auxiliar para converter dp para pixels
    private int convertDpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    // Navegação
    public void inicialMC(View view) {
        startActivity(new Intent(MeusCertificados.this, TelaInicial.class));
    }

    public void addEventoMC(View view) {
        startActivity(new Intent(MeusCertificados.this, CriarEvento.class));
    }

    public void meusEventosMC(View view) {
        startActivity(new Intent(this, MeusEventosParticipante.class));
    }

    public void localizacaoMC(View view) {
        startActivity(new Intent(this, Localizacao.class));
    }

    public void perfilMC(View view) {
        startActivity(new Intent(MeusCertificados.this, MinhaConta.class));
    }
}
