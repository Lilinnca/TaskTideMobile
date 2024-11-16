package com.example.tasktide.Objetos;

public class Informacoes {
    private String dataPrevista;
    private String dataFim;
    private String horarioInicio;
    private String horarioFim;
    private String prazo;
    private String local;
    private String pago; // Sim ou Não
    private Double valorEvento;

    public Informacoes(String dataPrevista, String dataFim, String horarioInicio, String horarioFim, String prazo, String local, Double valorEvento, String pago) {
        this.dataPrevista = dataPrevista;
        this.dataFim = dataFim;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.prazo = prazo;
        this.local = local;
        this.valorEvento = valorEvento;
        this.pago = pago;  // Certifique-se de inicializar
    }

    // Getters e setters
    public String getDataPrevista() {
        return dataPrevista;
    }

    public String getDataFim() {
        return dataFim;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public String getHorarioFim() {
        return horarioFim;
    }

    public String getPrazo() {
        return prazo;
    }

    public String getLocal() {
        return local;
    }

    public String getPago() {
        return pago;
    }  // Verifique se esse método existe

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

    public void setHorarioFim(String horarioFim) {
        this.horarioFim = horarioFim;
    }

    public void setPrazo(String prazo) {
        this.prazo = prazo;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }  // Certifique-se de ter esse setter

    public void setValorEvento(Double valorEvento) {
        this.valorEvento = valorEvento;
    }


    @Override
    public String toString() {
        return "Informacoes{" +
                "dataPrevista='" + dataPrevista + '\'' +
                ", dataFim='" + dataFim + '\'' +
                ", horarioInicio='" + horarioInicio + '\'' +
                ", horarioFim='" + horarioFim + '\'' +
                ", prazo='" + prazo + '\'' +
                ", local='" + local + '\'' +
                ", pago=" + (pago != null && pago.equals("Sim") ? "Sim" : "Não") +
                ", valorEvento=" + valorEvento +
                '}';
    }
}