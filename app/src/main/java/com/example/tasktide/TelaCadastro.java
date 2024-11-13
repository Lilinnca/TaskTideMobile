package com.example.tasktide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Usuario;

public class TelaCadastro extends AppCompatActivity {

    EditText editTextNomeCadastro;
    EditText editTextEmailCadastro;
    Spinner spinnerCargoCadastro;
    EditText editTextSenhaCadastro;
    Button btnCriarContaCadastro;

    DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        editTextNomeCadastro = findViewById(R.id.editTextNomeCadastro);
        editTextEmailCadastro = findViewById(R.id.editTextEmailCadastro);
        spinnerCargoCadastro = findViewById(R.id.spinnerCargoCadastro);
        editTextSenhaCadastro = findViewById(R.id.editTextSenhaCadastro);
        btnCriarContaCadastro = findViewById(R.id.btnCriarContaCadastro);

        dao = new DAO(this);

        btnCriarContaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCamposPreenchidos()) {

                    String emailNovoUsuario = editTextEmailCadastro.getText().toString();
                    Usuario usuarioExistente = dao.buscarUsuarioPorEmail(emailNovoUsuario);
                    if (usuarioExistente != null) {
                        Toast.makeText(getApplicationContext(), "Este email já está cadastrado!", Toast.LENGTH_SHORT).show();
                    } else {
                        Usuario novoUsuario = new Usuario();
                        novoUsuario.setNome(editTextNomeCadastro.getText().toString());
                        novoUsuario.setEmail(emailNovoUsuario);
                        novoUsuario.setSenha(editTextSenhaCadastro.getText().toString());
                        novoUsuario.setCargo(spinnerCargoCadastro.getSelectedItem().toString());

                        long id = dao.inserirUsuario(novoUsuario);

                        if (id != -1) {
                            Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(TelaCadastro.this, TelaBoasVindas.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private boolean isCamposPreenchidos() {
        if (editTextNomeCadastro.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Preencha o nome", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextEmailCadastro.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Preencha o e-mail", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextSenhaCadastro.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Preencha a senha", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spinnerCargoCadastro.getSelectedItem() == null || spinnerCargoCadastro.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Selecione um cargo", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void voltarCadastro(View view) {
        Intent in = new Intent(TelaCadastro.this, TelaPrimeirosPassos.class);
        startActivity(in);
    }
}
