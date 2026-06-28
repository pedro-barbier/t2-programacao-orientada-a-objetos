package br.pucrs.poo.trabalho2.dados;

import java.util.ArrayList;
import java.util.List;

public class Jogos {
    private List<Jogo> jogos = new ArrayList<>();

    public boolean adicionar(Jogo jogo){
        if (buscar(jogo.getCodigo()) == null){
            jogos.add(jogo);
            return true;
        } 
        return false;
    }

    public boolean remover(Jogo jogo){
        return jogos.remove(jogo);
    }

    public boolean remover(int codigo){
        return jogos.remove(buscar(codigo));
    }

    public Jogo buscar(int codigo){
        for (Jogo jogo : jogos) {
            if (jogo.getCodigo() == codigo) return jogo;
        }
        return null;
    }

    public List<Jogo> buscar(Categoria categoria){
        List<Jogo> temp = new ArrayList<>();
        for (Jogo jogo : jogos) {
            if (jogo.getCategoria().equals(categoria)) {
                temp.add(jogo);
            }
        }
        return temp;
    }

    public List<Jogo> getCopia(){
        List<Jogo> copia = new ArrayList<>(jogos.size());
        for (Jogo j : jogos) {
            copia.add(j);
        }
        return copia;
    }
}

