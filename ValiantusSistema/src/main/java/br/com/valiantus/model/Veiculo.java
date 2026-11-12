package br.com.valiantus.model;
public class Veiculo {
    private int id,clienteId,ano;
    private String placa,marca,modelo,cor;
    public Veiculo(int id,int clienteId,String placa,String marca,String modelo,int ano,String cor) {
        this.id=id;this.clienteId=clienteId;this.placa=placa;this.marca=marca;this.modelo=modelo;this.ano=ano;this.cor=cor;
    }
    public int getId(){return id;}
    public int getClienteId(){return clienteId;}
    public String getPlaca(){return placa;}
    public String getMarca(){return marca;}
    public String getModelo(){return modelo;}
    public int getAno(){return ano;}
    public String getCor(){return cor;}
}
