package com.example.tasktide.Objetos;

import android.util.Patterns;

public class Usuario {
    private long id;
    private String nome;
    private String email;
    private String senha;
    private String cargo;

    // Getter e setter para 'id'
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    // Getter e setter para 'nome'
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Getter e setter para 'email' com validação simples
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email inválido");
        }
    }

    // Getter e setter para 'senha'
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        if (senha != null && senha.length() >= 3) {
            this.senha = senha;
        } else {
            throw new IllegalArgumentException("A senha deve ter no mínimo 3 caracteres");
        }
    }

    // Getter e setter para 'cargo'
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    // Método toString() para facilitar a depuração
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cargo='" + cargo + '\'' +
                '}';
    }
}
