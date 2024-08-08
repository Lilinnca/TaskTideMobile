package com.example.tasktide.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tasktide.Objetos.Evento;
import com.example.tasktide.Objetos.Identidade;
import com.example.tasktide.Objetos.Informacoes;
import com.example.tasktide.Objetos.Participantes;
import com.example.tasktide.Objetos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class DAO extends SQLiteOpenHelper {

    private static final String TAG = "DAO";

    private static final String NOME_BANCO = "tasktide_db";
    private static final int VERSAO_BANCO = 4; // Atualize a versão do banco conforme necessário

    // Tabelas para eventos
    private static final String TABELA_EVENTO = "evento";
    private static final String TABELA_INFORMACOES = "informacoes";
    private static final String TABELA_IDENTIDADE = "identidade";
    private static final String TABELA_PARTICIPANTES = "participantes";

    // Tabela para usuários
    private static final String TABELA_USUARIOS = "usuarios";

    public DAO(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação das tabelas
        String sqlEvento = "CREATE TABLE " + TABELA_EVENTO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nomeEvento TEXT," +
                "tipoEvento TEXT," +
                "horasComplementares TEXT," +
                "modalidade TEXT)";
        db.execSQL(sqlEvento);
        Log.i(TAG, "Tabela evento criada com sucesso. Local: " + db.getPath());

        String sqlInformacoes = "CREATE TABLE " + TABELA_INFORMACOES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_evento INTEGER," +
                "dataPrevis TEXT," +
                "horarioInicio TEXT," +
                "horarioTermino TEXT," +
                "local TEXT," +
                "FOREIGN KEY (id_evento) REFERENCES " + TABELA_EVENTO + "(id))";
        db.execSQL(sqlInformacoes);
        Log.i(TAG, "Tabela informacoes criada com sucesso.");

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

        String sqlParticipantes = "CREATE TABLE " + TABELA_PARTICIPANTES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_evento INTEGER," +
                "quantParticipantes TEXT," +
                "FOREIGN KEY (id_evento) REFERENCES " + TABELA_EVENTO + "(id))";
        db.execSQL(sqlParticipantes);
        Log.i(TAG, "Tabela participantes criada com sucesso.");

        String sqlUsuarios = "CREATE TABLE " + TABELA_USUARIOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "email TEXT," +
                "senha TEXT," +
                "cargo TEXT)";
        db.execSQL(sqlUsuarios);
        Log.i(TAG, "Tabela de usuários criada com sucesso.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualização do banco de dados
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_PARTICIPANTES);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_INFORMACOES);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_IDENTIDADE);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_EVENTO);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_USUARIOS);
            onCreate(db);
            Log.i(TAG, "Tabelas atualizadas para a nova versão do banco de dados.");
        }
    }

    // Métodos para eventos
    public long inserirEvento(Evento evento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nomeEvento", evento.getNomeEvento());
        values.put("tipoEvento", evento.getTipoEvento());
        values.put("horasComplementares", evento.getHorasComplementares());
        values.put("modalidade", evento.getModalidade());

        long id = db.insert(TABELA_EVENTO, null, values);
        db.close();
        if (id != -1) {
            Log.i(TAG, "Evento inserido com sucesso. ID: " + id);
        } else {
            Log.e(TAG, "Erro ao inserir evento.");
        }
        return id;
    }

    public long inserirIdentidade(Identidade identidade, long id_evento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_evento", id_evento);
        values.put("nome", identidade.getNome());
        values.put("cargo", identidade.getCargo());
        values.put("departamento", identidade.getDepartamento());
        values.put("contato", identidade.getContato());

        long id = db.insert(TABELA_IDENTIDADE, null, values);
        db.close();
        if (id != -1) {
            Log.i(TAG, "Identidade inserida com sucesso. ID: " + id);
        } else {
            Log.e(TAG, "Erro ao inserir identidade.");
        }
        return id;
    }

    public long inserirInformacoes(Informacoes informacoes, long id_evento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_evento", id_evento);
        values.put("dataPrevis", informacoes.getDataPrevis());
        values.put("horarioInicio", informacoes.getHorarioInicio());
        values.put("horarioTermino", informacoes.getHorarioTermino());
        values.put("local", informacoes.getLocal());

        long id = db.insert(TABELA_INFORMACOES, null, values);
        db.close();
        if (id != -1) {
            Log.i(TAG, "Informações inseridas com sucesso. ID: " + id);
        } else {
            Log.e(TAG, "Erro ao inserir informações.");
        }
        return id;
    }

    public long inserirParticipantes(Participantes participantes, long id_evento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantParticipantes", participantes.getQuantParticipantes());
        values.put("id_evento", id_evento);  // Associar ao evento específico

        long id = db.insert(TABELA_PARTICIPANTES, null, values);
        db.close();
        if (id != -1) {
            Log.i(TAG, "Participantes inseridos com sucesso. ID: " + id);
        } else {
            Log.e(TAG, "Erro ao inserir participantes.");
        }
        return id;
    }

    public Evento getEventoById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Evento evento = null;
        Cursor cursor = db.query(TABELA_EVENTO,
                new String[]{"nomeEvento", "tipoEvento", "horasComplementares"},
                "id = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            evento = new Evento();
            evento.setNomeEvento(cursor.getString(cursor.getColumnIndexOrThrow("nomeEvento")));
            evento.setTipoEvento(cursor.getString(cursor.getColumnIndexOrThrow("tipoEvento")));
            evento.setHorasComplementares(cursor.getString(cursor.getColumnIndexOrThrow("horasComplementares")));
            cursor.close();
        }
        db.close();
        return evento;
    }

    public void LimparTabelas() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABELA_PARTICIPANTES, null, null);
        db.delete(TABELA_INFORMACOES, null, null);
        db.delete(TABELA_IDENTIDADE, null, null);
        db.delete(TABELA_EVENTO, null, null);
        db.delete(TABELA_USUARIOS, null, null);
        Log.i(TAG, "Todas as tabelas foram limpas.");
        db.close();
    }

    public List<Evento> getAllEventos() {
        List<Evento> eventos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA_EVENTO, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Evento evento = new Evento();
                evento.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                evento.setNomeEvento(cursor.getString(cursor.getColumnIndexOrThrow("nomeEvento")));
                evento.setTipoEvento(cursor.getString(cursor.getColumnIndexOrThrow("tipoEvento")));
                evento.setHorasComplementares(cursor.getString(cursor.getColumnIndexOrThrow("horasComplementares")));
                evento.setModalidade(cursor.getString(cursor.getColumnIndexOrThrow("modalidade")));
                eventos.add(evento);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return eventos;
    }

    public void deleteEvento(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABELA_EVENTO, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Métodos para usuários
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

    public Usuario buscarUsuarioPorEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Usuario usuario = null;

        Cursor cursor = db.query(TABELA_USUARIOS, new String[]{"id", "nome", "email", "senha", "cargo"},
                "email=?", new String[]{email}, null, null, null, null);

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
}
