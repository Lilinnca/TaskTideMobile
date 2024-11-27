package com.example.tasktide;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Evento;
import com.example.tasktide.Objetos.Usuario;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EventoInfoTelaInicial extends AppCompatActivity {


    private TextView txtNomeEventoInfo, txtLocalEventoInfo, txtDataEventoInfo, txtDescricaoEventoInfo;
    private DAO dao;
    private long usuarioId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_info_tela_inicial);

}}