package br.pucrs.poo.trabalho2.dados;

import java.time.LocalDate;

public class CartaoCredito extends FormaPagamento {
    private String numero;
    private LocalDate validade;

    public CartaoCredito(int cod, int diaVencimento, String numero, LocalDate validade) {
        super(cod, diaVencimento);
        this.numero = numero;
        this.validade = validade;
    }

    public String getNumero() {
        return this.numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getValidade() {
        return this.validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

}
