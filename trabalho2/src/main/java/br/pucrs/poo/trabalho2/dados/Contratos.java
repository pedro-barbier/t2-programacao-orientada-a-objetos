package br.pucrs.poo.trabalho2.dados;

import java.util.ArrayList;
import java.util.List;

public class Contratos {
    private List<Contrato> contratos = new ArrayList<>();

    public boolean adicionar(Contrato contrato){
        if (buscar(contrato.getId()) == null && buscar(contrato.getJogo()).isEmpty()){
            contratos.add(contrato);
            return true;
        }
        return false;
    }

    public boolean remover(Contrato contrato){
        return contratos.remove(contrato);
    }

    public boolean remover(int id){
        return contratos.remove(buscar(id));
    }

    public Contrato buscar(int id){
        for (Contrato c : contratos) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public List<Contrato> buscar(Cliente cliente){
        List<Contrato> temp = new ArrayList<>();
        for (Contrato c : contratos) {
            if (c.getCliente().equals(cliente)) {
                temp.add(c);
            }
        }
        return temp;
    }
    
    public List<Contrato> buscar(Jogo jogo){
        List<Contrato> temp = new ArrayList<>();
        for (Contrato c : contratos) {
            if (c.getJogo().equals(jogo)) {
                temp.add(c);
            }
        }
        return temp;
    }

    public List<Contrato> getCopia(){
        List<Contrato> copia = new ArrayList<>(contratos.size());
        for (Contrato c : contratos) {
            copia.add(c);
        }
        return copia;
    }
}
