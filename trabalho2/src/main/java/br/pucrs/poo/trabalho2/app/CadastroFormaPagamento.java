package br.pucrs.poo.trabalho2.app;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;


import br.pucrs.poo.trabalho2.dados.FormaPagamento;
import br.pucrs.poo.trabalho2.dados.CartaoCredito;
import br.pucrs.poo.trabalho2.dados.Cliente;
import br.pucrs.poo.trabalho2.dados.ClienteCorporativo;
import br.pucrs.poo.trabalho2.dados.Clientes;
import br.pucrs.poo.trabalho2.dados.PIX;


@PageTitle("Cadastro de Formas de Pagamento")
@Route("cadastro-forma-pagamento")
public class CadastroFormaPagamento extends VerticalLayout {
    public CadastroFormaPagamento() {
        Text titulo = new Text("Cadastro de Formas de Pagamento");
        add(titulo);

        H4 selecioneClienteTitulo = new H4("Selecione um Cliente");

        TextField codField = new TextField("Código");
        TextField diaDeVencimentoField = new TextField("Dia de Vencimento");

        RadioButtonGroup<String> tipoPagamentoGroup = new RadioButtonGroup<>();
        tipoPagamentoGroup.setLabel("Tipo de Pagamento");
        tipoPagamentoGroup.setItems("Cartão de Crédito", "PIX");

        TextField numeroField = new TextField("Número");
        DatePicker validadeField = new DatePicker("Validade");
        TextField chaveField = new TextField("Chave");

        numeroField.setVisible(false);
        validadeField.setVisible(false);
        chaveField.setVisible(false);

        tipoPagamentoGroup.addValueChangeListener(event -> {
            String tipo = event.getValue();
            boolean cartaoCredito = "Cartão de Crédito".equals(tipo);
            numeroField.setVisible(cartaoCredito);
            validadeField.setVisible(cartaoCredito);
            chaveField.setVisible(!cartaoCredito);
        });

        VerticalLayout formularioPagamento = new VerticalLayout(
                codField,
                diaDeVencimentoField,
                tipoPagamentoGroup,
                numeroField,
                validadeField,
                chaveField
        );

        formularioPagamento.setPadding(false);
        formularioPagamento.setSpacing(true);
        formularioPagamento.setVisible(false);

        Clientes clientes = VaadinSession.getCurrent().getAttribute(Clientes.class);

        Grid<Cliente> clienteGrid = new Grid<>(Cliente.class, false);
        clienteGrid.addColumn(cliente -> cliente.getNumero()).setHeader("Número").setAutoWidth(true);
        clienteGrid.addColumn(cliente -> cliente.getNome()).setHeader("Nome").setAutoWidth(true);
        clienteGrid.addColumn(cliente -> cliente.getEmail()).setHeader("Email").setAutoWidth(true);
        clienteGrid.addColumn(cliente -> cliente instanceof ClienteCorporativo ? "Corporativo" : "Individual")
                .setHeader("Tipo").setAutoWidth(true);
        clienteGrid.setItems(clientes != null ? clientes.getCopia() : new java.util.ArrayList<>());
        clienteGrid.setHeight("200px");

        ComboBox<Cliente> clientesComboBox = new ComboBox<>("Cliente");
        clientesComboBox.setItems(clientes.getCopia());
        clientesComboBox.setItemLabelGenerator(cliente ->
        cliente.getNumero() + " - " + cliente.getNome());
        clientesComboBox.setPlaceholder("Selecione um cliente");
        clientesComboBox.setWidth("400px");

        clientesComboBox.addValueChangeListener(event -> {
            Cliente clienteSelecionado = event.getValue();

            if (clienteSelecionado != null) {
                formularioPagamento.setVisible(true);
            } else {
                formularioPagamento.setVisible(false);

                codField.clear();
                diaDeVencimentoField.clear();
                tipoPagamentoGroup.clear();
                numeroField.clear();
                validadeField.clear();
                chaveField.clear();

                numeroField.setVisible(false);
                validadeField.setVisible(false);
                chaveField.setVisible(false);
            }
        });

        if (clientes.getCopia().isEmpty()) {
                clientesComboBox.setEnabled(false);
                Notification.show("Nenhum Cliente Cadastrado.", 3000, Notification.Position.MIDDLE);
        }

        Button voltarButton = new Button("Voltar", e -> UI.getCurrent().navigate(""));

        Button salvarButton = new Button("Salvar", event -> {

            Cliente clienteSelecionado = clientesComboBox.getValue();
            if (clienteSelecionado == null) {
                Notification.show("Selecione um cliente da lista antes de salvar.", 3000, Notification.Position.MIDDLE);
                return;
            }


            boolean valido = true;
            codField.setInvalid(false);
            diaDeVencimentoField.setInvalid(false);
            numeroField.setInvalid(false);
            validadeField.setInvalid(false);
            chaveField.setInvalid(false);

            String codValue = codField.getValue().trim();
            String diaDeVencimentoValue = diaDeVencimentoField.getValue().trim();
            String numeroValue = numeroField.getValue().trim();
            String tipoValue = tipoPagamentoGroup.getValue();
            LocalDate validadeValue = validadeField.getValue();
            String chaveValue = chaveField.getValue().trim();
            

            if (codValue.isEmpty()) {
                codField.setErrorMessage("O código é obrigatório.");
                codField.setInvalid(true);
                valido = false;
            }

            if (diaDeVencimentoValue.isEmpty()) {
                diaDeVencimentoField.setErrorMessage("O dia de vencimento é obrigatório.");
                diaDeVencimentoField.setInvalid(true);
                valido = false;
            }


            if (tipoValue == null) {
                Notification.show("Escolha Cartão de Crédito ou PIX.", 3000, Notification.Position.MIDDLE);
                valido = false;
            }

            if (tipoValue != null && tipoValue.equals("Cartão de Crédito")) {
                if (numeroValue.isEmpty()) {
                    numeroField.setErrorMessage("O número é obrigatório.");
                    numeroField.setInvalid(true);
                    valido = false;
                }
                if (validadeValue == null) {
                    validadeField.setErrorMessage("A validade é obrigatória.");
                    validadeField.setInvalid(true);
                    valido = false;
                }
            } else if (tipoValue != null && tipoValue.equals("PIX")) {
                if (chaveValue.isEmpty()) {
                    chaveField.setErrorMessage("A chave é obrigatória.");
                    chaveField.setInvalid(true);
                    valido = false;
                }
            }

            int cod = -1;
            int diaVencimento = -1;
            LocalDate validade = null;
            if (valido) {
                try {
                    cod = Integer.parseInt(codValue);
                } catch (NumberFormatException ex) {
                    codField.setErrorMessage("Código deve ser um inteiro válido.");
                    codField.setInvalid(true);
                    valido = false;
                }
                try {
                    diaVencimento = Integer.parseInt(diaDeVencimentoValue);
                } catch (NumberFormatException ex) {
                    diaDeVencimentoField.setErrorMessage("O dia de vencimento deve ser um inteiro válido.");
                    diaDeVencimentoField.setInvalid(true);
                    valido = false;
                }
                try {
                    validade = validadeValue;
                } catch (DateTimeParseException ex) {
                    validadeField.setErrorMessage("A validade deve ser uma data válida.");
                    validadeField.setInvalid(true);
                    valido = false;
                }
            }

            if (valido) {
                if (clientes.buscarFormasDePagamento(cod) != null) {
                    codField.setErrorMessage("Este código já está cadastrado.");
                    codField.setInvalid(true);
                    Notification.show("Código já cadastrado no sistema.", 3000, Notification.Position.MIDDLE);
                    valido = false;
                }
            }

            if (valido) {
                FormaPagamento novaFormaPagamento = "Cartão de Crédito".equals(tipoValue)
                        ? new CartaoCredito(cod, diaVencimento, numeroValue, validade)
                        : new PIX(cod, diaVencimento, chaveValue);
                boolean cadastrado = novaFormaPagamento != null;
                if (cadastrado) {
                    clienteSelecionado.addFormaPagamento(novaFormaPagamento);
                    Notification.show("Forma de Pagamento cadastrada com sucesso para " + clienteSelecionado.getNome() + "!", 3000, Notification.Position.MIDDLE);
                    codField.clear();
                    diaDeVencimentoField.clear();
                    tipoPagamentoGroup.clear();
                    numeroField.setValue(null);
                    validadeField.clear();
                    chaveField.clear();
                    clientesComboBox.clear();
                    numeroField.setVisible(false);
                    validadeField.setVisible(false);
                    chaveField.setVisible(false);
                } else {
                    codField.setErrorMessage("Este código já está cadastrado.");
                    codField.setInvalid(true);
                    Notification.show("Não foi possível cadastrar: código já existe.", 3000, Notification.Position.MIDDLE);
                }
            }
        }); 

        HorizontalLayout botoes = new HorizontalLayout(voltarButton, salvarButton);

        add(
            titulo,
            selecioneClienteTitulo,
            clienteGrid,
            clientesComboBox,
            formularioPagamento,
            botoes
        );
    }
}
