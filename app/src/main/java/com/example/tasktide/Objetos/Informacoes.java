package com.example.tasktide.Objetos;


public class Informacoes {
    private String dataPrevis;
    private String horarioInicio;
    private String horarioTermino;
    private String local;


    public Informacoes(String dataPrevis, String horarioInicio, String horarioTermino, String local) {
        this.dataPrevis = dataPrevis;
        this.horarioInicio = horarioInicio;
        this.horarioTermino = horarioTermino;
        this.local = local;
    }


    // Getters
    public String getDataPrevis() {
        return dataPrevis;
    }


    public String getHorarioInicio() {
        return horarioInicio;
    }


    public String getHorarioTermino() {
        return horarioTermino;
    }


    public String getLocal() {
        return local;
    }


    // Setters
    public void setDataPrevis(String dataPrevis) {
        this.dataPrevis = dataPrevis;
    }


    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }


    public void setHorarioTermino(String horarioTermino) {
        this.horarioTermino = horarioTermino;
    }


    public void setLocal(String local) {
        this.local = local;
    }
}
