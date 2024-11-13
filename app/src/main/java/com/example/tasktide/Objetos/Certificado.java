package com.example.tasktide.Objetos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "certificados")
public class Certificado {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int recursoId; // ID do recurso drawable

    public Certificado(int recursoId) {
        this.recursoId = recursoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecursoId() {
        return recursoId;
    }

    public void setRecursoId(int recursoId) {
        this.recursoId = recursoId;
    }
}
