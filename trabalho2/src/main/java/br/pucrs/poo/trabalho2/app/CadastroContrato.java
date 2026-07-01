package br.pucrs.poo.trabalho2.app;

import java.time.LocalDate;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import br.pucrs.poo.trabalho2.dados.CartaoCredito;
import br.pucrs.poo.trabalho2.dados.Cliente;
import br.pucrs.poo.trabalho2.dados.ClienteCorporativo;
import br.pucrs.poo.trabalho2.dados.Clientes;
import br.pucrs.poo.trabalho2.dados.Contrato;
import br.pucrs.poo.trabalho2.dados.Contratos;
import br.pucrs.poo.trabalho2.dados.FormaPagamento;
import br.pucrs.poo.trabalho2.dados.Jogo;
import br.pucrs.poo.trabalho2.dados.Jogos;
import br.pucrs.poo.trabalho2.dados.PIX;

@PageTitle("Cadastro de Contratos")
@Route("cadastro-contrato")
public class CadastroContrato extends VerticalLayout {

    public CadastroContrato() {

        Text titulo = new Text("Cadastro de Contratos");
        add(titulo);

        Clientes clientes = VaadinSession.getCurrent().getAttribute(Clientes.class);
        Jogos jogos = VaadinSession.getCurrent().getAttribute(Jogos.class);

        if (clientes == null) {
            clientes = new Clientes();
        }

        if (jogos == null) {
            jogos = new Jogos();
        }


        Grid<Cliente> clientesGrid = new Grid<>(Cliente.class, false);
        clientesGrid.addColumn(Cliente::getNumero).setHeader("Número");
        clientesGrid.addColumn(Cliente::getNome).setHeader("Nome");
        clientesGrid.addColumn(Cliente::getEmail).setHeader("E-mail");
        clientesGrid.addColumn(cliente ->
                cliente instanceof ClienteCorporativo ? "Corporativo" : "Individual")
                .setHeader("Tipo");

        clientesGrid.setItems(clientes.getCopia());
        clientesGrid.setHeight("150px");
        clientesGrid.setWidthFull();

        ComboBox<Cliente> clientesComboBox = new ComboBox<>("Cliente");
        clientesComboBox.setItems(clientes.getCopia());
        clientesComboBox.setItemLabelGenerator(cliente ->
                cliente.getNumero() + " - " + cliente.getNome());
        clientesComboBox.setPlaceholder("Selecione um cliente");
        clientesComboBox.setWidth("400px");


        H3 formasPagamentoTitulo = new H3("Formas de Pagamento");

        Grid<FormaPagamento> formasPagamentoGrid = new Grid<>(FormaPagamento.class, false);

        formasPagamentoGrid.addColumn(FormaPagamento::getCod)
                .setHeader("Código");

        formasPagamentoGrid.addColumn(FormaPagamento::getDiaVencimento)
                .setHeader("Dia de Vencimento");

        formasPagamentoGrid.addColumn(formaPagamento ->
                formaPagamento instanceof CartaoCredito
                        ? "Cartão de Crédito"
                        : "PIX")
                .setHeader("Tipo");

        formasPagamentoGrid.addColumn(formaPagamento ->
                formaPagamento instanceof CartaoCredito
                        ? ((CartaoCredito) formaPagamento).getNumero()
                        : ((PIX) formaPagamento).getChave())
                .setHeader("Detalhes");

        formasPagamentoGrid.setHeight("150px");
        formasPagamentoGrid.setWidthFull();

        ComboBox<FormaPagamento> formasPagamentoComboBox = new ComboBox<>("Forma de Pagamento");
        formasPagamentoComboBox.setWidth("400px");
        formasPagamentoComboBox.setPlaceholder("Selecione uma forma de pagamento");
        formasPagamentoComboBox.setItemLabelGenerator(formaPagamento ->
                formaPagamento.getCod() + " - " + (formaPagamento instanceof CartaoCredito ? "Cartão de Crédito" : "PIX"));
        formasPagamentoComboBox.setVisible(false);

        VerticalLayout formasPagamentoSection =
                new VerticalLayout(formasPagamentoTitulo, formasPagamentoGrid, formasPagamentoComboBox);

        formasPagamentoSection.setPadding(false);
        formasPagamentoSection.setSpacing(true);
        formasPagamentoSection.setVisible(false);


        Grid<Jogo> jogosGrid = new Grid<>(Jogo.class, false);

        jogosGrid.addColumn(Jogo::getCodigo).setHeader("Código");
        jogosGrid.addColumn(Jogo::getNome).setHeader("Nome");
        jogosGrid.addColumn(jogo ->
                jogo.getCategoria() != null
                        ? jogo.getCategoria().getDescricao()
                        : "")
                .setHeader("Categoria");
        jogosGrid.addColumn(Jogo::getValorDiario).setHeader("Valor Diário");

        jogosGrid.setItems(jogos.getCopia());
        jogosGrid.setHeight("150px");
        jogosGrid.setWidthFull();

        ComboBox<Jogo> jogosComboBox = new ComboBox<>("Jogo");
        jogosComboBox.setItems(jogos.getCopia());
        jogosComboBox.setItemLabelGenerator(jogo ->
                jogo.getCodigo() + " - " + jogo.getNome());
        jogosComboBox.setPlaceholder("Selecione um jogo");
        jogosComboBox.setWidth("400px");


        VerticalLayout contratoSection = new VerticalLayout();
        contratoSection.setPadding(false);
        contratoSection.setSpacing(true);
        contratoSection.setVisible(false);

        TextField idField = new TextField("ID");
        DatePicker dataField = new DatePicker("Data");
        TextField periodoField = new TextField("Período");

        Button salvarContratoButton = new Button("Salvar Contrato", event -> {
            Cliente clienteSelecionado = clientesComboBox.getValue();
            Jogo jogoSelecionado = jogosComboBox.getValue();
            FormaPagamento formaSelecionada = formasPagamentoComboBox.getValue();

            if (clienteSelecionado == null || jogoSelecionado == null || formaSelecionada == null) {
                Notification.show("Selecione cliente, jogo e forma de pagamento.", 3000, Notification.Position.MIDDLE);
                return;
            }

            String idTexto = idField.getValue();
            LocalDate dataSelecionada = dataField.getValue();
            String periodoTexto = periodoField.getValue();

            if (idTexto == null || idTexto.isBlank() || dataSelecionada == null || periodoTexto == null || periodoTexto.isBlank()) {
                Notification.show("Preencha o ID, a data e o período do contrato.", 3000, Notification.Position.MIDDLE);
                return;
            }

            int id;
            int periodo;
            try {
                id = Integer.parseInt(idTexto.trim());
            } catch (NumberFormatException ex) {
                Notification.show("O ID deve ser um número inteiro.", 3000, Notification.Position.MIDDLE);
                return;
            }

            try {
                periodo = Integer.parseInt(periodoTexto.trim());
            } catch (NumberFormatException ex) {
                Notification.show("O período deve ser um número inteiro.", 3000, Notification.Position.MIDDLE);
                return;
            }

            Contratos contratos = VaadinSession.getCurrent().getAttribute(Contratos.class);
            if (contratos == null) {
                contratos = new Contratos();
                VaadinSession.getCurrent().setAttribute(Contratos.class, contratos);
            }

            if (contratos.buscar(id) != null) {
                Notification.show("Já existe um contrato com esse ID.", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (!contratos.buscar(jogoSelecionado).isEmpty()) {
                Notification.show("Jogo já esta contratado.", 3000, Notification.Position.MIDDLE);
                return;
            }

            Contrato novoContrato = new Contrato(id, dataSelecionada, periodo, clienteSelecionado, jogoSelecionado, formaSelecionada);
            boolean cadastrado = contratos.adicionar(novoContrato);
            if (cadastrado) {
                clienteSelecionado.addContrato(novoContrato);
                jogoSelecionado.addContrato(novoContrato);
                Notification.show("Contrato cadastrado com sucesso!", 3000, Notification.Position.MIDDLE);
                idField.clear();
                dataField.clear();
                periodoField.clear();
                clientesComboBox.clear();
                jogosComboBox.clear();
                formasPagamentoComboBox.clear();
                contratoSection.setVisible(false);
            } else {
                Notification.show("Não foi possível cadastrar o contrato.", 3000, Notification.Position.MIDDLE);
            }
        });

        contratoSection.add(new H3("Dados do Contrato"), idField, dataField, periodoField, salvarContratoButton);

        Runnable atualizarVisibilidadeContrato = () -> {
            boolean todosSelecionados = clientesComboBox.getValue() != null
                    && jogosComboBox.getValue() != null
                    && formasPagamentoComboBox.getValue() != null;
            contratoSection.setVisible(todosSelecionados);
            if (!todosSelecionados) {
                idField.clear();
                dataField.clear();
                periodoField.clear();
            }
        };

        clientesComboBox.addValueChangeListener(event -> {
            Cliente clienteSelecionado = event.getValue();

            if (clienteSelecionado != null) {
                formasPagamentoGrid.setItems(clienteSelecionado.getFormasPagamento());
                formasPagamentoComboBox.setItems(clienteSelecionado.getFormasPagamento());
                formasPagamentoComboBox.setVisible(true);
                formasPagamentoSection.setVisible(true);
            } else {
                formasPagamentoComboBox.clear();
                formasPagamentoComboBox.setVisible(false);
                formasPagamentoSection.setVisible(false);
            }

            atualizarVisibilidadeContrato.run();
        });

        jogosComboBox.addValueChangeListener(event -> atualizarVisibilidadeContrato.run());
        formasPagamentoComboBox.addValueChangeListener(event -> atualizarVisibilidadeContrato.run());

        VerticalLayout clientesSection = new VerticalLayout(
                new H3("Clientes"),
                clientesGrid,
                clientesComboBox,
                formasPagamentoSection);

        VerticalLayout jogosSection = new VerticalLayout(
                new H3("Jogos"),
                jogosGrid,
                jogosComboBox);

        clientesSection.setWidthFull();
        jogosSection.setWidthFull();

        HorizontalLayout gridsLayout = new HorizontalLayout();
        gridsLayout.setWidthFull();
        gridsLayout.setSpacing(true);

        clientesSection.setWidth("50%");
        jogosSection.setWidth("50%");

        gridsLayout.add(clientesSection, jogosSection);

        gridsLayout.setDefaultVerticalComponentAlignment(Alignment.START);

        clientesSection.setPadding(false);
        jogosSection.setPadding(false);

        clientesSection.setSpacing(true);
        jogosSection.setSpacing(true);


        Button voltarButton = new Button("Voltar",
                e -> UI.getCurrent().navigate(""));

        add(gridsLayout, contratoSection, voltarButton);
    }
}