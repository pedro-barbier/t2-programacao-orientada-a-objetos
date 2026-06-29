package br.pucrs.poo.trabalho2.app;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Alterar Cliente")
@Route("alterar-cliente")
public class AlteraCliente extends VerticalLayout {
    public AlteraCliente() {
        Text titulo = new Text("Alterar Cliente");
        add(titulo);
    }
}
