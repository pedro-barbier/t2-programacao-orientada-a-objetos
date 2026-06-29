package br.pucrs.poo.trabalho2.app;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Relatório de Clientes")
@Route("relatorio-clientes")
public class RelatorioClientes extends VerticalLayout {
    public RelatorioClientes() {
        Text titulo = new Text("Relatório de Clientes");
        add(titulo);
    }
}
