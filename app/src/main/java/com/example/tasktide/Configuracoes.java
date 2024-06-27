package com.example.tasktide;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Usuario;

public class Configuracoes extends AppCompatActivity {

    private DAO dao;
    private EditText nomeEditText;
    private EditText emailEditText;
    private EditText senhaEditText;
    private EditText cargoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        // Inicialização do DAO para acesso ao banco de dados
        dao = new DAO(this);

        // Vinculação dos elementos de interface com os EditTexts
        nomeEditText = findViewById(R.id.editTextText);
        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        senhaEditText = findViewById(R.id.editTextTextPassword);
        cargoEditText = findViewById(R.id.editTextTextMultiLine);

        // Carrega os dados do usuário logado nos EditTexts
        carregarDadosUsuario();
    }

    // Método para carregar os dados do usuário logado nos campos EditTexts
    private void carregarDadosUsuario() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = prefs.getString("email", null);
        String nome = prefs.getString("nome", null);
        String senha = prefs.getString("senha", null);
        String cargo = prefs.getString("cargo", null);

        if (email != null) {
            emailEditText.setText(email);
        }
        if (nome != null) {
            nomeEditText.setText(nome);
        }
        if (senha != null) {
            senhaEditText.setText(senha);
        }
        if (cargo != null) {
            cargoEditText.setText(cargo);
        }
    }

    // Método para abrir a tela MinhaConta
    public void minhacontac(View view) {
        Intent in = new Intent(Configuracoes.this, MinhaConta.class);
        startActivity(in);
    }

    // Método para exibir um diálogo de confirmação de exclusão da conta
    public void confirmacao(View view) {
        AlertDialog.Builder confirmaExclusao = new AlertDialog.Builder(Configuracoes.this);
        confirmaExclusao.setTitle("Atenção !");
        confirmaExclusao.setMessage("Tem certeza que deseja excluir sua conta?");
        confirmaExclusao.setCancelable(false);
        confirmaExclusao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmarExclusaoConta();
            }
        });
        confirmaExclusao.setNegativeButton("Não", null);
        confirmaExclusao.create().show();
    }

    // Método para confirmar a exclusão da conta do usuário
    private void confirmarExclusaoConta() {
        // Obtém o usuário logado atualmente
        Usuario usuarioLogado = getUsuarioLogado();

        if (usuarioLogado != null) {
            // Se o usuário estiver logado, tenta deletar a conta
            boolean sucesso = dao.deletarUsuario(usuarioLogado);

            if (sucesso) {
                // Se a exclusão for bem-sucedida, exibe uma mensagem de sucesso
                Toast.makeText(getApplicationContext(), "Conta excluída com sucesso!", Toast.LENGTH_SHORT).show();

                // Redireciona para a tela inicial após a exclusão da conta
                Intent intent = new Intent(Configuracoes.this, TelaInicial.class);
                startActivity(intent);
                finish(); // Finaliza a atividade atual para evitar retorno à tela de configurações
            } else {
                // Se houver falha na exclusão, exibe uma mensagem de erro
                Toast.makeText(getApplicationContext(), "Erro ao excluir conta", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Se não houver usuário logado, exibe uma mensagem informando o problema
            Toast.makeText(getApplicationContext(), "Usuário não está logado", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para obter o usuário logado atualmente
    private Usuario getUsuarioLogado() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = prefs.getString("email", null);

        if (email != null) {
            // Se o email estiver presente em SharedPreferences, busca o usuário correspondente no banco de dados
            return dao.buscarUsuarioPorEmail(email);
        }
        return null; // Retorna null se não houver email armazenado, o que indica que o usuário não está logado
    }
}
