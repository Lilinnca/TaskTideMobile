package com.example.tasktide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class EsqueciMinhaSenha extends AppCompatActivity {

    private EditText editTextEmailEsqueciSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_minha_senha);

        // Referencie o campo de entrada de e-mail
        editTextEmailEsqueciSenha = findViewById(R.id.editTextEmailEsqueciSenha);
    }

    // Método para o botão "Enviar E-mail de Recuperação"
    public void enviarEmailRecuperacao(View view) {
        String email = editTextEmailEsqueciSenha.getText().toString().trim();

        // Validação do campo de e-mail
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(EsqueciMinhaSenha.this, "Por favor, insira um e-mail", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void voltarEms(View view){
        Intent in = new Intent(this,Login.class);
        startActivity(in);
    }
}