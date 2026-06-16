package br.pucrs.poo.trabalho2.gui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Exemplo de múltiplas telas")
@Route("")
public class MainView extends VerticalLayout {
   public MainView() {
      Button tela1Button = new Button("Cadastro de pessoas");
      Button tela2Button = new Button("Edição de pessoas");
      Button tela3Button = new Button("Remoção de pessoas");
      add(tela1Button);
      add(tela2Button);
      add(tela3Button);
   }
}