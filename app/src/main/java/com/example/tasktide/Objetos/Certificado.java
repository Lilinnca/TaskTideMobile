package com.example.tasktide.Objetos;

public class Certificado {
    private long idCertificado;      // ID do certificado
    private int idUsuario;           // ID do usuário
    private int idEvento;            // ID do evento associado
    private String nomeCertificado;  // Nome do certificado
    private String tipoCertificado;  // Tipo do certificado
    private String dataEmissao;      // Data de emissão do certificado
    private String horasCertificado; // Horas complementares do certificado

    // Construtor com parâmetros
    public Certificado(long idCertificado, int idUsuario, String nomeCertificado,
                       String tipoCertificado, String dataEmissao, String horasCertificado) {
        this.idCertificado = idCertificado;
        this.idUsuario = idUsuario;
        this.idEvento = idEvento;
        this.nomeCertificado = nomeCertificado;
        this.tipoCertificado = tipoCertificado;
        this.dataEmissao = dataEmissao;
        this.horasCertificado = horasCertificado;
    }

    // Construtor padrão
    public Certificado() {
    }

    // Getters e Setters
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

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
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
