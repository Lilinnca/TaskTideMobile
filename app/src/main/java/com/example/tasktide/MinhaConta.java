package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MinhaConta extends AppCompatActivity {

    private TextView txtNomeUsuario;
    private TextView txtHorasUsuario;
    private TextView txtTipoUsuario;
    private TextView txtEmailUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);

        txtNomeUsuario = findViewById(R.id.txtNomeUsuario);
        txtHorasUsuario = findViewById(R.id.txtHorasUsuario);
        txtTipoUsuario = findViewById(R.id.txtTipoUsuario);
        txtEmailUsuario = findViewById(R.id.txtEmailUsuario);

        carregarDadosUsuario();
    }

    private void carregarDadosUsuario() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String nome = prefs.getString("nome", "");
        String horas = "192 horas contabilizadas"; // Exemplo
        String tipo = "Discente - Ensino Médio"; // Exemplo
        String email = prefs.getString("email", "");

        txtNomeUsuario.setText(nome);
        txtHorasUsuario.setText(horas);
        txtTipoUsuario.setText(tipo);
        txtEmailUsuario.setText(email);
    }

    public void telaperfil(View view) {
        Intent in = new Intent(MinhaConta.this, MinhaConta.class);
        startActivity(in);
    }

    public void telameuseventos(View view) {
        Intent in = new Intent(MinhaConta.this, MeusEventos.class);
        startActivity(in);
    }

    public void telacriarevento(View view) {
        Intent in = new Intent(MinhaConta.this, CriarEvento.class);
        startActivity(in);
    }

    public void telafavoritos(View view) {
    }

    public void meuscertificados(View view){
     Intent in = new Intent(MinhaConta.this, MeusCertificados.class);
     startActivity(in);
    }

    public void telainicial(View view) {
        Intent in = new Intent(MinhaConta.this, TelaInicial.class);
        startActivity(in);
    }

    public void configuracoes(View view){
        Intent in = new Intent(MinhaConta.this, Configuracoes.class);
        startActivity(in);
    }

    public void sobrenos(View view){
        Intent in = new Intent(MinhaConta.this, SobreNos.class);
        startActivity(in);
    }
}
