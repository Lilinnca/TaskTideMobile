package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Usuario;

public class Login extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextSenha;
    DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmailLogin);
        editTextSenha = findViewById(R.id.editTextSenhaLogin);

        dao = new DAO(this);
    }

    public void entrarLogin(View view) {
        try {
            String email = editTextEmail.getText().toString().trim();
            String senha = editTextSenha.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(Login.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Usuario usuario = dao.buscarUsuarioPorEmail(email);

            if (usuario != null && usuario.getSenha().equals(senha)) {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("email", usuario.getEmail());
                editor.putString("nome", usuario.getNome());
                editor.putString("senha", usuario.getSenha());
                editor.putString("cargo", usuario.getCargo());
                editor.apply();

                Toast.makeText(Login.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, TelaInicial.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Usuário não encontrado ou senha incorreta", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void esqueciSenha(View view){
        Intent in = new Intent(this, EsqueciMinhaSenha.class);
        startActivity(in);
    }

    public void voltarLogin(View view) {
        Intent in = new Intent(this, TelaBoasVindas.class);
        startActivity(in);
    }
}
