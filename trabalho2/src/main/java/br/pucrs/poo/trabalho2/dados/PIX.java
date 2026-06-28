package br.pucrs.poo.trabalho2.dados;

public class PIX extends FormaPagamento {
    private String chave;

    public PIX(int cod, int diaVencimento, String chave) {
        super(cod, diaVencimento);
        this.chave = chave;
    }

    public String getChave() {
        return this.chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }
}
