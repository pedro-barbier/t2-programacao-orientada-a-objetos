package br.pucrs.poo.trabalho2.app;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import br.pucrs.poo.trabalho2.dados.Categoria;
import br.pucrs.poo.trabalho2.dados.Jogo;
import br.pucrs.poo.trabalho2.dados.Jogos;

@PageTitle("Cadastro de Jogos")
@Route("cadastro-jogo")
public class CadastroJogo extends VerticalLayout {
    public CadastroJogo() {
        Text titulo = new Text("Cadastro de Jogos");

        TextField codigoField = new TextField("Código");
        TextField nomeField = new TextField("Nome");
        TextField anoField = new TextField("Ano");
        TextField valorDiarioField = new TextField("Valor Diário");
        TextField categoriaField = new TextField("Categoria");

        StringBuilder categoriasDisponiveis = new StringBuilder();
        for (Categoria categoria : Categoria.values()) {
            if (categoriasDisponiveis.length() > 0) {
                categoriasDisponiveis.append(", ");
            }
            categoriasDisponiveis.append(categoria.getDescricao());
        }
        Text categoriasText = new Text("Categorias cadastradas: " + categoriasDisponiveis);

        Jogos jogos = VaadinSession.getCurrent().getAttribute(Jogos.class);

        Button voltarButton = new Button("Voltar", e -> UI.getCurrent().navigate(""));
        Button salvarButton = new Button("Salvar", event -> {
            if (jogos == null) {
                Notification.show("Repositório de jogos indisponível.", 3000, Notification.Position.MIDDLE);
                return;
            }

            boolean valido = true;
            codigoField.setInvalid(false);
            nomeField.setInvalid(false);
            anoField.setInvalid(false);
            valorDiarioField.setInvalid(false);
            categoriaField.setInvalid(false);

            String codigoValue = codigoField.getValue().trim();
            String nomeValue = nomeField.getValue().trim();
            String anoValue = anoField.getValue().trim();
            String valorDiarioValue = valorDiarioField.getValue().trim();
            String categoriaValue = categoriaField.getValue().trim();

            if (codigoValue.isEmpty()) {
                codigoField.setErrorMessage("O código é obrigatório.");
                codigoField.setInvalid(true);
                valido = false;
            }
            if (nomeValue.isEmpty()) {
                nomeField.setErrorMessage("O nome é obrigatório.");
                nomeField.setInvalid(true);
                valido = false;
            }
            if (anoValue.isEmpty()) {
                anoField.setErrorMessage("O ano é obrigatório.");
                anoField.setInvalid(true);
                valido = false;
            }
            if (valorDiarioValue.isEmpty()) {
                valorDiarioField.setErrorMessage("O valor diário é obrigatório.");
                valorDiarioField.setInvalid(true);
                valido = false;
            }
            if (categoriaValue.isEmpty()) {
                categoriaField.setErrorMessage("A categoria é obrigatória.");
                categoriaField.setInvalid(true);
                valido = false;
            }

            int codigo = -1;
            int ano = -1;
            double valorDiario = -1;
            Categoria categoria = null;

            if (valido) {
                try {
                    codigo = Integer.parseInt(codigoValue);
                } catch (NumberFormatException ex) {
                    codigoField.setErrorMessage("Código deve ser um inteiro válido.");
                    codigoField.setInvalid(true);
                    valido = false;
                }
            }

            if (valido) {
                try {
                    ano = Integer.parseInt(anoValue);
                } catch (NumberFormatException ex) {
                    anoField.setErrorMessage("Ano deve ser um inteiro válido.");
                    anoField.setInvalid(true);
                    valido = false;
                }
            }

            if (valido) {
                try {
                    valorDiario = Double.parseDouble(valorDiarioValue);
                } catch (NumberFormatException ex) {
                    valorDiarioField.setErrorMessage("Valor diário deve ser um número válido.");
                    valorDiarioField.setInvalid(true);
                    valido = false;
                }
            }

            if (valido) {
                try {
                    categoria = Categoria.valueOf(categoriaValue.toUpperCase());
                } catch (IllegalArgumentException ex) {
                    categoriaField.setErrorMessage("Categoria inválida. Use uma das listadas acima.");
                    categoriaField.setInvalid(true);
                    Notification.show("Categoria informada não existe.", 3000, Notification.Position.MIDDLE);
                    valido = false;
                }
            }

            if (valido && jogos.buscar(codigo) != null) {
                codigoField.setErrorMessage("Este código já está cadastrado.");
                codigoField.setInvalid(true);
                Notification.show("Código já cadastrado no sistema.", 3000, Notification.Position.MIDDLE);
                valido = false;
            }

            if (valido) {
                Jogo novoJogo = new Jogo(codigo, nomeValue, ano, valorDiario, categoria);
                boolean cadastrado = jogos.adicionar(novoJogo);
                if (cadastrado) {
                    Notification.show("Jogo cadastrado com sucesso!", 3000, Notification.Position.MIDDLE);
                    codigoField.clear();
                    nomeField.clear();
                    anoField.clear();
                    valorDiarioField.clear();
                    categoriaField.clear();
                } else {
                    codigoField.setErrorMessage("Este código já está cadastrado.");
                    codigoField.setInvalid(true);
                    Notification.show("Não foi possível cadastrar: código já existe.", 3000, Notification.Position.MIDDLE);
                }
            }
        });

        HorizontalLayout botoes = new HorizontalLayout(voltarButton, salvarButton);

        add(titulo,
            codigoField,
            nomeField,
            anoField,
            valorDiarioField,
            categoriasText,
            categoriaField,
            botoes);
    }
}
