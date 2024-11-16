package com.example.tasktide.Objetos;

public class Certificado {
    private int idCertificado;
    private int idUsuario;
    private String nomeCertificado;
    private String tipoCertificado;
    private String dataEmissao;

    public Certificado(int idCertificado, int idUsuario, String nomeCertificado, String tipoCertificado, String dataEmissao) {
        this.idCertificado = idCertificado;
        this.idUsuario = idUsuario;
        this.nomeCertificado = nomeCertificado;
        this.tipoCertificado = tipoCertificado;
        this.dataEmissao = dataEmissao;
    }

    // Getters e setters
    public int getIdCertificado() {
        return idCertificado;
    }

    public void setIdCertificado(int idCertificado) {
        this.idCertificado = idCertificado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeCertificado() {
        return nomeCertificado;
    }

    public void setNomeCertificado(String nomeCertificado) {
        this.nomeCertificado = nomeCertificado;
    }

    public String getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(String tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }
}
