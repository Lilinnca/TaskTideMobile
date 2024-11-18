package com.example.tasktide.Objetos;

public class Pdf {
    private long idPdf;
    private String nomePdf;
    private long idCertificado;

    public Pdf(long idPdf, String nomePdf, long idCertificado) {
        this.idPdf = idPdf;
        this.nomePdf = nomePdf;
        this.idCertificado = idCertificado;
    }

    public long getIdPdf() {
        return idPdf;
    }

    public void setIdPdf(long idPdf) {
        this.idPdf = idPdf;
    }

    public String getNomePdf() {
        return nomePdf;
    }

    public void setNomePdf(String nomePdf) {
        this.nomePdf = nomePdf;
    }

    public long getIdCertificado() {
        return idCertificado;
    }

    public void setIdCertificado(long idCertificado) {
        this.idCertificado = idCertificado;
    }
}
