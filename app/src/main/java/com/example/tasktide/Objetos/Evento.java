package com.example.tasktide.Objetos;

import java.io.Serializable;

public class Evento implements Serializable {
    private long id;
    private String nomeEvento;
    private String tipoEvento;
    private String horasComplementares;
    private String modalidade;
    private String categoria;
    private String descricao;
    private String bannerImagem;
    private String localEvento;
    private String dataEvento;


    // Construtor sem parâmetros
    public Evento() {}

    // Construtor com todos os parâmetros
    public Evento(long id, String nomeEvento, String tipoEvento, String horasComplementares, String modalidade, String categoria, String descricao, String bannerImagem, String localEvento, String dataEvento) {
        this.id = id;
        this.nomeEvento = nomeEvento;
        this.tipoEvento = tipoEvento;
        this.horasComplementares = horasComplementares;
        this.modalidade = modalidade;
        this.categoria = categoria;
        this.descricao = descricao;
        this.bannerImagem = bannerImagem;
        this.localEvento = localEvento;
        this.dataEvento = dataEvento;
    }

    // Getters e setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getHorasComplementares() {
        return horasComplementares;
    }

    public void setHorasComplementares(String horasComplementares) {
        this.horasComplementares = horasComplementares;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getBannerImagem() {
        return bannerImagem;
    }

    public void setBannerImagem(String bannerImagem) {
        this.bannerImagem = bannerImagem;
    }

    public String getLocalEvento() {
        return localEvento;
    }

    public void setLocalEvento(String localEvento) {
        this.localEvento = localEvento;
    }

    public String getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(String dataEvento) {
        this.dataEvento = dataEvento;
    }

    // Método toString para exibir as informações do evento
    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", nomeEvento='" + nomeEvento + '\'' +
                ", tipoEvento='" + tipoEvento + '\'' +
                ", horasComplementares='" + horasComplementares + '\'' +
                ", modalidade='" + modalidade + '\'' +
                ", categoria='" + categoria + '\'' +
                ", descricao='" + descricao + '\'' +
                ", localEvento='" + localEvento + '\'' +
                ", dataEvento='" + dataEvento + '\'' +
                '}';
    }
}
