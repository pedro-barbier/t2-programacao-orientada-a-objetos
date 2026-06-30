package br.pucrs.poo.trabalho2.dados;

import java.util.ArrayList;
import java.util.List;

public abstract class Cliente {
    private int numero;
    private String nome;
    private String email;
    private List<Contrato> contratos;
    private List<FormaPagamento> formasPagamento;

    public Cliente(int numero, String nome, String email) {
        this.numero = numero;
        this.nome = nome;
        this.email = email;
        formasPagamento = new ArrayList<>();
        contratos = new ArrayList<>();
    }

    public abstract String descrever();

    public int getNumero() {
        return this.numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addContrato(Contrato contrato) {
        contratos.add(contrato);
    }

    public List<Contrato> getContratos() {
        List<Contrato> aux = new ArrayList<>();
        for (Contrato c : contratos) {
            aux.add(c);
        }
        return aux;
    }

    public void addFormaPagamento(FormaPagamento formaPagamento) {
        formasPagamento.add(formaPagamento);
    }

    public List<FormaPagamento> getFormasPagamento() {
        List<FormaPagamento> aux = new ArrayList<>();
        for (FormaPagamento f : formasPagamento) {
            aux.add(f);
        }
        return aux;
    }

    public FormaPagamento buscarFormaPagamento(int cod) {
        for (FormaPagamento f : formasPagamento) {
            if (f.getCod() == cod) {
                return f;
            }
        }
        return null;
    }
}
