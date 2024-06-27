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

    EditText editTextTextEmailAddressLogin;
    EditText editTextTextPasswordLogin;

    DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Vincula os EditTexts do layout com as variáveis locais
        editTextTextEmailAddressLogin = findViewById(R.id.editTextTextEmailAddressLogin);
        editTextTextPasswordLogin = findViewById(R.id.editTextTextPasswordLogin);

        // Inicializa o DAO para acesso ao banco de dados
        dao = new DAO(this);
    }

    // Método chamado ao clicar no botão "Boas Vindas"
    public void telaboasvindas(View view) {
        Intent in = new Intent(Login.this, TelaBoasVindas.class);
        startActivity(in);
    }

    // Método chamado ao clicar no botão "Login"
    public void telainicial(View view) {
        String email = editTextTextEmailAddressLogin.getText().toString().trim();
        String senha = editTextTextPasswordLogin.getText().toString().trim();

        // Valida se os campos de email e senha estão preenchidos
        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(Login.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Busca o usuário no banco de dados pelo email fornecido
        Usuario usuario = dao.buscarUsuarioPorEmail(email);

        // Verifica se o usuário existe e se a senha está correta
        if (usuario != null && usuario.getSenha().equals(senha)) {
            // Armazena informações do usuário logado no SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", usuario.getEmail());
            editor.putString("nome", usuario.getNome());
            editor.putString("senha", usuario.getSenha());
            editor.putString("cargo", usuario.getCargo());
            editor.apply();

            // Exibe mensagem de login bem-sucedido e redireciona para a tela inicial
            Toast.makeText(Login.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, TelaInicial.class);
            startActivity(intent);
            finish(); // Finaliza a atividade para evitar retorno à tela de login
        } else {
            // Exibe mensagem de usuário não encontrado ou senha incorreta
            Toast.makeText(Login.this, "Usuário não encontrado ou senha incorreta", Toast.LENGTH_SHORT).show();
        }
    }
}
