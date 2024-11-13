package com.example.tasktide.Objetos;


public class Informacoes {
    private String dataPrevis;
    private String dataFim;
    private String horarioInicio;
    private String horarioFim;
    private String prazo;
    private String local;
    private String pago; // Sim ou Não
    private Double valorEvento;


    public Informacoes(String dataPrevis, String dataFim, String horarioInicio, String horarioFim, String prazo, String local, Double valorEvento, String pago) {
        this.dataPrevis = dataPrevis;
        this.dataFim = dataFim;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.prazo = prazo;
        this.local = local;
        this.valorEvento = valorEvento;
        this.pago = pago;
    }


    // Getters
    public String getDataPrevis() { return dataPrevis; }
    public String getDataFim() { return dataFim; }
    public String getHorarioInicio() { return horarioInicio; }
    public String getHorarioFim() { return horarioFim; }
    public String getPrazo() { return prazo; }
    public String getLocal() { return local; }
    public String getPago() { return pago; }
    public Double getValorEvento() { return valorEvento; }
}



