package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
            txtQntdHorasCertificadoInfo.setText(formatarHoras(certificado.getHorasCertificado()));
            txtTipoCertificadoInfo.setText(certificado.getTipoCertificado());
            txtDataCertificadoInfo.setText(formatarData(certificado.getDataEmissao()));
        } else {
            Toast.makeText(this, "Certificado não encontrado", Toast.LENGTH_SHORT).show();
        }

        txtQntdHorasCertificadoInfo.setText(formatarHorasExibicao(certificado.getHorasCertificado()));

    }

    private long getUsuarioId() {
        String usuarioEmail = getEmailUsuario();

        if (usuarioEmail == null) {
            return -1;
        }

        Usuario usuario = dao.buscarUsuarioPorEmail(usuarioEmail);

        if (usuario != null) {
            return usuario.getId();
        } else {
            return -1;
        }
    }

    private String getEmailUsuario() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getString("email", null);
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


    private String formatarHoras(String horasStr) {
        if (horasStr == null || horasStr.isEmpty()) {
            return "Horas inválidas";
        }
        try {
            // Aqui assumimos que a string de horas segue o formato "HH:mm"
            String[] partes = horasStr.split(":");
            int horas = Integer.parseInt(partes[0]);
            int minutos = Integer.parseInt(partes[1]);

            if (minutos == 0) {
                return horas + " horas";
            } else {
                return horas + " horas e " + minutos + " minutos";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Horas inválidas";
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

}
