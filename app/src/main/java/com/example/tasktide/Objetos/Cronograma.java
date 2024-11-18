package com.example.tasktide.Objetos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cronograma")
public class Cronograma {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String data;
    private String horario;
    private String atividade;
    private String palestrante;

    public Cronograma() {
    }

    public Cronograma(String data, String horario, String atividade, String palestrante) {
        this.data = data;
        this.horario = horario;
        this.atividade = atividade;
        this.palestrante = palestrante;
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

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public String getPalestrante() {
        return palestrante;
    }

    public void setPalestrante(String palestrante) {
        this.palestrante = palestrante;
    }

    @Override
    public String toString() {
        return "Cronograma{" +
                "data='" + data + '\'' +
                ", horario='" + horario + '\'' +
                ", atividade='" + atividade + '\'' +
                ", palestrante='" + palestrante + '\'' +
                '}';
    }
}
