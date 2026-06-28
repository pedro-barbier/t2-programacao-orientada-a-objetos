package br.pucrs.poo.trabalho2.dados;

public class FormaPagamento {
    private int cod;
    private int diaVencimento;

    public FormaPagamento(int cod, int diaVencimento) {
        this.cod = cod;
        this.diaVencimento = diaVencimento;
    }

    public int getCod() {
        return this.cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getDiaVencimento() {
        return this.diaVencimento;
    }

    public void setDiaVencimento(int diaVencimento) {
        this.diaVencimento = diaVencimento;
    }
}
