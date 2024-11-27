package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Certificado;
import com.example.tasktide.Objetos.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InfoCertificado extends AppCompatActivity {

    private TextView txtNomeCertificadoInfo, txtQntdHorasCertificadoInfo, txtTipoCertificadoInfo, txtDataCertificadoInfo;
    private DAO dao;
    private long usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_certificado);

        // Inicializando as TextViews
        txtNomeCertificadoInfo = findViewById(R.id.txtNomeCertificadoInfo);
        txtQntdHorasCertificadoInfo = findViewById(R.id.txtQntdHorasCertificadoInfo);
        txtTipoCertificadoInfo = findViewById(R.id.txtTipoCertificadoInfo);
        txtDataCertificadoInfo = findViewById(R.id.txtDataCertificadoInfo);

        // Inicializando a instância do DAO para acessar o banco de dados
        dao = new DAO(this);

        // Recebe o ID do certificado passado pela Intent
        Intent intent = getIntent();
        long idCertificado = intent.getLongExtra("id_certificado", -1);

        // Caso o idCertificado não seja válido, não faz nada
        if (idCertificado == -1) {
            Toast.makeText(this, "Certificado não encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buscar o certificado no banco com o ID
        Certificado certificado = dao.buscarCertificadoPorId(idCertificado);
        if (certificado != null) {
            // Preencher as TextViews com os dados do certificado
            txtNomeCertificadoInfo.setText(certificado.getNomeCertificado());
            txtQntdHorasCertificadoInfo.setText(formatarHorasExibicao(certificado.getHorasCertificado())); // Use apenas uma função para exibir as horas
            txtTipoCertificadoInfo.setText(certificado.getTipoCertificado());
            txtDataCertificadoInfo.setText(formatarData(certificado.getDataEmissao()));
        } else {
            Toast.makeText(this, "Certificado não encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatarData(String data) {
        if (data == null || data.isEmpty()) {
            return "Data inválida";
        }
        try {
            SimpleDateFormat sdfEntrada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = sdfEntrada.parse(data);

            SimpleDateFormat sdfSaida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdfSaida.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Data inválida";
        }
    }

    private String formatarHorasExibicao(String horasStr) {
        if (horasStr == null || horasStr.isEmpty()) {
            return "Horas inválidas";
        }
        try {
            // Convertendo string decimal para número
            double horasDecimais = Double.parseDouble(horasStr);
            int horas = (int) horasDecimais; // Parte inteira (horas)
            int minutos = (int) Math.round((horasDecimais - horas) * 60); // Parte decimal convertida em minutos

            // Retornar formato amigável
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

    public void voltarInfoCertificado(View view){
        Intent in = new Intent(this,MeusCertificados.class);
        startActivity(in);
    }
}