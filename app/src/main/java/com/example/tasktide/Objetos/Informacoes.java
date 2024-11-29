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
    private int pago; // Agora é int para representar 1 ("Sim") ou 0 ("Não")
    private Double valorEvento;

    public Informacoes(long id, long idEvento, String dataPrevista, String dataFim, String horarioInicio, String horarioTermino, String prazo, String local, int pago, Double valorEvento) {
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

    public Informacoes(String dataPrevista, String dataFim, String horarioInicio, String horarioTermino, String prazo, String local, double valorEvento, int pago) {
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

    public int getPago() {
        return pago;
    }

    public void setPago(int pago) {
        this.pago = pago;
    }

    public Double getValorEvento() {
        return valorEvento;
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
                ", pago=" + (pago == 1 ? "Sim" : "Não") + // Converte int para "Sim" ou "Não"
                ", valorEvento=" + valorEvento +
                '}';
    }
}
