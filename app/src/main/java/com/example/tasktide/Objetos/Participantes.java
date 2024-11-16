package com.example.tasktide.Objetos;

public class Participantes {

    private int id;
    private int quantParticipantes;

    public Participantes(int quantParticipantes) {
        this.quantParticipantes = quantParticipantes;
    }

    public Participantes(int quantParticipantes, boolean anyCheckBoxChecked) {
        this.quantParticipantes = quantParticipantes;
        // Lógica relacionada ao checkbox pode ser adicionada aqui
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantParticipantes() {
        return quantParticipantes;
    }

    public void setQuantParticipantes(int quantParticipantes) {
        this.quantParticipantes = quantParticipantes;
    }

    @Override
    public String toString() {
        return "Participantes{" +
                "id=" + id +
                ", quantParticipantes=" + quantParticipantes +
                '}';
    }
}
