package com.example.tasktide.Objetos;

import android.graphics.Bitmap;
import java.io.Serializable;

public class Evento implements Serializable {
    private long id;
    private String nomeEvento;
    private String tipoEvento;
    private String horasComplementares;
    private String modalidade;
    private String categoria;
    private String descricao;
    private String bannerImagem; // Se você deseja usar o caminho ou nome da imagem
    private Bitmap banner; // Armazena a imagem em si
    private String localEvento;
    private String dataEvento;

    public Evento() {}

    public Evento(String nomeEvento, String tipoEvento, String horasComplementares, String modalidade, String descricao,
                  String categoria, Bitmap banner, String localEvento, String dataEvento) {
        this.nomeEvento = nomeEvento;
        this.tipoEvento = tipoEvento;
        this.horasComplementares = horasComplementares;
        this.modalidade = modalidade;
        this.descricao = descricao;
        this.categoria = categoria;
        this.banner = banner;
        this.localEvento = localEvento;
        this.dataEvento = dataEvento;
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

    public Bitmap getBanner() {
        return banner;
    }

    public void setBanner(Bitmap banner) {
        this.banner = banner;
    }

    public String getBannerImagem() {
        return bannerImagem;
    }

    public void setBannerImagem(String bannerImagem) {
        this.bannerImagem = bannerImagem;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id; // Mantendo o tipo consistente
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
