package com.example.tasktide.Objetos;

public class Atividade {
    private String data;
    private String horario;
    private String nomeAtividade;
    private String palestrante;
    private String localAtividade;
    private String responsavel;
    private long idEvento;

    public Atividade(String data, String horario, String nomeAtividade, String palestrante, String localAtividade) {
        this.data = data;
        this.horario = horario;
        this.nomeAtividade = nomeAtividade;
        this.palestrante = palestrante;
        this.localAtividade = localAtividade;
        this.idEvento = idEvento;
    }

    public String getData() { return data; }
    public String getHorario() { return horario; }
    public String getNomeAtividade() { return nomeAtividade; }
    public String getPalestrante() { return palestrante; }
    public String getLocalAtividade() { return localAtividade; }
    public String getResponsavel() { return responsavel;}
    public long getIdEvento() { return idEvento; }
}
