package com.example.tasktide.Objetos;

import android.graphics.Bitmap;

public class Evento {
    private int id;
    private String nomeEvento;
    private String tipoEvento;
    private String horasComplementares;
    private String modalidade;
    private Bitmap banner;


    public Evento() {
    }


    public Evento(String nomeEvento, String tipoEvento, String horasComplementares, String modalidade) {
        this.nomeEvento = nomeEvento;
        this.tipoEvento = tipoEvento;
        this.horasComplementares = horasComplementares;
        this.modalidade = modalidade;
        this.banner = banner;
    }


    // Getters e setters
    public Bitmap getBannerBitmap() {
        return banner;
    }


    public void setBannerBitmap(Bitmap banner) {
        this.banner = banner;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
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


}

