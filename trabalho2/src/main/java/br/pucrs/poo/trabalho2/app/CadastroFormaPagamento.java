package br.pucrs.poo.trabalho2.app;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Cadastro de Formas de Pagamento")
@Route("cadastro-forma-pagamento")
public class CadastroFormaPagamento extends VerticalLayout {
    public CadastroFormaPagamento() {
        Text titulo = new Text("Cadastro de Formas de Pagamento");
        add(titulo);
    }
}
