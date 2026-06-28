package br.pucrs.poo.trabalho2.dados;

public class ClienteCorporativo extends Cliente {
    private String cnpj;
    private String nomeFantasia;

    public ClienteCorporativo(int numero, String nome, String email, String cnpj, String nomeFantasia) {
        super(numero, nome, email);
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeFantasia() {
        return this.nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }    

    @Override
    public String descrever() {
        return getNumero() + ";" 
                + getNome() + ";"
                + getEmail() + ";"
                + getCnpj() + ";"
                + getNomeFantasia();
    }
}
