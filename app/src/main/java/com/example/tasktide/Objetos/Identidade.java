package com.example.tasktide.Objetos;


public class Identidade {
    private String nome;
    private String cargo;
    private String departamento;
    private String contato;


    public Identidade(String nome, String cargo, String departamento, String contato) {
        this.nome = nome;
        this.cargo = cargo;
        this.departamento = departamento;
        this.contato = contato;
    }


    // Getters
    public String getNome() {
        return nome;
    }


    public String getCargo() {
        return cargo;
    }


    public String getDepartamento() {
        return departamento;
    }


    public String getContato() {
        return contato;
    }


    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }


    public void setCargo(String cargo) {
        this.cargo = cargo;
    }


    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }


    public void setContato(String contato) {
        this.contato = contato;
    }
}
