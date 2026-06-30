package br.pucrs.poo.trabalho2.app;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import br.pucrs.poo.trabalho2.dados.Cliente;
import br.pucrs.poo.trabalho2.dados.ClienteCorporativo;
import br.pucrs.poo.trabalho2.dados.ClienteIndividual;
import br.pucrs.poo.trabalho2.dados.Clientes;

@PageTitle("Cadastro de Clientes")
@Route("cadastro-cliente")
public class CadastroCliente extends VerticalLayout {
    public CadastroCliente() {
        Text titulo = new Text("Cadastro de Clientes");

        TextField numeroField = new TextField("Número");
        TextField nomeField = new TextField("Nome");
        EmailField emailField = new EmailField("Email");
        emailField.setClearButtonVisible(true);

        RadioButtonGroup<String> tipoClienteGroup = new RadioButtonGroup<>();
        tipoClienteGroup.setLabel("Tipo de Cliente");
        tipoClienteGroup.setItems("Corporativo", "Individual");

        TextField cnpjField = new TextField("CNPJ");
        TextField nomeFantasiaField = new TextField("Nome Fantasia");
        TextField cpfField = new TextField("CPF");

        cnpjField.setVisible(false);
        nomeFantasiaField.setVisible(false);
        cpfField.setVisible(false);

        tipoClienteGroup.addValueChangeListener(event -> {
            String tipo = event.getValue();
            boolean corporativo = "Corporativo".equals(tipo);
            cnpjField.setVisible(corporativo);
            nomeFantasiaField.setVisible(corporativo);
            cpfField.setVisible(!corporativo);
        });

        Clientes clientes = VaadinSession.getCurrent().getAttribute(Clientes.class);

        Button voltarButton = new Button("Voltar", e -> UI.getCurrent().navigate(""));
        Button salvarButton = new Button("Salvar", event -> {
            if (clientes == null) {
                Notification.show("Repositório de clientes indisponível.", 3000, Notification.Position.MIDDLE);
                return;
            }

            boolean valido = true;
            numeroField.setInvalid(false);
            nomeField.setInvalid(false);
            emailField.setInvalid(false);
            cnpjField.setInvalid(false);
            nomeFantasiaField.setInvalid(false);
            cpfField.setInvalid(false);

            String numeroValue = numeroField.getValue().trim();
            String nomeValue = nomeField.getValue().trim();
            String emailValue = emailField.getValue().trim();
            String tipoValue = tipoClienteGroup.getValue();
            String cnpjValue = cnpjField.getValue().trim();
            String nomeFantasiaValue = nomeFantasiaField.getValue().trim();
            String cpfValue = cpfField.getValue().trim();

            if (numeroValue.isEmpty()) {
                numeroField.setErrorMessage("O número é obrigatório.");
                numeroField.setInvalid(true);
                valido = false;
            }

            if (nomeValue.isEmpty()) {
                nomeField.setErrorMessage("O nome é obrigatório.");
                nomeField.setInvalid(true);
                valido = false;
            }

            if (emailValue.isEmpty()) {
                emailField.setErrorMessage("O email é obrigatório.");
                emailField.setInvalid(true);
                valido = false;
            }

            if (tipoValue == null) {
                Notification.show("Escolha Corporativo ou Individual.", 3000, Notification.Position.MIDDLE);
                valido = false;
            }

            if (tipoValue != null && tipoValue.equals("Corporativo")) {
                if (cnpjValue.isEmpty()) {
                    cnpjField.setErrorMessage("O CNPJ é obrigatório.");
                    cnpjField.setInvalid(true);
                    valido = false;
                }
                if (nomeFantasiaValue.isEmpty()) {
                    nomeFantasiaField.setErrorMessage("O nome fantasia é obrigatório.");
                    nomeFantasiaField.setInvalid(true);
                    valido = false;
                }
            } else if (tipoValue != null && tipoValue.equals("Individual")) {
                if (cpfValue.isEmpty()) {
                    cpfField.setErrorMessage("O CPF é obrigatório.");
                    cpfField.setInvalid(true);
                    valido = false;
                }
            }

            int numero = -1;
            if (valido) {
                try {
                    numero = Integer.parseInt(numeroValue);
                } catch (NumberFormatException ex) {
                    numeroField.setErrorMessage("Número deve ser um inteiro válido.");
                    numeroField.setInvalid(true);
                    valido = false;
                }
            }

            if (valido) {
                if (clientes.buscar(numero) != null) {
                    numeroField.setErrorMessage("Este número já está cadastrado.");
                    numeroField.setInvalid(true);
                    Notification.show("Número já cadastrado no sistema.", 3000, Notification.Position.MIDDLE);
                    valido = false;
                }
            }

            if (valido) {
                Cliente novoCliente;
                if (tipoValue.equals("Corporativo")) {
                    novoCliente = new ClienteCorporativo(numero, nomeValue, emailValue, cnpjValue, nomeFantasiaValue);
                } else {
                    novoCliente = new ClienteIndividual(numero, nomeValue, emailValue, cpfValue);
                }
                boolean cadastrado = clientes.adicionar(novoCliente);
                if (cadastrado) {
                    Notification.show("Cliente cadastrado com sucesso!", 3000, Notification.Position.MIDDLE);
                    numeroField.clear();
                    nomeField.clear();
                    emailField.clear();
                    tipoClienteGroup.setValue(null);
                    cnpjField.clear();
                    nomeFantasiaField.clear();
                    cpfField.clear();
                    cnpjField.setVisible(false);
                    nomeFantasiaField.setVisible(false);
                    cpfField.setVisible(false);
                } else {
                    numeroField.setErrorMessage("Este número já está cadastrado.");
                    numeroField.setInvalid(true);
                    Notification.show("Não foi possível cadastrar: número já existe.", 3000, Notification.Position.MIDDLE);
                }
            }
        });

        HorizontalLayout botoes = new HorizontalLayout(voltarButton, salvarButton);

        add(titulo,
            numeroField,
            nomeField,
            emailField,
            tipoClienteGroup,
            cnpjField,
            nomeFantasiaField,
            cpfField,
            botoes);
    }
}
