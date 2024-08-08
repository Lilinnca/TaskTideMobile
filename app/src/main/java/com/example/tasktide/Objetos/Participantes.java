package com.example.tasktide.Objetos;


public class Participantes {


    private int id;
    private String quantParticipantes;


    public Participantes(){


    }


    public Participantes(String quantParticipantes) {
        this.quantParticipantes = quantParticipantes;


    }


    // Getters e setters


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getQuantParticipantes() {
        return quantParticipantes;
    }


    public void setQuantParticipantes(String quantParticipantes) {
        this.quantParticipantes = quantParticipantes;
    }


}
