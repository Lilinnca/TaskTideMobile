package com.example.tasktide.Objetos;

public class Certificado {
    private long idCertificado;
    private int idUsuario;
    private String nomeCertificado;
    private String tipoCertificado;
    private String dataEmissao;
    private String horasCertificado;

    // Construtor com parâmetros
    public Certificado(long idCertificado, int idUsuario, String nomeCertificado, String tipoCertificado, String dataEmissao, String horasCertificado) {
        this.idCertificado = idCertificado;
        this.idUsuario = idUsuario;
        this.nomeCertificado = nomeCertificado;
        this.tipoCertificado = tipoCertificado;
        this.dataEmissao = dataEmissao;
        this.horasCertificado = horasCertificado;
    }

    public Certificado() {
    }

    // Getters e setters
    public long getIdCertificado() {
        return idCertificado;
    }

    public void setIdCertificado(long idCertificado) {
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

    public String getHorasCertificado() {
        return horasCertificado;
    }

    public void setHorasCertificado(String horasCertificado) {
        this.horasCertificado = horasCertificado;
    }

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }
}
