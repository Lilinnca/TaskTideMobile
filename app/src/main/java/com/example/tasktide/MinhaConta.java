package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MinhaConta extends AppCompatActivity {

    private TextView txtNomeUsuario;
    private TextView txtHorasUsuario;
    private TextView txtCargoUsuario;
    private TextView txtEmailUsuario;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);

        txtNomeUsuario = findViewById(R.id.txtNomeUsuario);
        txtHorasUsuario = findViewById(R.id.txtHorasUsuario);
        txtCargoUsuario = findViewById(R.id.txtCargoUsuario);
        txtEmailUsuario = findViewById(R.id.txtEmailUsuario);
        imgView = findViewById(R.id.imgPerfilMinhaConta);  // ImageView para exibir a imagem de perfil

        carregarDadosUsuario();
    }

    private void carregarDadosUsuario() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        String nome = prefs.getString("nome", "Nome não disponível");  // Nome do usuário
        String horas = prefs.getString("horas", "0 horas contabilizadas");  // Horas cadastradas
        String cargo = prefs.getString("cargo", "Cargo não disponível");  // Cargo do usuário
        String email = prefs.getString("email", "Email não disponível");  // Email do usuário
        String uriString = prefs.getString("fotoPerfil", null);  // URI da imagem de perfil, se existir

        // Set user information on TextViews
        txtNomeUsuario.setText(nome);
        txtHorasUsuario.setText(horas);
        txtCargoUsuario.setText(cargo);
        txtEmailUsuario.setText(email);

        // Load profile image using Glide or set a default image if URI is null
        if (uriString != null) {
            Uri imageUri = Uri.parse(uriString);
            Glide.with(this).load(imageUri).into(imgView);
        } else {
            imgView.setImageResource(R.drawable.usuario_perfil); // Imagem padrão
        }
    }

    public void meuscertificados(View view) {
        Intent in = new Intent(this, MeusCertificados.class);
        startActivity(in);
    }

    public void configuracoes(View view) {
        Intent in = new Intent(this, Configuracoes.class);
        startActivity(in);
    }

    public void sobrenos(View view) {
        Intent in = new Intent(this, SobreNos.class);
        startActivity(in);
    }

    public void inicialMinhaConta(View view){
        Intent in = new Intent(this, TelaInicial.class);
        startActivity(in);
    }
    public void localizaoMinhaConta(View view){
        Intent in = new Intent(this, Localizacao.class);
        startActivity(in);
    }
    public void addEventoMinhaConta(View view){
        Intent in = new Intent(this, CriarEvento.class);
        startActivity(in);
    }
    public void meusEventosMinhaConta(View view){
        Intent in = new Intent(this, MeusEventosParticipante.class);
        startActivity(in);
    }
    public void perfilMinhaConta(View view){
        Intent in = new Intent(this, MinhaConta.class);
        startActivity(in);
    }

}
