package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
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

        // Inicializando os campos de texto
        txtNomeCertificadoInfo = findViewById(R.id.txtNomeCertificadoInfo);
        txtQntdHorasCertificadoInfo = findViewById(R.id.txtQntdHorasCertificadoInfo);
        txtTipoCertificadoInfo = findViewById(R.id.txtTipoCertificadoInfo);
        txtDataCertificadoInfo = findViewById(R.id.txtDataCertificadoInfo);

        dao = new DAO(this);

        // Recuperando o ID do certificado via Intent
        Intent intent = getIntent();
        long idCertificado = intent.getLongExtra("id_certificado", -1);

        if (idCertificado != -1) {
            Certificado certificado = dao.buscarCertificadoPorId(idCertificado);
            if (certificado != null) {
                // Populando a interface com os dados do certificado
                txtNomeCertificadoInfo.setText(certificado.getNomeCertificado());
                txtQntdHorasCertificadoInfo.setText(formatarHoras(certificado.getHorasCertificado()));
                txtTipoCertificadoInfo.setText(certificado.getTipoCertificado());
                txtDataCertificadoInfo.setText(formatarData(certificado.getDataEmissao()));
            } else {
                // Caso o certificado não seja encontrado
                txtNomeCertificadoInfo.setText("Certificado não encontrado");
            }
        } else {
            txtNomeCertificadoInfo.setText("ID de certificado inválido");
        }

        // Busca o ID do usuário
        usuarioId = getUsuarioId();
    }

    // Método para obter o ID do usuário, buscando pelo e-mail armazenado nos SharedPreferences
    private long getUsuarioId() {
        String usuarioEmail = getEmailUsuario();

        if (usuarioEmail == null) {
            return -1; // Se não encontrar o e-mail, retorna -1
        }

        Usuario usuario = dao.buscarUsuarioPorEmail(usuarioEmail);

        if (usuario != null) {
            return usuario.getId();
        } else {
            return -1; // Se não encontrar o usuário no banco, retorna -1
        }
    }

    // Método para recuperar o e-mail do usuário armazenado nos SharedPreferences
    private String getEmailUsuario() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getString("email", null); // Retorna o e-mail ou null se não encontrado
    }

    // Método para formatar a data de emissão do certificado
    private String formatarData(String data) {
        if (data == null || data.isEmpty()) {
            return "Data inválida";
        }
        try {
            SimpleDateFormat sdfEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdfEntrada.parse(data);
            SimpleDateFormat sdfSaida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdfSaida.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Data inválida";
        }
    }

    // Método para formatar as horas do certificado
    private String formatarHoras(String horasStr) {
        if (horasStr == null || horasStr.isEmpty()) {
            return "Horas inválidas";
        }
        try {
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
}
