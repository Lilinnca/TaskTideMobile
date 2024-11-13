package com.example.tasktide.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.tasktide.Objetos.Evento;
import com.example.tasktide.Objetos.Informacoes;
import com.example.tasktide.Objetos.Participantes;
import com.example.tasktide.Objetos.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DAO extends SQLiteOpenHelper {

    private static final String TAG = "DAO";
    private static final String NOME_BANCO = "tasktide_db";
    private static final int VERSAO_BANCO = 5;

    public static final String TABELA_EVENTO = "evento";
    public static final String TABELA_INFORMACOES = "informacoes";
    public static final String TABELA_PARTICIPANTES = "participantes";
    private static final String TABELA_IDENTIDADE = "identidade";
    private static final String TABELA_USUARIOS = "usuarios";
    private SQLiteOpenHelper dao;

    public DAO(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlEvento = "CREATE TABLE " + TABELA_EVENTO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nomeEvento TEXT," +
                "tipoEvento TEXT," +
                "horasComplementares TEXT," +
                "modalidade TEXT," +
                "banner BLOB)";
        db.execSQL(sqlEvento);
        Log.i(TAG, "Tabela evento criada com sucesso. Local: " + db.getPath());

        String sqlInformacoes = "CREATE TABLE " + TABELA_INFORMACOES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_evento INTEGER," +
                "dataPrevis TEXT," +
                "dataFim TEXT," +
                "horarioInicio TEXT," +
                "horarioTermino TEXT," +
                "local TEXT," +
                "prazo TEXT," +
                "valorEvento TEXT," +
                "Pago TEXT," +
                "FOREIGN KEY (id_evento) REFERENCES " + TABELA_EVENTO + "(id))";
        db.execSQL(sqlInformacoes);
        Log.i(TAG, "Tabela informacoes criada com sucesso.");

        // Tabela de Identidade
        String sqlIdentidade = "CREATE TABLE " + TABELA_IDENTIDADE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_evento INTEGER," +
                "nome TEXT," +
                "cargo TEXT," +
                "departamento TEXT," +
                "contato TEXT," +
                "FOREIGN KEY (id_evento) REFERENCES " + TABELA_EVENTO + "(id))";
        db.execSQL(sqlIdentidade);
        Log.i(TAG, "Tabela identidade criada com sucesso.");

        // Tabela de Participantes
        String sqlParticipantes = "CREATE TABLE " + TABELA_PARTICIPANTES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_evento INTEGER," +
                "quantParticipantes TEXT," +
                "FOREIGN KEY (id_evento) REFERENCES " + TABELA_EVENTO + "(id))";
        db.execSQL(sqlParticipantes);
        Log.i(TAG, "Tabela participantes criada com sucesso.");

        // Tabela de Usuários
        String sqlUsuarios = "CREATE TABLE " + TABELA_USUARIOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "email TEXT," +
                "senha TEXT," +
                "cargo TEXT)";
        db.execSQL(sqlUsuarios);
        Log.i(TAG, "Tabela de usuários criada com sucesso.");
    }

    public long inserirUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());
        values.put("cargo", usuario.getCargo());

        long id = db.insert(TABELA_USUARIOS, null, values);
        db.close();
        Log.i(TAG, "Usuário inserido com sucesso. ID: " + id);
        return id;
    }

    @SuppressLint("Range")
    public Usuario buscarUsuarioPorEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Usuario usuario = null;

        Cursor cursor = db.query(TABELA_USUARIOS, new String[]{"id", "nome", "email", "senha", "cargo"},
                "email=?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            usuario = new Usuario();
            usuario.setId(cursor.getInt(cursor.getColumnIndex("id")));
            usuario.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            usuario.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            usuario.setSenha(cursor.getString(cursor.getColumnIndex("senha")));
            usuario.setCargo(cursor.getString(cursor.getColumnIndex("cargo")));
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return usuario;
    }

    public boolean atualizarUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());
        values.put("cargo", usuario.getCargo());

        int linhasAfetadas = db.update(TABELA_USUARIOS, values, "id = ?", new String[]{String.valueOf(usuario.getId())});
        db.close();

        return linhasAfetadas > 0;
    }

    public boolean deletarUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        int linhasAfetadas = db.delete(TABELA_USUARIOS, "id = ?", new String[]{String.valueOf(usuario.getId())});
        db.close();

        return linhasAfetadas > 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_PARTICIPANTES);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_INFORMACOES);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_EVENTO);
            onCreate(db);
            Log.i(TAG, "Tabelas atualizadas para a nova versão do banco de dados.");
        }
    }

    public long inserirEvento(Evento evento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nomeEvento", evento.getNomeEvento());
        values.put("tipoEvento", evento.getTipoEvento());
        values.put("horasComplementares", evento.getHorasComplementares());
        values.put("modalidade", evento.getModalidade());

        if (evento.getBannerBitmap() != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            evento.getBannerBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] bannerBytes = outputStream.toByteArray();
            values.put("banner", bannerBytes);
        }

        long id = db.insert(TABELA_EVENTO, null, values);
        db.close();
        if (id != -1) {
            Log.i(TAG, "Evento inserido com sucesso. ID: " + id);
        } else {
            Log.e(TAG, "Erro ao inserir evento.");
        }
        return id;
    }

    public long inserirInformacoes(Informacoes informacoes, long idEvento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_evento", idEvento);
        values.put("dataPrevis", informacoes.getDataPrevis());
        values.put("dataFim", informacoes.getDataFim());
        values.put("horarioInicio", informacoes.getHorarioInicio());
        values.put("horarioTermino", informacoes.getHorarioFim());
        values.put("prazo", informacoes.getPrazo());
        values.put("local", informacoes.getLocal());
        values.put("valorEvento", informacoes.getValorEvento());
        values.put("Pago", informacoes.getPago());

        long id = db.insert(TABELA_INFORMACOES, null, values);

        if (id == -1) {
            Log.e(TAG, "Erro ao inserir informações no banco de dados.");
        } else {
            Log.i(TAG, "Informações inseridas com sucesso. ID: " + id);
        }

        db.close();
        return id;
    }

    public long inserirParticipantes(Participantes participantes, long id_evento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantParticipantes", participantes.getQuantParticipantes());
        values.put("id_evento", id_evento);

        long id = db.insert(TABELA_PARTICIPANTES, null, values);
        db.close();
        return id;
    }

    public void LimparTabelas() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABELA_EVENTO, null, null);
        db.delete(TABELA_INFORMACOES, null, null);
        db.delete(TABELA_PARTICIPANTES, null, null);
        db.close();
    }

    public Evento getEventoById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Evento evento = null;
        Cursor cursor = db.query(TABELA_EVENTO, new String[]{"id", "nomeEvento", "tipoEvento", "horasComplementares", "banner"},
                "id=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            evento = new Evento();
            evento.setId((int) cursor.getLong(0));
            evento.setNomeEvento(cursor.getString(1));
            evento.setTipoEvento(cursor.getString(2));
            evento.setHorasComplementares(cursor.getString(3));

            byte[] bannerBytes = cursor.getBlob(4);
            if (bannerBytes != null) {
                Bitmap bannerBitmap = BitmapFactory.decodeByteArray(bannerBytes, 0, bannerBytes.length);
                evento.setBannerBitmap(bannerBitmap);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return evento;
    }

    public List<Evento> getAllEventos() {
        List<Evento> listaEventos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABELA_EVENTO, null);

        if (cursor.moveToFirst()) {
            do {
                Evento evento = new Evento();
                evento.setId((int) cursor.getLong(0));
                evento.setNomeEvento(cursor.getString(1));
                evento.setTipoEvento(cursor.getString(2));
                evento.setHorasComplementares(cursor.getString(3));

                byte[] bannerBytes = cursor.getBlob(4);
                if (bannerBytes != null) {
                    Bitmap bannerBitmap = BitmapFactory.decodeByteArray(bannerBytes, 0, bannerBytes.length);
                    evento.setBannerBitmap(bannerBitmap);
                }

                listaEventos.add(evento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaEventos;
    }

    public boolean deleteEvento(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Deleta primeiro as informações relacionadas
        db.delete(TABELA_INFORMACOES, "id_evento = ?", new String[]{String.valueOf(id)});
        db.delete(TABELA_PARTICIPANTES, "id_evento = ?", new String[]{String.valueOf(id)});

        int linhasAfetadas = db.delete(TABELA_EVENTO, "id = ?", new String[]{String.valueOf(id)});
        db.close();

        return linhasAfetadas > 0;
    }
}
