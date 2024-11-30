package com.example.tasktide.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

import com.example.tasktide.Objetos.Atividade;
import com.example.tasktide.Objetos.Certificado;
import com.example.tasktide.Objetos.Evento;
import com.example.tasktide.Objetos.Informacoes;
import com.example.tasktide.Objetos.Participantes;
import com.example.tasktide.Objetos.Usuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DAO extends SQLiteOpenHelper {

    private static final String TAG = "DAO";
    private static final String NOME_BANCO = "tasktide_db";
    private static final int VERSAO_BANCO = 17;
    public static final String TABELA_USUARIOS = "usuarios";
    public static final String TABELA_EVENTO = "evento";
    public static final String TABELA_INFORMACOES = "informacoes";
    public static final String TABELA_PARTICIPANTES = "participantes";
    private static final String TABELA_CERTIFICADOS = "Certificados";
    public static final String TABELA_CRONOGRAMA = "cronograma";
    private Context context;

    public DAO(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableEvento = "CREATE TABLE IF NOT EXISTS " + TABELA_EVENTO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome_evento TEXT, " +
                "tipo_evento TEXT, " +
                "horas_complementares TEXT, " +
                "modalidade TEXT, " +
                "categoria TEXT, " +
                "descricao TEXT, " +
                "banner_imagem BLOB)";
        db.execSQL(createTableEvento);
        Log.i(TAG, "Tabela de eventos criada com sucesso.");

        createUsuarioTable(db);
        createCronogramaTable(db);
        createInformacoesTable(db);
        createParticipantesTable(db);
        createInscricaoTable(db);
        createCertificadoTable(db);
        createTablePdf(db);

        Log.i("DAO", "Banco de dados criado com sucesso.");
    }

    private void createTablePdf(SQLiteDatabase db) {
        String createTablePdf = "CREATE TABLE IF NOT EXISTS tabela_pdfs (" +
                "id_pdf INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome_pdf TEXT);";
        db.execSQL(createTablePdf);
        Log.i("DAO", "Tabela de PDF criada com sucesso.");
    }

    private void createCertificadoTable(SQLiteDatabase db) {
        String createTableCertificado = "CREATE TABLE IF NOT EXISTS " + TABELA_CERTIFICADOS + " (" +
                "id_certificado INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_usuario INTEGER, " +
                "id_evento INTEGER, " +
                "nome_certificado TEXT, " +
                "tipo_certificado TEXT, " +
                "data_emissao TEXT, " +
                "horas_certificado TEXT, " +
                "FOREIGN KEY (id_usuario) REFERENCES Usuarios(id)" +
                ");";
        db.execSQL(createTableCertificado);
        Log.i("DAO", "Tabela de certificados criada com sucesso.");
    }

    private void createUsuarioTable(SQLiteDatabase db) {
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS " + TABELA_USUARIOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "email TEXT, " +
                "senha TEXT, " +
                "cargo TEXT)";
        db.execSQL(sqlUsuarios);
        Log.i(TAG, "Tabela de usuários criada com sucesso.");
    }

    private void createCronogramaTable(SQLiteDatabase db) {
        String createCronogramaTable = "CREATE TABLE IF NOT EXISTS " + TABELA_CRONOGRAMA + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id_evento INTEGER NOT NULL, "
                + "nomeAtividade TEXT, "
                + "horario TEXT, "
                + "palestrante TEXT, "
                + "localAtividade TEXT, "
                + "data TEXT, "
                + "FOREIGN KEY (id_evento) REFERENCES evento(id) ON DELETE CASCADE"
                + ")";
        db.execSQL(createCronogramaTable);
        Log.i(TAG, "Tabela cronograma criada com sucesso.");
    }

    private void createInformacoesTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABELA_INFORMACOES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_evento INTEGER," +
                "dataPrevista TEXT," +
                "dataFim TEXT," +
                "horarioInicio TEXT," +
                "horarioTermino TEXT," +
                "prazo TEXT," +
                "local TEXT," +
                "valorEvento DOUBLE," +
                "Pago INTEGER," +
                "FOREIGN KEY (id_evento) REFERENCES " + TABELA_EVENTO + "(id))";
        db.execSQL(sql);
        Log.i(TAG, "Tabela de informações criada com sucesso.");
    }

    private void createParticipantesTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABELA_PARTICIPANTES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_evento INTEGER," +
                "quantParticipantes TEXT," +
                "FOREIGN KEY (id_evento) REFERENCES " + TABELA_EVENTO + "(id))";
        db.execSQL(sql);
        Log.i(TAG, "Tabela de participantes criada com sucesso.");
    }

    private void createInscricaoTable(SQLiteDatabase db) {
        String createTableInscricoes = "CREATE TABLE IF NOT EXISTS Inscricoes (" +
                "id_inscricao INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_usuario INTEGER, " +
                "id_evento INTEGER, " +
                "data_inscricao TEXT, " +
                "FOREIGN KEY (id_usuario) REFERENCES " + TABELA_USUARIOS + "(id), " +
                "FOREIGN KEY (id_evento) REFERENCES " + TABELA_EVENTO + "(id)" +
                ");";
        db.execSQL(createTableInscricoes);
        Log.i(TAG, "Tabela de inscrição criada com sucesso.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_EVENTO);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_USUARIOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_INFORMACOES);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_PARTICIPANTES);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_CRONOGRAMA);
            db.execSQL("DROP TABLE IF EXISTS Inscricoes");
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_CERTIFICADOS);

            onCreate(db);
            Log.i(TAG, "Banco de dados atualizado com sucesso.");
        }
    }

    //Evento (Métodos)
    public long inserirEvento(Evento evento) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = -1;

        try {
            ContentValues values = new ContentValues();
            values.put("nome_evento", evento.getNomeEvento());
            values.put("tipo_evento", evento.getTipoEvento());
            values.put("horas_complementares", evento.getHorasComplementares());
            values.put("descricao", evento.getDescricao());
            values.put("categoria", evento.getCategoria());
            values.put("modalidade", evento.getModalidade());

            id = db.insert(TABELA_EVENTO, null, values);
            Log.i(TAG, "Evento inserido com ID: " + id);

            if (id != -1) {
                Certificado certificado = new Certificado();
                certificado.setNomeCertificado(evento.getNomeEvento());
                certificado.setDataEmissao(evento.getDataEvento());
                certificado.setHorasCertificado(evento.getHorasComplementares());
                certificado.setTipoCertificado(evento.getTipoEvento());
                certificado.setIdEvento((int) id);

                salvarCertificado(certificado);
                Log.i(TAG, "Certificado criado para o evento ID: " + id);
            }


        } catch (Exception e) {
            Log.e(TAG, "Erro ao inserir evento: " + e.getMessage());
        } finally {
            db.close();
        }

        return id;
    }

    public long inserirInformacoes(Informacoes informacoes, long idEvento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id_evento", idEvento);
        values.put("dataPrevista", informacoes.getDataPrevista());
        values.put("dataFim", informacoes.getDataFim());
        values.put("horarioInicio", informacoes.getHorarioInicio());
        values.put("horarioTermino", informacoes.getHorarioTermino());
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

    public long inserirParticipantes(Participantes participantes, long idEvento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantParticipantes", participantes.getQuantParticipantes());
        values.put("id_evento", idEvento);

        long id = db.insert(TABELA_PARTICIPANTES, null, values);
        db.close();
        if (id != -1) {
            Log.i(TAG, "Participantes inseridos com sucesso. ID: " + id);
        } else {
            Log.e(TAG, "Erro ao inserir participantes.");
        }
        return id;
    }

    public String[] buscarParticipantes(long eventoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT quantParticipantes FROM participantes WHERE id_evento = ?",
                new String[]{String.valueOf(eventoId)});

        List<String> participantes = new ArrayList<>();
        while (cursor.moveToNext()) {
            participantes.add(cursor.getString(0));
        }
        cursor.close();
        return participantes.toArray(new String[0]);
    }


    @SuppressLint("Range")
    public Informacoes getInformacoesById(long idEvento) {
        Informacoes informacoes = null;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM informacoes WHERE id_evento = ?", new String[]{String.valueOf(idEvento)});


        if (cursor != null && cursor.moveToFirst()) {

            informacoes = new Informacoes(
                    cursor.getLong(cursor.getColumnIndex("id")),
                    cursor.getLong(cursor.getColumnIndex("id_evento")),
                    cursor.getString(cursor.getColumnIndex("dataPrevista")),
                    cursor.getString(cursor.getColumnIndex("dataFim")),
                    cursor.getString(cursor.getColumnIndex("horarioInicio")),
                    cursor.getString(cursor.getColumnIndex("horarioTermino")),
                    cursor.getString(cursor.getColumnIndex("prazo")),
                    cursor.getString(cursor.getColumnIndex("local")),
                    cursor.getInt(cursor.getColumnIndex("Pago")),
                    cursor.getDouble(cursor.getColumnIndex("valorEvento"))
            );
        }

        if (cursor != null) {
            cursor.close();
        }

        return informacoes;
    }

    @SuppressLint("Range")
    public String[] buscarInformacoesPorEvento(long eventoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT dataPrevista, dataFim, local, prazo, valorEvento, horarioInicio, horarioTermino FROM informacoes WHERE id = ?",
                new String[]{String.valueOf(eventoId)});
        if (cursor.moveToFirst()) {
            String dataPrevista = cursor.getString(0);
            String dataFim = cursor.getString(1);
            String local = cursor.getString(2);
            String prazo = cursor.getString(3);
            String valorEvento = cursor.getString(4);
            String horarioInicio = cursor.getString(5);
            String horarioTermino = cursor.getString(6);
            cursor.close();
            return new String[]{dataPrevista, dataFim, local, prazo, valorEvento, horarioInicio, horarioTermino};
        }
        cursor.close();
        return new String[]{"", "", "", "", "", "", ""};
    }

    public Evento buscarEventoPorId(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Evento evento = null;
        Cursor cursorEvento = null;

        try {
            String queryEvento = "SELECT * FROM " + TABELA_EVENTO + " WHERE id = ?";
            cursorEvento = db.rawQuery(queryEvento, new String[]{String.valueOf(id)});

            if (cursorEvento != null && cursorEvento.moveToFirst()) {
                evento = new Evento();

                evento.setId(cursorEvento.getLong(cursorEvento.getColumnIndexOrThrow("id")));
                evento.setNomeEvento(cursorEvento.getString(cursorEvento.getColumnIndexOrThrow("nome_evento")));
                evento.setTipoEvento(cursorEvento.getString(cursorEvento.getColumnIndexOrThrow("tipo_evento")));
                evento.setHorasComplementares(cursorEvento.getString(cursorEvento.getColumnIndexOrThrow("horas_complementares")));
                evento.setModalidade(cursorEvento.getString(cursorEvento.getColumnIndexOrThrow("modalidade")));
                evento.setCategoria(cursorEvento.getString(cursorEvento.getColumnIndexOrThrow("categoria")));
                evento.setDescricao(cursorEvento.getString(cursorEvento.getColumnIndexOrThrow("descricao")));

                byte[] bannerImagemBlob = cursorEvento.getBlob(cursorEvento.getColumnIndexOrThrow("banner_imagem"));
                if (bannerImagemBlob != null) {
                    String bannerImagemBase64 = Base64.encodeToString(bannerImagemBlob, Base64.DEFAULT);
                    evento.setBannerImagem(bannerImagemBase64);
                } else {
                    evento.setBannerImagem(null);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar evento com ID: " + id, e);
        } finally {
            if (cursorEvento != null) {
                cursorEvento.close();
            }
        }

        return evento;
    }

    @SuppressLint("Range")
    public List<Evento> getAllEventos() {
        List<Evento> eventos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Log.i(TAG, "Verificando existência da tabela 'evento'...");
        if (isTableExists(db, TABELA_EVENTO)) {
            Log.i(TAG, "Tabela 'evento' existe. Realizando consulta.");
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABELA_EVENTO, null);

            if (cursor.moveToFirst()) {
                do {
                    Evento evento = new Evento();
                    evento.setId(cursor.getLong(cursor.getColumnIndex("id")));
                    evento.setNomeEvento(cursor.getString(cursor.getColumnIndex("nome_evento")));
                    eventos.add(evento);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            Log.e(TAG, "A tabela 'evento' não foi encontrada.");
        }
        db.close();
        return eventos;
    }

    public void deleteEvento(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("evento", "id = ?", new String[]{String.valueOf(id)});
        if (rowsDeleted > 0) {
            Log.d("DAO", "Evento deletado com sucesso.");
        } else {
            Log.d("DAO", "Erro ao deletar evento. ID não encontrado: " + id);
        }
        db.close();
    }

    //Certificado (Métodos)
    public List<Certificado> getAllCertificados() {
        List<Certificado> certificados = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABELA_CERTIFICADOS, null);

        if (cursor.moveToFirst()) {
            do {
                Certificado certificado = new Certificado(
                        cursor.getLong(cursor.getColumnIndexOrThrow("id_certificado")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nome_certificado")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tipo_certificado")),
                        cursor.getString(cursor.getColumnIndexOrThrow("data_emissao")),
                        cursor.getString(cursor.getColumnIndexOrThrow("horas_certificado"))
                );
                certificados.add(certificado);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return certificados;
    }

    public Certificado buscarCertificadoPorId(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Certificado certificado = null;

        try {
            String query = "SELECT * FROM " + TABELA_CERTIFICADOS + " WHERE id_certificado = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

            Log.d("DAO", "Certificados encontrados: " + cursor.getCount());

            if (cursor.moveToFirst()) {
                certificado = new Certificado(
                        cursor.getLong(cursor.getColumnIndexOrThrow("id_certificado")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nome_certificado")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tipo_certificado")),
                        cursor.getString(cursor.getColumnIndexOrThrow("data_emissao")),
                        cursor.getString(cursor.getColumnIndexOrThrow("horas_certificado"))
                );
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        return certificado;
    }

    public void salvarCertificado(Certificado certificado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("nome_certificado", certificado.getNomeCertificado());
        values.put("tipo_certificado", certificado.getTipoCertificado());
        values.put("data_emissao", certificado.getDataEmissao());
        values.put("horas_certificado", certificado.getHorasCertificado());
        values.put("id_evento", certificado.getIdEvento());

        db.insert("certificados", null, values);
        db.close();
    }

    public void deletarCertificado(long idCertificado) {
        SQLiteDatabase db = this.getWritableDatabase();
        int resultado = db.delete(TABELA_CERTIFICADOS, "id_certificado = ?", new String[]{String.valueOf(idCertificado)});
        if (resultado > 0) {
            Log.i("DAO", "Certificado deletado com sucesso.");
        } else {
            Log.e("DAO", "Erro ao deletar o certificado.");
        }
        db.close();
    }

    public List<Certificado> getCertificadosPorEvento(int idEvento) {
        List<Certificado> certificados = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABELA_CERTIFICADOS + " WHERE id_evento = ?", new String[]{String.valueOf(idEvento)});

        if (cursor.moveToFirst()) {
            do {
                Certificado certificado = new Certificado(
                        cursor.getLong(cursor.getColumnIndexOrThrow("id_certificado")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nome_certificado")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tipo_certificado")),
                        cursor.getString(cursor.getColumnIndexOrThrow("data_emissao")),
                        cursor.getString(cursor.getColumnIndexOrThrow("horas_certificado"))
                );
                certificados.add(certificado);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return certificados;
    }


    @SuppressLint("Range")
    public List<Certificado> getCertificadosGerados() {
        List<Certificado> certificados = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM certificados", null);
        if (cursor.moveToFirst()) {
            do {
                Certificado certificado = new Certificado();
                certificado.setIdCertificado(cursor.getInt(cursor.getColumnIndex("id_certificado")));
                certificado.setNomeCertificado(cursor.getString(cursor.getColumnIndex("nome_certificado")));
                certificados.add(certificado);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return certificados;
    }

    //(Métodos)
    public boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");
        return db;
    }

    //Usuário (Métodos)
    public long inserirUsuario(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            Log.w(TAG, "Nome do usuário não pode ser vazio.");
            return -1;
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            Log.w(TAG, "Email do usuário não pode ser vazio.");
            return -1;
        }

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
        String query = "SELECT * FROM " + TABELA_USUARIOS + " WHERE email = ?";
        Cursor cursor = null;
        Usuario usuario = null;

        try {
            cursor = db.rawQuery(query, new String[]{email});
            if (cursor != null && cursor.moveToFirst()) {
                usuario = new Usuario();
                usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                usuario.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                usuario.setSenha(cursor.getString(cursor.getColumnIndexOrThrow("senha")));
                usuario.setCargo(cursor.getString(cursor.getColumnIndexOrThrow("cargo")));
            } else {
                Log.w(TAG, "Usuário com email " + email + " não encontrado.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar usuário por email: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return usuario;
    }

    public boolean deletarUsuario(Usuario usuario) {
        String email = usuario.getEmail();
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;

        try {
            int rowsDeleted = db.delete(TABELA_USUARIOS, "email = ?", new String[]{email});
            success = rowsDeleted > 0;
        } catch (Exception e) {
            Log.e(TAG, "Erro ao deletar usuário: " + e.getMessage());
        } finally {
            db.close();
        }
        return success;
    }

    //Inscrição (Métodos)
    public boolean inscreverNoEvento(long idUsuario, long idEvento) {
        if (verificarInscricao(idUsuario, idEvento)) {
            Log.i(TAG, "O usuário já está inscrito neste evento.");
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String dataInscricao = sdf.format(new Date());

        ContentValues values = new ContentValues();
        values.put("id_usuario", idUsuario);
        values.put("id_evento", idEvento);
        values.put("data_inscricao", dataInscricao);

        db.insert("Inscricoes", null, values);
        Log.i(TAG, "Usuário " + idUsuario + " inscrito no evento " + idEvento + " com sucesso.");
        return false;
    }

    @SuppressLint("Range")
    public List<Evento> getEventosInscritos(long idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Evento> eventos = new ArrayList<>();

        String query = "SELECT e.* FROM " + TABELA_EVENTO + " e " +
                "JOIN Inscricoes i ON e.id = i.id_evento " +
                "WHERE i.id_usuario = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idUsuario)});

        if (cursor.moveToFirst()) {
            do {
                Evento evento = new Evento();
                evento.setId(cursor.getLong(cursor.getColumnIndex("id")));
                evento.setNomeEvento(cursor.getString(cursor.getColumnIndex("nome_evento")));
                evento.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
                eventos.add(evento);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return eventos;
    }

    public boolean verificarInscricao(long idUsuario, long idEvento) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Inscricoes WHERE id_usuario = ? AND id_evento = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idUsuario), String.valueOf(idEvento)});

        boolean inscrito = cursor.getCount() > 0;
        cursor.close();

        return inscrito;
    }

    //Visão Geral (Métodos)
    public void atualizarNomeEvento(String nomeEvento, long idEvento) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome_evento", nomeEvento);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(idEvento)};

        db.update("evento", values, whereClause, whereArgs);
    }


    public void atualizarLocalEvento(String localEvento, long idEvento) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("local", localEvento);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(idEvento)};

        db.update("informacoes", values, whereClause, whereArgs);
    }

    public void atualizarHorarioInicioEvento(String horarioInicio, long idEvento) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("horarioInicio", horarioInicio);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(idEvento)};

        db.update("informacoes", values, whereClause, whereArgs);
    }

    public void atualizarHorarioTerminoEvento(String horarioTermino, long idEvento) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("horarioTermino", horarioTermino);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(idEvento)};

        db.update("informacoes", values, whereClause, whereArgs);
    }

    public void atualizarPrazoInscricaoEvento(String prazo, long idEvento) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("prazo", prazo);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(idEvento)};

        db.update("informacoes", values, whereClause, whereArgs);
    }

    public void atualizarValorEvento(double valorEvento, long idEvento) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("valorEvento", valorEvento);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(idEvento)};

        db.update("informacoes", values, whereClause, whereArgs);
    }

    public void atualizarDescricaoEvento(String descricao, long idEvento) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("descricao", descricao);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(idEvento)};

        db.update("evento", values, whereClause, whereArgs);
    }

    public void atualizarTipoEhorasNoBanco(String tipoEvento, String horasComplementares, long IdEvento) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("tipo_evento", tipoEvento);
        values.put("horas_complementares", horasComplementares);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(IdEvento)};

        db.update("evento", values, whereClause, whereArgs);
    }

    public void atualizarBannerEvento(long idEvento, byte[] bannerImagem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("banner_imagem", bannerImagem);

        Log.d("AtualizacaoBanner", "ID do Evento: " + idEvento);

        int rowsAffected = db.update(TABELA_EVENTO, values, "id = ?", new String[]{String.valueOf(idEvento)});
        db.close();

        if (rowsAffected > 0) {
            Log.i(TAG, "Imagem do banner atualizada com sucesso para o evento ID: " + idEvento);
        } else {
            Log.e(TAG, "Erro ao atualizar a imagem do banner para o evento ID: " + idEvento);
        }
    }

    public byte[] obterBannerEvento(long idEvento) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        byte[] imagemBytes = null;

        try {
            cursor = db.query(TABELA_EVENTO, new String[]{"banner_imagem"}, "id = ?",
                    new String[]{String.valueOf(idEvento)}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int colunaIndex = cursor.getColumnIndex("banner_imagem");
                if (colunaIndex >= 0) {
                    imagemBytes = cursor.getBlob(colunaIndex);
                } else {
                    Log.w("EventoDAO", "Coluna 'banner_imagem' não encontrada no cursor.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return imagemBytes;
    }

    public String obterDescricaoEvento(long idEvento) {
        String descricao = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT descricao FROM evento WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idEvento)});

        if (cursor.moveToFirst()) {
            descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
        }

        cursor.close();
        db.close();
        return descricao;
    }

    public boolean EventoPago(long idEvento) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean isPago = false;

        try {
            String query = "SELECT Pago FROM informacoes WHERE id_evento = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(idEvento)});

            if (cursor != null && cursor.moveToFirst()) {
                isPago = cursor.getInt(0) == 1;
            }
        } catch (Exception e) {
            Log.e("DAO", "Erro ao verificar o pagamento do evento: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return isPago;
    }

    public void adicionarAtividade(long idEvento, String nomeAtividade, String horario, String palestrante, String localAtividade, String data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_evento", idEvento);
        values.put("nomeAtividade", nomeAtividade);
        values.put("horario", horario);
        values.put("palestrante", palestrante);
        values.put("localAtividade", localAtividade);
        values.put("data", data);

        try {
            long newRowId = db.insert("cronograma", null, values);
            if (newRowId == -1) {
                Log.e("Database Error", "Erro ao inserir atividade");
            } else {
                Log.d("Database Success", "Atividade adicionada com sucesso! ID: " + newRowId + ", ID Evento: " + idEvento);
            }
        } catch (Exception e) {
            Log.e("Database Exception", "Erro ao adicionar atividade: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public List<Atividade> buscarAtividadesPorEvento(long idEvento) {
        List<Atividade> atividades = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM cronograma WHERE id_evento = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(idEvento)});

        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                String horario = cursor.getString(cursor.getColumnIndexOrThrow("horario"));
                String nomeAtividade = cursor.getString(cursor.getColumnIndexOrThrow("nomeAtividade"));
                String palestrante = cursor.getString(cursor.getColumnIndexOrThrow("palestrante"));
                String localAtividade = cursor.getString(cursor.getColumnIndexOrThrow("localAtividade"));

                Atividade atividade = new Atividade(data, horario, nomeAtividade, palestrante, localAtividade);
                atividades.add(atividade);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return atividades;
    }
}
