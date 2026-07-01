package br.pucrs.poo.trabalho2.app;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import br.pucrs.poo.trabalho2.dados.Jogos;
import br.pucrs.poo.trabalho2.dados.Jogo;
import java.util.List;

@PageTitle("Relatório de Jogos")
@Route("relatorio-jogos")
public class RelatorioJogos extends VerticalLayout {
    public RelatorioJogos() {
        Text titulo = new Text("Relatório de Jogos");
        add(titulo);

        // Obtém os jogos da sessão
        Jogos jogos = VaadinSession.getCurrent().getAttribute(Jogos.class);
        List<Jogo> listaJogos = jogos != null ? jogos.getCopia() : List.of();

        // Verifica se não há jogos cadastrados
        if (listaJogos.isEmpty()) {
            Text erro = new Text("Nenhum jogo cadastrado!");
            erro.getStyle().set("color", "red").set("font-size", "16px");
            add(erro);
        } else {
            // Cria o Grid
            Grid<Jogo> jogoGrid = new Grid<>(Jogo.class, false);
            jogoGrid.addColumn(jogo -> jogo.getCodigo()).setHeader("Código").setAutoWidth(true);
            jogoGrid.addColumn(jogo -> jogo.getNome()).setHeader("Nome").setAutoWidth(true);
            jogoGrid.addColumn(jogo -> jogo.getAno()).setHeader("Ano").setAutoWidth(true);
            jogoGrid.addColumn(jogo -> jogo.getValorDiario()).setHeader("Valor Diário").setAutoWidth(true);
            jogoGrid.addColumn(jogo -> jogo.getCategoria().getDescricao()).setHeader("Categoria").setAutoWidth(true);
            jogoGrid.addColumn(jogo -> jogo.getContrato() != null ? "Sim" : "Não").setHeader("Contratado").setAutoWidth(true);

            jogoGrid.setItems(listaJogos);
            jogoGrid.setHeight("600px");
            jogoGrid.setWidthFull();
            add(jogoGrid);
        }

        // Botão para voltar para a página inicial
        Button voltarBtn = new Button("Voltar", e -> {
            UI.getCurrent().navigate("");
        });
        voltarBtn.getStyle().set("margin-top", "20px");

        HorizontalLayout botoesLayout = new HorizontalLayout(voltarBtn);
        add(botoesLayout);
    }
}
