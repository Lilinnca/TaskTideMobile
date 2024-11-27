package com.example.tasktide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Evento;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InfoEvento extends AppCompatActivity {

    private TextView tvQuantidade, txtNomeEventoInfo, txtLocalEventoInfo, txtDataEventoInfo, txtDescricaoEventoInfo;
    private int quantidade = 0; // Quantidade inicial de ingressos
    private DAO dao;
    private double valorEvento = 0.0; // Valor do evento

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_evento);

    }
}
