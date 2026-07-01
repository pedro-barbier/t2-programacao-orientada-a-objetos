package br.pucrs.poo.trabalho2.app;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import br.pucrs.poo.trabalho2.dados.CartaoCredito;
import br.pucrs.poo.trabalho2.dados.Cliente;
import br.pucrs.poo.trabalho2.dados.ClienteCorporativo;
import br.pucrs.poo.trabalho2.dados.Clientes;
import br.pucrs.poo.trabalho2.dados.Contrato;
import br.pucrs.poo.trabalho2.dados.Contratos;
import br.pucrs.poo.trabalho2.dados.Jogo;
import br.pucrs.poo.trabalho2.dados.Jogos;

@PageTitle("Consultas")
@Route("consultas")
public class Consultas extends VerticalLayout {
    private final String CONSULTA_JOGO = "Jogo com maior valor diário";
    private final String CONSULTA_CONTRATO = "Contrato com maior valor final";
    private final String CONSULTA_CLIENTE = "Cliente com maior número de contratos";

    private final DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/yyyy");

    public Consultas() {
        Text titulo = new Text("Consultas");
        add(titulo);

        H4 selecioneConsultaTitulo = new H4("Selecione uma consulta");

        RadioButtonGroup<String> consultaGroup = new RadioButtonGroup<>();
        consultaGroup.setItems(CONSULTA_JOGO, CONSULTA_CONTRATO, CONSULTA_CLIENTE);

        VerticalLayout resultadoContainer = new VerticalLayout();

        Button consultarButton = new Button("Consultar", event -> {
            resultadoContainer.removeAll();
            String consulta = consultaGroup.getValue();

            if (consulta == null) {
                Notification.show("Selecione uma consulta.", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (CONSULTA_JOGO.equals(consulta)) {
                consultarJogoMaiorValorDiario(resultadoContainer);
            } else if (CONSULTA_CONTRATO.equals(consulta)) {
                consultarContratoMaiorValorFinal(resultadoContainer);
            } else if (CONSULTA_CLIENTE.equals(consulta)) {
                consultarClienteMaiorMontanteContratos(resultadoContainer);
            }
        });

        Button voltarButton = new Button("Voltar", e -> UI.getCurrent().navigate(""));

        HorizontalLayout botoes = new HorizontalLayout(voltarButton, consultarButton);

        add(selecioneConsultaTitulo, consultaGroup, botoes, resultadoContainer);
    }

    private void consultarJogoMaiorValorDiario(VerticalLayout resultadoContainer) {
        Jogos jogos = VaadinSession.getCurrent().getAttribute(Jogos.class);
        List<Jogo> todos = jogos != null ? jogos.getCopia() : new ArrayList<>();

        if (todos.isEmpty()) {
            Notification.show("Nenhum jogo cadastrado.", 3000, Notification.Position.MIDDLE);
            return;
        }

        double maiorValor = todos.get(0).getValorDiario();
        for (Jogo jogo : todos) {
            if (jogo.getValorDiario() > maiorValor) {
                maiorValor = jogo.getValorDiario();
            }
        }

        List<Jogo> resultado = new ArrayList<>();
        for (Jogo jogo : todos) {
            if (jogo.getValorDiario() == maiorValor) {
                resultado.add(jogo);
            }
        }

        Grid<Jogo> jogoGrid = new Grid<>(Jogo.class, false);
        jogoGrid.addColumn(jogo -> jogo.getCodigo()).setHeader("Código").setAutoWidth(true);
        jogoGrid.addColumn(jogo -> jogo.getNome()).setHeader("Nome").setAutoWidth(true);
        jogoGrid.addColumn(jogo -> jogo.getAno()).setHeader("Ano").setAutoWidth(true);
        jogoGrid.addColumn(jogo -> jogo.getValorDiario()).setHeader("Valor Diário").setAutoWidth(true);
        jogoGrid.addColumn(jogo -> jogo.getCategoria().getDescricao()).setHeader("Categoria").setAutoWidth(true);
        jogoGrid.setItems(resultado);
        jogoGrid.setHeight("200px");

        resultadoContainer.add(jogoGrid);
    }

    private void consultarContratoMaiorValorFinal(VerticalLayout resultadoContainer) {
        Contratos contratos = VaadinSession.getCurrent().getAttribute(Contratos.class);
        List<Contrato> todos = contratos != null ? contratos.getCopia() : new ArrayList<>();

        if (todos.isEmpty()) {
            Notification.show("Nenhum contrato cadastrado.", 3000, Notification.Position.MIDDLE);
            return;
        }

        double maiorValor = todos.get(0).calculaValorFinal();
        for (Contrato contrato : todos) {
            if (contrato.calculaValorFinal() > maiorValor) {
                maiorValor = contrato.calculaValorFinal();
            }
        }

        List<Contrato> resultado = new ArrayList<>();
        for (Contrato contrato : todos) {
            if (contrato.calculaValorFinal() == maiorValor) {
                resultado.add(contrato);
            }
        }

        Grid<Contrato> contratoGrid = new Grid<>(Contrato.class, false);
        contratoGrid.addColumn(contrato -> contrato.getId()).setHeader("ID").setAutoWidth(true);
        contratoGrid.addColumn(contrato -> contrato.getData().format(formatador)).setHeader("Data").setAutoWidth(true);
        contratoGrid.addColumn(contrato -> contrato.getPeriodo()).setHeader("Período").setAutoWidth(true);
        contratoGrid.addColumn(contrato -> contrato.getCliente().getNumero()).setHeader("Cliente Nº").setAutoWidth(true);
        contratoGrid.addColumn(contrato -> contrato.getCliente().getNome()).setHeader("Cliente").setAutoWidth(true);
        contratoGrid.addColumn(contrato -> contrato.getJogo().getCodigo()).setHeader("Jogo Cód.").setAutoWidth(true);
        contratoGrid.addColumn(contrato -> contrato.getJogo().getNome()).setHeader("Jogo").setAutoWidth(true);
        contratoGrid.addColumn(contrato -> contrato.getFormaPagamento().getCod()).setHeader("Forma Pgto. Cód.").setAutoWidth(true);
        contratoGrid.addColumn(contrato -> contrato.getFormaPagamento() instanceof CartaoCredito ? "Cartão de Crédito" : "PIX")
                .setHeader("Forma Pgto. Tipo").setAutoWidth(true);
        contratoGrid.addColumn(contrato -> contrato.calculaValorFinal()).setHeader("Valor Final").setAutoWidth(true);
        contratoGrid.setItems(resultado);
        contratoGrid.setHeight("200px");

        resultadoContainer.add(contratoGrid);
    }

    private void consultarClienteMaiorMontanteContratos(VerticalLayout resultadoContainer) {
        Clientes clientes = VaadinSession.getCurrent().getAttribute(Clientes.class);
        Contratos contratos = VaadinSession.getCurrent().getAttribute(Contratos.class);
        List<Cliente> todos = clientes != null ? clientes.getCopia() : new ArrayList<>();

        if (todos.isEmpty()) {
            Notification.show("Nenhum cliente cadastrado.", 3000, Notification.Position.MIDDLE);
            return;
        }

        int maiorQuantidade = 0;
        for (Cliente cliente : todos) {
            int quantidade = contratos != null ? contratos.buscar(cliente).size() : 0;
            if (quantidade > maiorQuantidade) {
                maiorQuantidade = quantidade;
            }
        }

        if (maiorQuantidade == 0) {
            Notification.show("Nenhum contrato cadastrado.", 3000, Notification.Position.MIDDLE);
            return;
        }

        List<Cliente> resultado = new ArrayList<>();
        for (Cliente cliente : todos) {
            int quantidade = contratos != null ? contratos.buscar(cliente).size() : 0;
            if (quantidade == maiorQuantidade) {
                resultado.add(cliente);
            }
        }

        final int quantidadeFinal = maiorQuantidade;

        Grid<Cliente> clienteGrid = new Grid<>(Cliente.class, false);
        clienteGrid.addColumn(cliente -> cliente.getNumero()).setHeader("Número").setAutoWidth(true);
        clienteGrid.addColumn(cliente -> cliente.getNome()).setHeader("Nome").setAutoWidth(true);
        clienteGrid.addColumn(cliente -> cliente.getEmail()).setHeader("Email").setAutoWidth(true);
        clienteGrid.addColumn(cliente -> cliente instanceof ClienteCorporativo ? "Corporativo" : "Individual")
                .setHeader("Tipo").setAutoWidth(true);
        clienteGrid.addColumn(cliente -> quantidadeFinal).setHeader("Quantidade de Contratos").setAutoWidth(true);
        clienteGrid.setItems(resultado);
        clienteGrid.setHeight("200px");

        resultadoContainer.add(clienteGrid);
    }
}
