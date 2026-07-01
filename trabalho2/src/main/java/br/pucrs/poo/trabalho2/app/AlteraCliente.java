package br.pucrs.poo.trabalho2.app;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import br.pucrs.poo.trabalho2.dados.Cliente;
import br.pucrs.poo.trabalho2.dados.ClienteCorporativo;
import br.pucrs.poo.trabalho2.dados.ClienteIndividual;
import br.pucrs.poo.trabalho2.dados.Clientes;

@PageTitle("Alterar Cliente")
@Route("alterar-cliente")
public class AlteraCliente extends VerticalLayout {
    public AlteraCliente() {
        Text titulo = new Text("Alterar Cliente");
        add(titulo);

        Clientes clientes = VaadinSession.getCurrent().getAttribute(Clientes.class);
        if (clientes == null) {
            clientes = new Clientes();
            VaadinSession.getCurrent().setAttribute(Clientes.class, clientes);
        }

        Button voltarButton = new Button("Voltar para página inicial", e -> UI.getCurrent().navigate(""));
        add(voltarButton);

        H4 selecioneClienteTitulo = new H4("Clientes Cadastrados");
        Grid<Cliente> clienteGrid = new Grid<>(Cliente.class, false);
        clienteGrid.addColumn(cliente -> cliente.getNumero()).setHeader("Número").setAutoWidth(true);
        clienteGrid.addColumn(cliente -> cliente.getNome()).setHeader("Nome").setAutoWidth(true);
        clienteGrid.addColumn(cliente -> cliente.getEmail()).setHeader("Email").setAutoWidth(true);
        clienteGrid.addColumn(cliente -> cliente instanceof ClienteCorporativo ? "Corporativo" : "Individual")
                .setHeader("Tipo").setAutoWidth(true);
        clienteGrid.setItems(clientes.getCopia());
        clienteGrid.setHeight("150px");

        ComboBox<Cliente> clientesBox = new ComboBox<>("Cliente");
        clientesBox.setItems(clientes.getCopia());
        clientesBox.setItemLabelGenerator(cliente -> cliente.getNumero() + " - " + cliente.getNome());
        clientesBox.setPlaceholder("Selecione um cliente");
        clientesBox.setWidth("400px");

        TextField nomeField = new TextField("Nome");
        EmailField emailField = new EmailField("Email");
        TextField cpfField = new TextField("CPF");
        TextField cnpjField = new TextField("CNPJ");
        TextField nomeFantasiaField = new TextField("Nome Fantasia");
        cpfField.setVisible(false);
        cnpjField.setVisible(false);
        nomeFantasiaField.setVisible(false);

        Span mensagem = new Span();
        mensagem.getStyle().set("color", "var(--lumo-error-text-color)");

        Button salvarButton = new Button("Salvar alterações", e -> {
            Cliente clienteSelecionado = clientesBox.getValue();
            if (clienteSelecionado == null) {
                Notification.show("Selecione um cliente para alterar.", 3000, Notification.Position.MIDDLE);
                return;
            }

            nomeField.setInvalid(false);
            emailField.setInvalid(false);
            cpfField.setInvalid(false);
            cnpjField.setInvalid(false);
            nomeFantasiaField.setInvalid(false);

            String nome = nomeField.getValue() == null ? "" : nomeField.getValue().trim();
            String email = emailField.getValue() == null ? "" : emailField.getValue().trim();

            if (nome.isEmpty()) {
                nomeField.setErrorMessage("O nome é obrigatório.");
                nomeField.setInvalid(true);
                return;
            }

            if (email.isEmpty()) {
                emailField.setErrorMessage("O email é obrigatório.");
                emailField.setInvalid(true);
                return;
            }

            if (clienteSelecionado instanceof ClienteIndividual) {
                String cpf = cpfField.getValue() == null ? "" : cpfField.getValue().trim();
                if (cpf.isEmpty()) {
                    cpfField.setErrorMessage("O CPF é obrigatório.");
                    cpfField.setInvalid(true);
                    return;
                }
                ((ClienteIndividual) clienteSelecionado).setCpf(cpf);
            } else if (clienteSelecionado instanceof ClienteCorporativo) {
                String cnpj = cnpjField.getValue() == null ? "" : cnpjField.getValue().trim();
                String nomeFantasia = nomeFantasiaField.getValue() == null ? "" : nomeFantasiaField.getValue().trim();
                if (cnpj.isEmpty()) {
                    cnpjField.setErrorMessage("O CNPJ é obrigatório.");
                    cnpjField.setInvalid(true);
                    return;
                }
                if (nomeFantasia.isEmpty()) {
                    nomeFantasiaField.setErrorMessage("O nome fantasia é obrigatório.");
                    nomeFantasiaField.setInvalid(true);
                    return;
                }
                ((ClienteCorporativo) clienteSelecionado).setCnpj(cnpj);
                ((ClienteCorporativo) clienteSelecionado).setNomeFantasia(nomeFantasia);
            }

            clienteSelecionado.setNome(nome);
            clienteSelecionado.setEmail(email);
            Notification.show("Cliente atualizado com sucesso!", 3000, Notification.Position.MIDDLE);
            mensagem.setText("");

            UI.getCurrent().getPage().reload();
        });

        clientesBox.addValueChangeListener(event -> {
            Cliente clienteSelecionado = event.getValue();
            if (clienteSelecionado == null) {
                nomeField.clear();
                emailField.clear();
                cpfField.clear();
                cnpjField.clear();
                nomeFantasiaField.clear();
                cpfField.setVisible(false);
                cnpjField.setVisible(false);
                nomeFantasiaField.setVisible(false);
                return;
            }

            nomeField.setValue(clienteSelecionado.getNome() == null ? "" : clienteSelecionado.getNome());
            emailField.setValue(clienteSelecionado.getEmail() == null ? "" : clienteSelecionado.getEmail());

            if (clienteSelecionado instanceof ClienteIndividual) {
                ClienteIndividual clienteIndividual = (ClienteIndividual) clienteSelecionado;
                cpfField.setVisible(true);
                cnpjField.setVisible(false);
                nomeFantasiaField.setVisible(false);
                cpfField.setValue(clienteIndividual.getCpf() == null ? "" : clienteIndividual.getCpf());
                cnpjField.clear();
                nomeFantasiaField.clear();
            } else if (clienteSelecionado instanceof ClienteCorporativo) {
                ClienteCorporativo clienteCorporativo = (ClienteCorporativo) clienteSelecionado;
                cpfField.setVisible(false);
                cnpjField.setVisible(true);
                nomeFantasiaField.setVisible(true);
                cnpjField.setValue(clienteCorporativo.getCnpj() == null ? "" : clienteCorporativo.getCnpj());
                nomeFantasiaField.setValue(clienteCorporativo.getNomeFantasia() == null ? "" : clienteCorporativo.getNomeFantasia());
                cpfField.clear();
            }
        });

        if (clientes.getCopia().isEmpty()) {
            mensagem.setText("Não existem clientes cadastrados no sistema.");
            clientesBox.setEnabled(false);
            salvarButton.setEnabled(false);
            nomeField.setEnabled(false);
            emailField.setEnabled(false);
            cpfField.setEnabled(false);
            cnpjField.setEnabled(false);
            nomeFantasiaField.setEnabled(false);
        }

        add(selecioneClienteTitulo, clienteGrid, clientesBox, mensagem, nomeField, emailField, cpfField, cnpjField, nomeFantasiaField, salvarButton);
    }
}
