package com.example.tasktide.Objetos;

public class Atividade {
    private String data;
    private String horario;
    private String nomeAtividade;
    private String palestrante;
    private String localAtividade;

    // Supondo que você queira adicionar campos de responsável e idEvento
    private String responsavel;  // Exemplo de campo
    private long idEvento;       // Exemplo de campo

    // Construtor atualizado
    public Atividade(String data, String horario, String nomeAtividade, String palestrante, String localAtividade) {
        this.data = data;
        this.horario = horario;
        this.nomeAtividade = nomeAtividade;
        this.palestrante = palestrante;
        this.localAtividade = localAtividade;
        this.idEvento = idEvento;       // Definindo o campo
    }

    public String getData() { return data; }
    public String getHorario() { return horario; }
    public String getNomeAtividade() { return nomeAtividade; }
    public String getPalestrante() { return palestrante; }
    public String getLocalAtividade() { return localAtividade; }

    // Métodos de acesso para os novos campos
    public String getResponsavel() {
        return responsavel; // Retorna o responsável
    }

    public long getIdEvento() {
        return idEvento; // Retorna o id do evento
    }
}
