package com.example.tasktide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Obtém as preferências compartilhadas (SharedPreferences)
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String email = sharedPreferences.getString("email", "");  // Busca o valor do email

                Intent intent;
                if (email.isEmpty()) {
                    // Se não houver email salvo, redireciona para a tela de login
                    intent = new Intent(SplashActivity.this, Login.class);
                } else {
                    // Se houver email salvo, redireciona para a tela inicial
                    intent = new Intent(SplashActivity.this, TelaInicial.class);
                }

                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
