package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Usuario;

import java.util.List;

public class TelaCadastro extends AppCompatActivity {

    EditText editTextTextNomeCadastro;
    EditText editTextTextEmailAddressEmailCadastro;
    EditText editTextTextMultiLineCargoCadastro;
    EditText editTextTextPasswordSenhaCadastro;
    Button btnCriarContaCadastro;

    DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        // Inicialização das views
        editTextTextNomeCadastro = findViewById(R.id.editTextTextNomeCadastro);
        editTextTextEmailAddressEmailCadastro = findViewById(R.id.editTextTextEmailAddressEmailCadastro);
        editTextTextMultiLineCargoCadastro = findViewById(R.id.editTextTextMultiLineCargoCadastro);
        editTextTextPasswordSenhaCadastro = findViewById(R.id.editTextTextPasswordSenhaCadastro);
        btnCriarContaCadastro = findViewById(R.id.btnCriarContaCadastro);

        // Inicialização do DAO
        dao = new DAO(this);

        // Configuração do listener do botão
        btnCriarContaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verifica se todos os campos estão preenchidos
                if (editTextTextNomeCadastro.getText().toString().isEmpty() ||
                        editTextTextEmailAddressEmailCadastro.getText().toString().isEmpty() ||
                        editTextTextPasswordSenhaCadastro.getText().toString().isEmpty() ||
                        editTextTextMultiLineCargoCadastro.getText().toString().isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verifica se o email já está cadastrado
                String emailNovoUsuario = editTextTextEmailAddressEmailCadastro.getText().toString();
                Usuario usuarioExistente = dao.buscarUsuarioPorEmail(emailNovoUsuario);
                if (usuarioExistente != null) {
                    Toast.makeText(getApplicationContext(), "Este email já está cadastrado!", Toast.LENGTH_SHORT).show();
                } else {
                    // Cria um novo usuário com os dados inseridos
                    Usuario novoUsuario = new Usuario();
                    novoUsuario.setNome(editTextTextNomeCadastro.getText().toString());
                    novoUsuario.setEmail(emailNovoUsuario);
                    novoUsuario.setSenha(editTextTextPasswordSenhaCadastro.getText().toString());
                    novoUsuario.setCargo(editTextTextMultiLineCargoCadastro.getText().toString());

                    // Insere o novo usuário no banco de dados
                    long id = dao.inserirUsuario(novoUsuario);

                    // Feedback para o usuário
                    if (id != -1) {
                        Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                        // Redireciona para a tela de boas-vindas
                        Intent intent = new Intent(TelaCadastro.this, TelaBoasVindas.class);
                        startActivity(intent);
                        finish(); // Finaliza a activity atual para evitar o retorno incorreto

                    } else {
                        Toast.makeText(getApplicationContext(), "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Método para redirecionamento para a tela de primeiros passos
    public void telaprimeirospassos(View view) {
        Intent in = new Intent(TelaCadastro.this, TelaPrimeirosPassos.class);
        startActivity(in);
    }

    // Método para redirecionamento para a tela de boas-vindas
    public void telaboasvindas(View view) {
        Intent in = new Intent(TelaCadastro.this, TelaBoasVindas.class);
        startActivity(in);
    }
}
