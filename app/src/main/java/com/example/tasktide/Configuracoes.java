package com.example.tasktide;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Usuario;

public class Configuracoes extends AppCompatActivity {

    private DAO dao;
    private EditText editTextNome, editTextEmail, editTextSenha;
    private Spinner spnCargo;
    private ImageView imgPerfil;
    private static final int PICK_IMAGE = 1;

    private String nomeAnterior, emailAnterior, senhaAnterior, cargoAnterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        dao = new DAO(this);
        editTextNome = findViewById(R.id.editTextNomeConfiguracoes);
        editTextEmail = findViewById(R.id.editTextEmailConfiguracoes);
        editTextSenha = findViewById(R.id.editTextSenhaConfiguracoes);
        spnCargo = findViewById(R.id.spnCargoConfiguracoes);
        imgPerfil = findViewById(R.id.imgPerfil);

        configurarSpinner();
        carregarDadosUsuario();
    }

    private void configurarSpinner() {
        String[] cargos = {"Discente - Ensino Médio ", "Discente - Ensino Superior", "Docente", "Administrador"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cargos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCargo.setAdapter(adapter);
    }

    public void alterarFotoPerfil(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Selecione uma foto"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Glide.with(this).load(imageUri).into(imgPerfil);
            salvarImagemNoBanco(imageUri.toString());
        }
    }

    private void salvarImagemNoBanco(String imageUri) {
        SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
        editor.putString("fotoPerfil", imageUri);
        editor.apply();
        Toast.makeText(this, "Foto de perfil alterada com sucesso!", Toast.LENGTH_SHORT).show();
    }

    private void carregarDadosUsuario() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        nomeAnterior = prefs.getString("nome", null);
        emailAnterior = prefs.getString("email", null);
        senhaAnterior = prefs.getString("senha", null);
        cargoAnterior = prefs.getString("cargo", null);
        String fotoPerfilUri = prefs.getString("fotoPerfil", null);

        if (nomeAnterior != null) editTextNome.setText(nomeAnterior);
        if (emailAnterior != null) editTextEmail.setText(emailAnterior);
        if (senhaAnterior != null) editTextSenha.setText(senhaAnterior);
        if (cargoAnterior != null) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnCargo.getAdapter();
            int position = adapter.getPosition(cargoAnterior);
            spnCargo.setSelection(position);
        }

        if (fotoPerfilUri != null) {
            Glide.with(this).load(Uri.parse(fotoPerfilUri)).into(imgPerfil);
        } else {
            imgPerfil.setImageResource(R.drawable.usuario_perfil);
        }
    }

    public void salvarAlteracoes(View view) {
        String nome = editTextNome.getText().toString();
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();
        String cargo = spnCargo.getSelectedItem().toString();

        if (!nome.equals(nomeAnterior) || !email.equals(emailAnterior) || !senha.equals(senhaAnterior) || !cargo.equals(cargoAnterior)) {
            SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
            editor.putString("nome", nome);
            editor.putString("email", email);
            editor.putString("senha", senha);
            editor.putString("cargo", cargo);
            editor.apply();

            Toast.makeText(this, "Informações salvas com sucesso!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, MinhaConta.class));
            finish();
        } else {
            Toast.makeText(this, "Nenhuma alteração a ser salva!", Toast.LENGTH_SHORT).show();
        }
    }

    public void confirmacaoExcluir(View view) {
        AlertDialog.Builder confirmaExclusao = new AlertDialog.Builder(Configuracoes.this);
        confirmaExclusao.setTitle("Atenção!");
        confirmaExclusao.setMessage("Tem certeza que deseja excluir sua conta?");
        confirmaExclusao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                excluirConta();
            }
        });
        confirmaExclusao.setNegativeButton("Não", null);
        confirmaExclusao.create().show();
    }

    private void excluirConta() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = prefs.getString("email", null);

        if (email != null) {
            Usuario usuarioLogado = dao.buscarUsuarioPorEmail(email);

            if (usuarioLogado != null) {
                try {
                    boolean resultado = dao.deletarUsuario(usuarioLogado);
                    if (resultado) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear();
                        editor.apply();
                        Toast.makeText(this, "Conta excluída com sucesso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, Login.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Falha ao excluir conta", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Erro ao excluir conta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
