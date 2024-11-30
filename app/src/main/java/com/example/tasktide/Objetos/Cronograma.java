package com.example.tasktide.Objetos;
public class Cronograma {
    private long id;
    private String nomeAtividade;
    private String data;
    private String horario;
    private String palestrante;
    private String local;

    public Cronograma(long id, String nomeAtividade, String data, String horario, String palestrante, String local) {
        this.id = id;
        this.nomeAtividade = nomeAtividade;
        this.data = data;
        this.horario = horario;
        this.palestrante = palestrante;
        this.local = local;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeAtividade() {
        return nomeAtividade;
    }

    public void setNomeAtividade(String nomeAtividade) {
        this.nomeAtividade = nomeAtividade;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getPalestrante() {
        return palestrante;
    }

    public void setPalestrante(String palestrante) {
        this.palestrante = palestrante;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    @Override
    public String toString() {
        return nomeAtividade + " - " + data + " às " + horario;
    }
}
