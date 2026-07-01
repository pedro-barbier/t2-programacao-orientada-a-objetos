package br.pucrs.poo.trabalho2.app;

import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import br.pucrs.poo.trabalho2.dados.Contrato;
import br.pucrs.poo.trabalho2.dados.Contratos;

@PageTitle("Remover Contrato")
@Route("remover-contrato")
public class RemoverContrato extends VerticalLayout {
    public RemoverContrato() {
        Text titulo = new Text("Remover Contrato");
        add(titulo);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/yyyy");

        Contratos contratos = VaadinSession.getCurrent().getAttribute(Contratos.class);

        Grid<Contrato> contratoGrid = new Grid<>(Contrato.class, false);
        contratoGrid.addColumn(contrato -> contrato.getId()).setHeader("ID").setAutoWidth(true);
        contratoGrid.addColumn(contrato -> contrato.getData().format(formatador)).setHeader("Data").setAutoWidth(true);
        contratoGrid.addColumn(contrato -> contrato.getPeriodo()).setHeader("Período").setAutoWidth(true);
        contratoGrid.addColumn(contrato -> contrato.getCliente().getNome()).setHeader("Cliente").setAutoWidth(true);
        contratoGrid.addColumn(contrato -> contrato.getJogo().getNome()).setHeader("Jogo").setAutoWidth(true);
        contratoGrid.addColumn(contrato -> contrato.getFormaPagamento().getCod()).setHeader("Forma de Pagamento").setAutoWidth(true);
        contratoGrid.addColumn(contrato -> contrato.calculaValorFinal()).setHeader("Valor Final").setAutoWidth(true);
        contratoGrid.setItems(contratos != null ? contratos.getCopia() : new java.util.ArrayList<>());
        contratoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        contratoGrid.setHeight("300px");

        Button voltarButton = new Button("Voltar", e -> UI.getCurrent().navigate(""));

        Button removerButton = new Button("Remover", e -> {
            Contrato contratoSelecionado = contratoGrid.asSingleSelect().getValue();
            if (contratoSelecionado == null) {
                Notification.show("Selecione um contrato da lista antes de remover.", 3000, Notification.Position.MIDDLE);
                return;
            }

            Dialog dialogo = new Dialog();
            dialogo.setCloseOnOutsideClick(false);

            Text pergunta = new Text("Tem certeza que deseja remover o contrato selecionado?");

            Button cancelarBtn = new Button("Cancelar", ev -> {
                contratoGrid.asSingleSelect().clear();
                dialogo.close();
            });

            Button confirmarBtn = new Button("Remover", ev -> {
                contratos.remover(contratoSelecionado);
                contratoGrid.setItems(contratos.getCopia());
                contratoGrid.asSingleSelect().clear();
                Notification.show("Contrato removido com sucesso.", 3000, Notification.Position.MIDDLE);
                dialogo.close();
            });

            HorizontalLayout botoesDialogo = new HorizontalLayout(cancelarBtn, confirmarBtn);
            VerticalLayout conteudoDialogo = new VerticalLayout(pergunta, botoesDialogo);
            dialogo.add(conteudoDialogo);
            dialogo.open();
        });

        HorizontalLayout botoes = new HorizontalLayout(voltarButton, removerButton);

        add(contratoGrid, botoes);
    }
}
