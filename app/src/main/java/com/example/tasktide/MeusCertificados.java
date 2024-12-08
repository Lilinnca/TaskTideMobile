package com.example.tasktide;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

        int idEvento = getIntent().getIntExtra("id_evento", -1);  // Aqui o id_evento é recuperado do Intent
        if (idEvento != -1) {  // Verificando se o idEvento é válido (não é -1)
            // Se o idEvento estiver presente, carrega os certificados para o evento específico
            carregarCertificadosGerados(idEvento);
        } else {
            // Se o idEvento não for encontrado, exibe os certificados gerais
            carregarCertificados();
        }
    }

    // Método para carregar certificados por evento
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

        // Inicializar soma de horas como um decimal
        double totalHorasGeradas = 0.0;

        // Adiciona cada certificado ao container de certificados gerados
        for (Certificado certificado : certificados) {
            Log.d("MeusCertificados", "Certificado gerado recuperado: " + certificado.getNomeCertificado());

            String horasStr = certificado.getHorasCertificado();
            if (horasStr == null || horasStr.isEmpty()) {
                Log.e("MeusCertificados", "Horas inválidas para certificado: " + certificado.getNomeCertificado());
                continue; // Pula certificados com valores inválidos
            }

            try {
                // Remover texto não numérico (como "horas") e converter para número
                String horasNumericas = horasStr.replaceAll("[^0-9]", "");
                if (!horasNumericas.isEmpty()) {
                    totalHorasGeradas += Double.parseDouble(horasNumericas);
                }
            } catch (NumberFormatException e) {
                Log.e("MeusCertificados", "Erro ao converter horas para número no certificado: " + certificado.getNomeCertificado(), e);
            }

            // Certificados gerados serão adicionados ao container específico de evento
            adicionarNovoCertificadoGerado(certificado);
        }

        // Formatar o total de horas para exibição
        String totalHorasGeradasFormatado = formatarHorasExibicao(String.valueOf(totalHorasGeradas));
        txtHoras.setText(totalHorasGeradasFormatado);

        // Salvar o total de horas geradas em SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("horas_geradas", totalHorasGeradasFormatado); // Salva o valor formatado (ex.: "5 horas e 30 minutos")
        editor.apply();
    }

    // Método para adicionar certificado gerado ao layout específico (por evento)
    private void adicionarNovoCertificadoGerado(Certificado certificado) {
        try {
            Log.d("MeusCertificados", "Adicionando certificado gerado: " + certificado.getNomeCertificado());

            // Infla o layout do certificado
            View certificadoView = getLayoutInflater().inflate(R.layout.certificado_layout, null);

            // Configura a imagem principal do certificado
            ImageView imgCertificado = certificadoView.findViewById(R.id.imgCertificado);
            imgCertificado.setImageResource(R.drawable.certificados_meuscertificados);

            // Configura o botão de informações
            ImageButton btnInfo = certificadoView.findViewById(R.id.btnInfo);
            btnInfo.setOnClickListener(v -> {
                Intent intent = new Intent(this, InfoCertificado.class);
                intent.putExtra("id_certificado", certificado.getIdCertificado());
                intent.putExtra("nome_certificado", certificado.getNomeCertificado());
                intent.putExtra("tipo_certificado", certificado.getTipoCertificado());
                intent.putExtra("data_emissao", certificado.getDataEmissao());
                intent.putExtra("horas_certificado", certificado.getHorasCertificado());
                startActivity(intent);
            });

            // Configura o botão de download (adicione sua lógica aqui)
            ImageButton btnDownload = certificadoView.findViewById(R.id.btnDownload);
            btnDownload.setOnClickListener(v -> {
                // Recupera o nome do usuário, nome do evento e as horas (substitua com os valores reais)
                String nomeUsuario = "Nome do Usuário";  // Você pode pegar o nome do usuário com SharedPreferences ou outra forma
                String nomeEvento = "Nome do Evento";  // Nome do evento que está sendo exibido
                String horasCertificado = certificado.getHorasCertificado();  // Quantidade de horas do certificado

                // Criando a mensagem do certificado
                String mensagemCertificado = String.format(
                        "Certificamos que %s, participou com êxito do evento %s, com carga horária total de %s horas.",
                        nomeUsuario, nomeEvento, horasCertificado);

                // Exibindo o pop-up
                showCertificadoPopup(mensagemCertificado);
            });

            // Adiciona o layout do certificado ao container de eventos
            linearLayoutCertificadosEventos.addView(certificadoView);
        } catch (Exception e) {
            Log.e("MeusCertificados", "Erro ao adicionar certificado gerado", e);
        }
    }

    // Método para exibir o pop-up
    private void showCertificadoPopup(String mensagem) {
        // Criação do LayoutInflater e View do Pop-up
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.popup_certificado, null);

        // Definir o texto do certificado
        TextView txtCertificado = popupView.findViewById(R.id.txtCertificado);
        txtCertificado.setText(mensagem);

        // Criar um Dialog para exibir o Pop-up
        Dialog popupDialog = new Dialog(this);
        popupDialog.setContentView(popupView);
        popupDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); // Tamanho do pop-up
        popupDialog.getWindow().setGravity(Gravity.CENTER); // Posicionamento do pop-up
        popupDialog.show(); // Exibe o pop-up

        // Se quiser adicionar um botão de fechar ou algo mais, pode fazer aqui
        popupView.findViewById(R.id.btnFecharPopup).setOnClickListener(v -> popupDialog.dismiss());
    }

    // Método para carregar todos os certificados (gerais)
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

        // Adiciona certificados ao layout principal (certificados gerais)
        for (Certificado certificado : certificados) {
            Log.d("MeusCertificados", "Certificado recuperado: " + certificado.getNomeCertificado());

            String horasStr = certificado.getHorasCertificado();
            if (horasStr == null || horasStr.isEmpty()) {
                Log.e("MeusCertificados", "Horas inválidas para certificado: " + certificado.getNomeCertificado());
                continue; // Pula certificados com valores inválidos
            }

            try {
                // Converte string para número decimal e soma
                double horasDecimal = parseHorasParaDecimal(horasStr);  // Método para tratar horas em formato "5 horas" ou "5h"
                totalHoras += horasDecimal;
            } catch (NumberFormatException e) {
                Log.e("MeusCertificados", "Erro ao converter horas para número no certificado: " + certificado.getNomeCertificado(), e);
            }

            // Certificados inseridos manualmente (não relacionados a eventos) serão adicionados ao container geral
            adicionarNovoCertificado(certificado);
        }

        // Formatar o total de horas para exibição
        String totalHorasFormatado = formatarHorasExibicao(totalHoras);
        txtHoras.setText(totalHorasFormatado);

        // Salvar o total de horas em SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("horas", totalHorasFormatado); // Salva o valor formatado (ex.: "5 horas e 30 minutos")
        editor.apply();
    }

    // Método para tratar a conversão de horas no formato "5 horas" ou "5h"
    private double parseHorasParaDecimal(String horasStr) {
        horasStr = horasStr.trim().toLowerCase();

        // Remove a palavra "horas" ou "h" no final da string, caso exista
        if (horasStr.endsWith("horas") || horasStr.endsWith("h")) {
            horasStr = horasStr.replace("horas", "").replace("h", "").trim();
        }

        // Verifica se o valor restante é um número
        try {
            return Double.parseDouble(horasStr);
        } catch (NumberFormatException e) {
            Log.e("MeusCertificados", "Erro ao converter horas para número: " + horasStr, e);
            return 0.0;  // Retorna 0 caso não consiga fazer a conversão
        }
    }

    // Método para formatar horas decimais para exibição (ex: "5 horas e 30 minutos")
    private String formatarHorasExibicao(double totalHoras) {
        int horas = (int) totalHoras;
        int minutos = (int) Math.round((totalHoras - horas) * 60);

        if (minutos == 0) {
            return horas + " horas";
        } else {
            return horas + " horas e " + minutos + " minutos";
        }
    }


    // Método para adicionar certificado ao layout principal (certificados gerais)
    private void adicionarNovoCertificado(Certificado certificado) {
        try {
            Log.d("MeusCertificados", "Adicionando certificado: " + certificado.getNomeCertificado());

            // Infla o layout do certificado
            View certificadoView = getLayoutInflater().inflate(R.layout.certificado_layout, null);

            // Configura a imagem principal do certificado
            ImageView imgCertificado = certificadoView.findViewById(R.id.imgCertificado);
            imgCertificado.setImageResource(R.drawable.certificados_meuscertificados);

            // Configura o botão de informações
            ImageButton btnInfo = certificadoView.findViewById(R.id.btnInfo);
            btnInfo.setOnClickListener(v -> {
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

            // Configura o botão de download (adicione sua lógica aqui)
            ImageButton btnDownload = certificadoView.findViewById(R.id.btnDownload);
            btnDownload.setOnClickListener(v -> {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

                String nomeUsuario = prefs.getString("nome", "Nome não disponível");
                String nomeEvento = certificado.getNomeCertificado(); // Nome do evento que está sendo exibido
                String horasCertificado = certificado.getHorasCertificado();  // Quantidade de horas do certificado

                // Criando a mensagem do certificado
                String mensagemCertificado = String.format(
                        "Certificamos que %s, participou com êxito do evento %s, com carga horária total de %s.",
                        nomeUsuario, nomeEvento, horasCertificado);

                // Exibindo o pop-up
                showCertificadoPopup(mensagemCertificado);
            });

            // Adiciona o layout do certificado ao container principal
            linearLayoutCertificados.addView(certificadoView);
        } catch (Exception e) {
            Log.e("MeusCertificados", "Erro ao adicionar certificado", e);
        }
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
