package br.pucrs.poo.trabalho2.dados;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public class Clientes {
    private List<Cliente> clientes = new ArrayList<>();

    public boolean adicionar(Cliente c){
        if (buscar(c.getNumero()) == null){
            clientes.add(c);
            clientes.sort(Comparator.comparingInt(cl -> cl.getNumero()));
            return true;
        } 
        return false;
    }

    public boolean remover(Cliente c){
        return clientes.remove(c);
    }

    public boolean remover(int numero){
        return clientes.remove(buscar(numero));
    }

    public Cliente buscar(int numero){
        for (Cliente cliente : clientes) {
            if (cliente.getNumero() == numero) return cliente;
        }
        return null;
    }

    public List<Cliente> getCopia(){
        List<Cliente> copia = new ArrayList<>(clientes.size());
        for (Cliente c : clientes) {
            copia.add(c);
        }
        return copia;
    }
}
