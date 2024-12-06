package com.example.tasktide.Objetos;

public class Informacoes {
    private long id;
    private long idEvento;
    private String dataPrevista;
    private String dataFim;
    private String horarioInicio;
    private String horarioTermino;
    private String prazo;
    private String local;
    private String pago;
    private Double valorEvento;

    public Informacoes(long id, long idEvento, String dataPrevista, String dataFim, String horarioInicio, String horarioTermino, String prazo, String local, String pago, Double valorEvento) {
        this.id = id;
        this.idEvento = idEvento;
        this.dataPrevista = dataPrevista;
        this.dataFim = dataFim;
        this.horarioInicio = horarioInicio;
        this.horarioTermino = horarioTermino;
        this.prazo = prazo;
        this.local = local;
        this.pago = pago;
        this.valorEvento = valorEvento;
    }

    public Informacoes() {

    }

    public Informacoes(String dataPrevista, String dataFim, String horarioInicio, String horarioTermino, String prazo, String local, double valorEvento, String pago) {
        this.dataPrevista = dataPrevista;
        this.dataFim = dataFim;
        this.horarioInicio = horarioInicio;
        this.horarioTermino = horarioTermino;
        this.prazo = prazo;
        this.local = local;
        this.valorEvento = valorEvento;
        this.pago = pago;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(long idEvento) {
        this.idEvento = idEvento;
    }

    public String getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(String dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioTermino() {
        return horarioTermino;
    }

    public void setHorarioTermino(String horarioTermino) {
        this.horarioTermino = horarioTermino;
    }

    public String getPrazo() {
        return prazo;
    }

    public void setPrazo(String prazo) {
        this.prazo = prazo;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public Double getValorEvento() {
        return valorEvento;
    }

    public void setValorEvento(Double valorEvento) {
        this.valorEvento = valorEvento;
    }
}
