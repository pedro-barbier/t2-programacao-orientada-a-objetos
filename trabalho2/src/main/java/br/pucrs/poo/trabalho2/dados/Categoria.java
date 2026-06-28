package br.pucrs.poo.trabalho2.dados;

public enum Categoria { 
    AVENTURA("AVENTURA"),
    ESTRATEGIA("ESTRATEGIA"),
    CORRIDA("CORRIDA");

    private String nome;

    private Categoria (String nome){
        this.nome = nome;
    }

    public String getDescricao() {
        return nome;
    }
}
