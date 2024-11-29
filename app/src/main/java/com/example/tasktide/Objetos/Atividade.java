package com.example.tasktide.Objetos;

public class Atividade {
    private String data;
    private String horario;
    private String nomeAtividade;
    private String palestrante;
    private String localAtividade;
    private long idEvento;

    public Atividade(String data, String horario, String nomeAtividade, String palestrante, String localAtividade, long idEvento) {
        this.data = data;
        this.horario = horario;
        this.nomeAtividade = nomeAtividade;
        this.palestrante = palestrante;
        this.localAtividade = localAtividade;
        this.idEvento = idEvento;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public void setNomeAtividade(String nomeAtividade) {
        this.nomeAtividade = nomeAtividade;
    }

    public void setPalestrante(String palestrante) {
        this.palestrante = palestrante;
    }

    public void setLocalAtividade(String localAtividade) {
        this.localAtividade = localAtividade;
    }

    public void setIdEvento(long idEvento) {
        this.idEvento = idEvento;
    }

    public String getData() {
        return data;
    }

    public String getHorario() {
        return horario;
    }

    public String getNomeAtividade() {
        return nomeAtividade;
    }

    public String getPalestrante() {
        return palestrante;
    }

    public String getLocalAtividade() {
        return localAtividade;
    }

    public long getIdEvento() {
        return idEvento;
    }
}
