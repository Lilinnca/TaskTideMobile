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

    public String getDataFim() {
        return dataFim;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public String getHorarioTermino() {
        return horarioTermino;
    }

    public String getPrazo() {
        return prazo;
    }

    public String getLocal() {
        return local;
    }

    public String getPago() {
        return pago;
    }

    public Double getValorEvento() {
        return valorEvento;
    }

    public void setDataPrevista(String dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public void setHorarioTermino(String horarioTermino) {
        this.horarioTermino = horarioTermino;
    }

    public void setPrazo(String prazo) {
        this.prazo = prazo;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public void setValorEvento(Double valorEvento) {
        this.valorEvento = valorEvento;
    }

    @Override
    public String toString() {
        return "Informacoes{" +
                "dataPrevista='" + dataPrevista + '\'' +
                ", dataFim='" + dataFim + '\'' +
                ", horarioInicio='" + horarioInicio + '\'' +
                ", horarioTermino='" + horarioTermino + '\'' +
                ", prazo='" + prazo + '\'' +
                ", local='" + local + '\'' +
                ", pago=" + (pago != null && pago.equals("Sim") ? "Sim" : "Não") +
                ", valorEvento=" + valorEvento +
                '}';
    }
}