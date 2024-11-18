package com.example.tasktide.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tasktide.Objetos.Atividade;
import com.example.tasktide.Objetos.Certificado;
import com.example.tasktide.Objetos.Evento;
import com.example.tasktide.Objetos.Informacoes;
import com.example.tasktide.Objetos.Participantes;
import com.example.tasktide.Objetos.Pdf;
import com.example.tasktide.Objetos.Usuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public static final String TABELA_ATIVIDADE = "atividade";
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
                "banner_imagem TEXT, " +
                "local_evento TEXT, " +
                "data_evento TEXT" +
                ");";
        db.execSQL(createTableEvento);
        Log.i(TAG, "Tabela de eventos criada com sucesso.");

        createUsuarioTable(db);
        createCronogramaTable(db);
        createInformacoesTable(db);
        createParticipantesTable(db);
        createAtividadeTable(db);
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
        String createCronogramaTable = "CREATE TABLE IF NOT EXISTS " + TABELA_CRONOGRAMA + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_evento INTEGER, " +
                "nomeAtividade TEXT, " +
                "horario TEXT, " +
                "palestrante TEXT, " +
                "localAtividade TEXT, " +
                "data TEXT, " +
                "FOREIGN KEY (id_evento) REFERENCES " + TABELA_EVENTO + "(id)" +
                ");";
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
                "Pago TEXT," +
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

    private void createAtividadeTable(SQLiteDatabase db) {
        String createAtividadeTable = "CREATE TABLE IF NOT EXISTS " + TABELA_ATIVIDADE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_evento INTEGER, " +
                "nomeAtividade TEXT, " +
                "horario TEXT, " +
                "responsavel TEXT, " +
                "localAtividade TEXT, " +
                "data TEXT, " +
                "FOREIGN KEY (id_evento) REFERENCES " + TABELA_EVENTO + "(id)" +
                ");";
        db.execSQL(createAtividadeTable);
        Log.i(TAG, "Tabela de atividades criada com sucesso.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_EVENTO);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_USUARIOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_INFORMACOES);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_PARTICIPANTES);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_CRONOGRAMA);
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_ATIVIDADE);
            db.execSQL("DROP TABLE IF EXISTS Inscricoes");
            db.execSQL("DROP TABLE IF EXISTS " + TABELA_CERTIFICADOS);

            onCreate(db);
            Log.i(TAG, "Banco de dados atualizado com sucesso.");
        }
    }

    public void limparTabelas() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABELA_PARTICIPANTES, null, null);
        db.delete(TABELA_INFORMACOES, null, null);
        db.delete(TABELA_EVENTO, null, null);
        db.delete(TABELA_CRONOGRAMA, null, null);
        Log.i(TAG, "Todas as tabelas foram limpas.");
        db.close();
    }


    public long inserirCertificado(Certificado certificado) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = -1;

        try {
            ContentValues values = new ContentValues();
            values.put("id_usuario", certificado.getIdUsuario());
            values.put("nome_certificado", certificado.getNomeCertificado());
            values.put("tipo_certificado", certificado.getTipoCertificado());
            values.put("data_emissao", certificado.getDataEmissao());
            values.put("horas_certificado", certificado.getHorasCertificado());

            id = db.insert(TABELA_CERTIFICADOS, null, values);
            Log.i(TAG, "Certificado inserido com ID: " + id);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao inserir certificado: " + e.getMessage());
        } finally {
            db.close();
        }

        return id;
    }

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

    public ArrayList<Certificado> listarCertificados(int idUsuario) {
        ArrayList<Certificado> certificados = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABELA_CERTIFICADOS + " WHERE id_usuario = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idUsuario)});

        if (cursor.moveToFirst()) {
            do {
                Certificado certificado = new Certificado(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_certificado")),
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
        Certificado certificado = null; // Declarar a variável 'certificado' aqui

        try {
            String query = "SELECT * FROM " + TABELA_CERTIFICADOS + " WHERE id_certificado = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

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


    public void deletarCertificados(long idCertificado) {
        SQLiteDatabase db = this.getWritableDatabase();
        int resultado = db.delete(TABELA_CERTIFICADOS, "id_certificado = ?", new String[]{String.valueOf(idCertificado)});
        if (resultado > 0) {
            Log.i("DAO", "Certificado deletado com sucesso.");
        } else {
            Log.e("DAO", "Erro ao deletar o certificado.");
        }
        db.close();
    }



    public void atualizarInformacoesEvento(long eventoId, String dataPrevista, String dataFim, String prazoInscricao, String local, boolean isPago, String valorEvento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("dataPrevista", dataPrevista);
        values.put("dataFim", dataFim);
        values.put("prazoInscricao", prazoInscricao);
        values.put("local", local);
        values.put("isPago", isPago ? 1 : 0);
        values.put("valorEvento", valorEvento);

        int rowsUpdated = db.update("Eventos", values, "id = ?", new String[]{String.valueOf(eventoId)});
        if (rowsUpdated > 0) {
            Log.d("DAO", "Informações do evento atualizadas com sucesso.");
        } else {
            Log.d("DAO", "Falha ao atualizar as informações do evento.");
        }
    }

    private boolean verificarDataValida(String data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            sdf.parse(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String formatarData(String data) {
        try {
            SimpleDateFormat sdfEntrada = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfSaida = new SimpleDateFormat("dd/MM/yyyy");
            return sdfSaida.format(sdfEntrada.parse(data));
        } catch (Exception e) {
            return data;
        }
    }

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
        } catch (Exception e) {
            Log.e(TAG, "Erro ao inserir evento: " + e.getMessage());
        } finally {
            db.close();
        }

        return id;
    }

    public long inserirAtividade(Atividade atividade, long idEvento) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put("id_evento", idEvento);
            values.put("nomeAtividade", atividade.getNomeAtividade());
            values.put("horario", atividade.getHorario());
            values.put("responsavel", atividade.getResponsavel());
            values.put("localAtividade", atividade.getLocalAtividade());
            values.put("data", atividade.getData());

            id = db.insert(TABELA_ATIVIDADE, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao inserir atividade: " + e.getMessage());
        } finally {
            db.close();
        }
        return id;
    }

    public boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void inserirInformacoes(Informacoes informacoes, long idEvento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("dataPrevista", informacoes.getDataPrevista());
        values.put("dataFim", informacoes.getDataFim());
        values.put("prazo", informacoes.getPrazo());
        values.put("local", informacoes.getLocal());
        values.put("valorEvento", informacoes.getValorEvento());
        values.put("pago", informacoes.getPago());
        values.put("id_evento", idEvento);

        // Só insere os horários se eles não estiverem vazios
        if (!informacoes.getHorarioInicio().isEmpty()) {
            values.put("horarioInicio", informacoes.getHorarioInicio());
        }
        if (!informacoes.getHorarioFim().isEmpty()) {
            values.put("horarioFim", informacoes.getHorarioFim());
        }

        db.insert("informacoes", null, values);
        db.close();
    }


    public String[] buscarInformacoesPorEvento(long eventoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT dataPrevista, dataFim, local FROM informacoes WHERE id = ?", new String[]{String.valueOf(eventoId)});

        if (cursor.moveToFirst()) {
            String dataPrevista = cursor.getString(0);
            String dataFim = cursor.getString(1);
            String local = cursor.getString(2);
            cursor.close();
            return new String[]{dataPrevista, dataFim, local};
        }
        cursor.close();
        return new String[]{"", "", ""}; // Valores padrão
    }



    @Override
    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");
        return db;
    }

    public Evento buscarEventoPorId(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Evento evento = null;
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + TABELA_EVENTO + " WHERE id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

            if (cursor != null && cursor.moveToFirst()) {
                evento = new Evento();
                evento.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                evento.setNomeEvento(cursor.getString(cursor.getColumnIndexOrThrow("nome_evento")));
                evento.setTipoEvento(cursor.getString(cursor.getColumnIndexOrThrow("tipo_evento")));
                evento.setHorasComplementares(cursor.getString(cursor.getColumnIndexOrThrow("horas_complementares")));
                evento.setModalidade(cursor.getString(cursor.getColumnIndexOrThrow("modalidade")));
                evento.setCategoria(cursor.getString(cursor.getColumnIndexOrThrow("categoria")));
                evento.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                evento.setBannerImagem(cursor.getString(cursor.getColumnIndexOrThrow("banner_imagem")));
                evento.setLocalEvento(cursor.getString(cursor.getColumnIndexOrThrow("local_evento")));
                evento.setDataEvento(cursor.getString(cursor.getColumnIndexOrThrow("data_evento")));
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar evento com ID: " + id, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return evento;
    }


    public List<Atividade> buscarAtividadesPorEvento(long idEvento) {
        List<Atividade> atividades = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABELA_ATIVIDADE + " WHERE id_evento = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, new String[]{String.valueOf(idEvento)});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                    String horario = cursor.getString(cursor.getColumnIndexOrThrow("horario"));
                    String nomeAtividade = cursor.getString(cursor.getColumnIndexOrThrow("nomeAtividade"));
                    String responsavel = cursor.getString(cursor.getColumnIndexOrThrow("responsavel"));
                    String localAtividade = cursor.getString(cursor.getColumnIndexOrThrow("localAtividade"));

                    Atividade atividade = new Atividade(data, horario, nomeAtividade, responsavel, localAtividade);

                    atividades.add(atividade);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar atividades por evento: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return atividades;
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

    public void deleteEvento(long idEvento) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rowsDeleted = db.delete(TABELA_EVENTO, "id = ?", new String[]{String.valueOf(idEvento)});
            if (rowsDeleted > 0) {
                Log.i(TAG, "Evento deletado com sucesso.");
            } else {
                Log.w(TAG, "Nenhum evento encontrado com o id: " + idEvento);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao deletar evento: " + e.getMessage());
        } finally {
            db.close();
        }
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

    public void inscreverNoEvento(long idUsuario, long idEvento) {
        if (verificarInscricao(idUsuario, idEvento)) {
            Log.i(TAG, "O usuário já está inscrito neste evento.");
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dataInscricao = sdf.format(new Date());

        ContentValues values = new ContentValues();
        values.put("id_usuario", idUsuario);
        values.put("id_evento", idEvento);
        values.put("data_inscricao", dataInscricao);

        db.insert("Inscricoes", null, values);
        Log.i(TAG, "Usuário " + idUsuario + " inscrito no evento " + idEvento + " com sucesso.");
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


    public String obterDescricaoEvento(long idEvento) {
        String descricao = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT descricao FROM evento WHERE id = ?";
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(idEvento)});
            if (cursor.moveToFirst()) {
                descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao obter descrição do evento: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return descricao;
    }

    public void atualizarNomeEvento(long idEvento, String novoNome) {
        if (novoNome == null || novoNome.trim().isEmpty()) {
            Log.w(TAG, "Nome do evento não pode ser vazio ou nulo");
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome_evento", novoNome);

        try {
            db.update(TABELA_EVENTO, values, "id = ?", new String[]{String.valueOf(idEvento)});
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar nome do evento: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public void atualizarLocalEvento(long idEvento, String novoLocal) {
        if (novoLocal == null || novoLocal.trim().isEmpty()) {
            Log.w(TAG, "Local do evento não pode ser vazio ou nulo.");
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("local_evento", novoLocal);

        try {
            db.update(TABELA_EVENTO, values, "id = ?", new String[]{String.valueOf(idEvento)});
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar local do evento: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public void adicionarAtividade(long idEvento, String nomeAtividade, String horario, String palestrante, String localAtividade, String data) {
        if (nomeAtividade == null || nomeAtividade.isEmpty()) {
            Log.w(TAG, "Nome da atividade não pode ser vazio.");
            return;
        }

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
                Log.d("Database Success", "Atividade adicionada com sucesso! ID: " + newRowId);
            }
        } catch (Exception e) {
            Log.e("Database Error", "Erro ao adicionar atividade: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public void atualizarEventoTipoEhoras(long idEvento, String tipoEvento, String horasComplementares) {
        if (tipoEvento == null || tipoEvento.trim().isEmpty() || horasComplementares == null || horasComplementares.trim().isEmpty()) {
            Log.w(TAG, "Tipo de evento ou horas complementares não podem ser vazios.");
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tipoEvento", tipoEvento);
        values.put("horasComplementares", horasComplementares);

        try {
            db.update("evento", values, "id = ?", new String[]{String.valueOf(idEvento)});
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar tipo de evento e horas complementares: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public void atualizarDataEvento(long idEvento, String novaData) {
        if (novaData == null || novaData.trim().isEmpty()) {
            Log.w(TAG, "Data do evento não pode ser vazia.");
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("dataPrevis", novaData);

        try {
            db.update(TABELA_INFORMACOES, values, "id_evento = ?", new String[]{String.valueOf(idEvento)});
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar data do evento: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public void atualizarHorarioInicio(long idEvento, String novoHorarioInicio) {
        if (novoHorarioInicio == null || novoHorarioInicio.trim().isEmpty()) {
            Log.w(TAG, "Horário de início não pode ser vazio.");
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("horarioInicio", novoHorarioInicio);

        try {
            db.update(TABELA_INFORMACOES, values, "id_evento = ?", new String[]{String.valueOf(idEvento)});
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar horário de início do evento: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public void atualizarHorarioTermino(long idEvento, String novoHorarioTermino) {
        if (novoHorarioTermino == null || novoHorarioTermino.trim().isEmpty()) {
            Log.w(TAG, "Horário de término não pode ser vazio.");
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("horarioTermino", novoHorarioTermino);

        try {
            db.update(TABELA_INFORMACOES, values, "id_evento = ?", new String[]{String.valueOf(idEvento)});
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar horário de término do evento: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public void atualizarTipoEvento(long idEvento, String novoTipo) {
        if (novoTipo == null || novoTipo.trim().isEmpty()) {
            Log.w(TAG, "Tipo do evento não pode ser vazio.");
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tipoEvento", novoTipo);

        try {
            db.update(TABELA_EVENTO, values, "id = ?", new String[]{String.valueOf(idEvento)});
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar tipo do evento: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public void atualizarPrazoEvento(long idEvento, String novoPrazo) {
        if (novoPrazo == null || novoPrazo.trim().isEmpty()) {
            Log.w(TAG, "Prazo do evento não pode ser vazio.");
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("prazo", novoPrazo);

        try {
            db.update(TABELA_INFORMACOES, values, "id_evento = ?", new String[]{String.valueOf(idEvento)});
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar prazo do evento: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public void atualizarValorEvento(long idEvento, String novoValor) {
        if (novoValor == null || novoValor.trim().isEmpty()) {
            Log.w(TAG, "Valor do evento não pode ser vazio.");
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("valorEvento", novoValor);

        try {
            db.update(TABELA_INFORMACOES, values, "id_evento = ?", new String[]{String.valueOf(idEvento)});
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar valor do evento: " + e.getMessage());
        } finally {
            db.close();
        }
    }


    public void atualizarBannerEvento(long idEvento, byte[] bannerImagem) {
        if (bannerImagem == null || bannerImagem.length == 0) {
            Log.w(TAG, "Imagem do banner não pode ser vazia.");
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bannerImagem", bannerImagem);

        try {
            int rowsAffected = db.update(TABELA_EVENTO, values, "id = ?", new String[]{String.valueOf(idEvento)});
            if (rowsAffected > 0) {
                Log.i(TAG, "Imagem do banner atualizada com sucesso para o evento ID: " + idEvento);
            } else {
                Log.e(TAG, "Erro ao atualizar a imagem do banner para o evento ID: " + idEvento);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar banner do evento: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    @SuppressLint("Range")
    public Evento getEventoById(int idEvento) {
        Evento evento = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM evento WHERE id = ?", new String[]{String.valueOf(idEvento)});

        if (cursor.moveToFirst()) {
            evento = new Evento();

            int nomeEventoIndex = cursor.getColumnIndex("nomeEvento");
            int descricaoIndex = cursor.getColumnIndex("descricao");

            if (nomeEventoIndex != -1) {
                evento.setNomeEvento(cursor.getString(nomeEventoIndex));
            } else {
                Log.e("DB_ERROR", "Coluna nomeEvento não encontrada.");
            }

            if (descricaoIndex != -1) {
                evento.setDescricao(cursor.getString(descricaoIndex));
            } else {
                Log.e("DB_ERROR", "Coluna descricao não encontrada.");
            }

        } else {
            Log.e("DB_ERROR", "Nenhum evento encontrado com o ID: " + idEvento);
        }

        cursor.close();
        db.close();
        return evento;
    }

    @SuppressLint("Range")
    public Informacoes getInformacoesById(long idEvento) {
        Informacoes informacoes = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM informacoes WHERE evento_id = ?", new String[]{String.valueOf(idEvento)});

        if (cursor.moveToFirst()) {
            informacoes = new Informacoes(
                    cursor.getString(cursor.getColumnIndex("dataPrevista")),
                    cursor.getString(cursor.getColumnIndex("dataFim")),
                    cursor.getString(cursor.getColumnIndex("horarioInicio")),
                    cursor.getString(cursor.getColumnIndex("horarioFim")),
                    cursor.getString(cursor.getColumnIndex("prazo")),
                    cursor.getString(cursor.getColumnIndex("local")),
                    cursor.getDouble(cursor.getColumnIndex("valorEvento")),
                    cursor.getString(cursor.getColumnIndex("pago"))
            );
        }
        cursor.close();
        return informacoes;
    }

    public void atualizarDescricaoEvento(long idEvento, String novaDescricao) {
        if (novaDescricao == null || novaDescricao.trim().isEmpty()) {
            Log.w(TAG, "Descrição do evento não pode ser vazia.");
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("descricao", novaDescricao);

        try {
            db.update(TABELA_EVENTO, values, "id = ?", new String[]{String.valueOf(idEvento)});
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao atualizar descrição do evento: " + e.getMessage());
        } finally {
            db.close();
        }
    }

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

    public byte[] obterBannerEvento(long idEvento) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        byte[] imagemBytes = null;

        try {
            cursor = db.query(TABELA_EVENTO, new String[]{"bannerImagem"}, "id = ?",
                    new String[]{String.valueOf(idEvento)}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int colunaIndex = cursor.getColumnIndex("bannerImagem");
                if (colunaIndex != -1) {
                    imagemBytes = cursor.getBlob(colunaIndex);
                } else {
                    Log.w("EventoDAO", "Coluna 'bannerImagem' não encontrada.");
                }
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao obter banner do evento: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return imagemBytes;
    }

    public long inserirParticipantes(Participantes participantes, long idEvento) {
        if (participantes.getQuantParticipantes() <= 0) {
            Log.w(TAG, "Número de participantes inválido.");
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantParticipantes", participantes.getQuantParticipantes());
        values.put("id_evento", idEvento);

        long id = db.insert(TABELA_PARTICIPANTES, null, values);
        db.close();
        return id;
    }

    public Usuario getUsuarioLogado() {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);
            String usuarioEmail = sharedPreferences.getString("email", null);
            if (usuarioEmail != null) {
                return buscarUsuarioPorEmail(usuarioEmail);
            }
        } else {
            Log.e("DAO", "Contexto nulo! Não é possível acessar SharedPreferences.");
        }
        return null;
    }





    public long inserirAtividade(Atividade atividade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_evento", atividade.getIdEvento());
        values.put("nomeAtividade", atividade.getNomeAtividade());
        values.put("horario", atividade.getHorario());
        values.put("palestrante", atividade.getPalestrante());
        values.put("localAtividade", atividade.getLocalAtividade());
        values.put("data", atividade.getData());
        long id = db.insert(TABELA_CRONOGRAMA, null, values);
        db.close();
        return id;
    }
}
