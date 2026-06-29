package br.pucrs.poo.trabalho2.app;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Cadastro de Jogos")
@Route("cadastro-jogo")
public class CadastroJogo extends VerticalLayout {
    public CadastroJogo() {
        Text titulo = new Text("Cadastro de Jogos");
        add(titulo);
    }
}
