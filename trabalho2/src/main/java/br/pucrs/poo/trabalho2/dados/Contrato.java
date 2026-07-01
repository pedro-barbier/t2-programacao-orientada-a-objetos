package br.pucrs.poo.trabalho2.dados;

import java.time.LocalDate;

public class Contrato {
    private int id;
    private LocalDate data;
    private int periodo;
    private Cliente cliente;
    private Jogo jogo;
    private FormaPagamento formaPagamento;

    public Contrato(int id, LocalDate data, int periodo, Cliente cliente, Jogo jogo, FormaPagamento formaPagamento) {
        this.id = id;
        this.data = data;
        this.periodo = periodo;
        this.cliente = cliente;
        this.jogo = jogo;
        this.formaPagamento = formaPagamento;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return this.data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getPeriodo() {
        return this.periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Jogo getJogo() {
        return this.jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public FormaPagamento getFormaPagamento() {
        return this.formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public double calculaValorFinal() {
        double valorBase = getJogo().getValorDiario();
        double aumento = 0.0;
        switch (getJogo().getCategoria()) {
            case Categoria.AVENTURA:
                aumento += 0.05; // Aventura: +5%
                break;
            case Categoria.ESTRATEGIA:
                aumento += 0.10; // Estratégia: +10%
                break;
            case Categoria.CORRIDA:
                aumento += 0.15; // Corrida: +15%
                break;
        }
        if (getFormaPagamento() instanceof PIX) {
            aumento -= 0.05; // PIX: -5%
        } else if (getFormaPagamento() instanceof CartaoCredito) {
            aumento += 0.05; // Cartão de Crédito: +5%
        }
        valorBase += valorBase * aumento;
        if (getCliente().getContratos().size() > 2) {
            return (valorBase * getPeriodo()) * 0.97;
        } else {
            return valorBase * getPeriodo();
        }
    }

    public String descrever() {
        return getId() + ";" 
                + getData() + ";"
                + getPeriodo() + ";"
                + getCliente().getNumero() + ";"
                + getJogo().getCodigo();
    }
}
