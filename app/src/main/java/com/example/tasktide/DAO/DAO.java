package com.example.tasktide.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tasktide.Objetos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class DAO extends SQLiteOpenHelper {

    private static final String TAG = "DAO";

    private static final String NOME_BANCO = "tasktide_db";
    private static final int VERSAO_BANCO = 1;

    public DAO(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação da tabela "usuarios" no banco de dados
        String sql = "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "email TEXT," +
                "senha TEXT," +
                "cargo TEXT)";
        db.execSQL(sql);
        Log.i(TAG, "Tabela de usuários criada com sucesso.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualização da tabela "usuarios" caso haja mudança na versão do banco de dados
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
        Log.i(TAG, "Tabela de usuários atualizada.");
    }

    public long inserirUsuario(Usuario usuario) {
        // Método para inserir um novo usuário no banco de dados
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());
        values.put("cargo", usuario.getCargo());

        long id = db.insert("usuarios", null, values);
        db.close();
        Log.i(TAG, "Usuário inserido com sucesso. ID: " + id);
        return id;
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        // Método para buscar um usuário pelo email no banco de dados
        SQLiteDatabase db = this.getReadableDatabase();
        Usuario usuario = null;

        Cursor cursor = db.query("usuarios", new String[]{"id", "nome", "email", "senha", "cargo"},
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
        // Método para atualizar os dados de um usuário no banco de dados
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());
        values.put("cargo", usuario.getCargo());

        int linhasAfetadas = db.update("usuarios", values, "id = ?", new String[]{String.valueOf(usuario.getId())});
        db.close();

        boolean sucesso = linhasAfetadas > 0;
        return sucesso;
    }

    public boolean deletarUsuario(Usuario usuario) {
        // Método para deletar um usuário do banco de dados
        SQLiteDatabase db = this.getWritableDatabase();
        int linhasAfetadas = db.delete("usuarios", "id = ?", new String[]{String.valueOf(usuario.getId())});
        db.close();

        boolean sucesso = linhasAfetadas > 0;
        Log.i(TAG, "Exclusão de usuário bem-sucedida: " + sucesso);
        return sucesso;
    }
}
