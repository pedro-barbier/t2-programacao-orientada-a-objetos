package br.pucrs.poo.trabalho2.app;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import br.pucrs.poo.trabalho2.dados.CartaoCredito;
import br.pucrs.poo.trabalho2.dados.Cliente;
import br.pucrs.poo.trabalho2.dados.ClienteCorporativo;
import br.pucrs.poo.trabalho2.dados.ClienteIndividual;
import br.pucrs.poo.trabalho2.dados.Contrato;
import br.pucrs.poo.trabalho2.dados.Contratos;
import br.pucrs.poo.trabalho2.dados.FormaPagamento;
import br.pucrs.poo.trabalho2.dados.Jogo;
import br.pucrs.poo.trabalho2.dados.PIX;

@PageTitle("Relatório de Contratos")
@Route("relatorio-contratos")
public class RelatorioContratos extends VerticalLayout {

    public RelatorioContratos() {
        H2 titulo = new H2("Relatório de Contratos");
        add(titulo);

        Contratos contratos = VaadinSession.getCurrent().getAttribute(Contratos.class);

        if (contratos == null || contratos.getCopia().isEmpty()) {
            Text erro = new Text("Nenhum contrato cadastrado.");
            erro.getStyle().set("color", "red").set("font-size", "16px");
            add(erro);

            Button voltar = new Button("Voltar", e -> UI.getCurrent().navigate(""));
            add(voltar);
            return;
        }

        for (Contrato contrato : contratos.getCopia()) {
            VerticalLayout card = new VerticalLayout();
            card.setSpacing(true);
            card.setPadding(true);
            card.getStyle()
                    .set("border", "1px solid lightgray")
                    .set("border-radius", "8px")
                    .set("margin-bottom", "20px")
                    .set("padding", "15px");

            card.add(new H3("Contrato: " + contrato.getId()));
            card.add(new Span("Data: " + contrato.getData()));
            card.add(new Span("Período: " + contrato.getPeriodo()));
            card.add(new Span("Valor final: " + String.format("%.2f", contrato.calculaValorFinal())));

            card.add(new H4("Cliente"));
            Cliente cliente = contrato.getCliente();
            if (cliente != null) {
                card.add(new Span("Número: " + cliente.getNumero()));
                card.add(new Span("Nome: " + cliente.getNome()));
                card.add(new Span("Email: " + cliente.getEmail()));

                if (cliente instanceof ClienteIndividual) {
                    ClienteIndividual ci = (ClienteIndividual) cliente;
                    card.add(new Span("Tipo: Individual"));
                    card.add(new Span("CPF: " + ci.getCpf()));
                } else if (cliente instanceof ClienteCorporativo) {
                    ClienteCorporativo cc = (ClienteCorporativo) cliente;
                    card.add(new Span("Tipo: Corporativo"));
                    card.add(new Span("CNPJ: " + cc.getCnpj()));
                    card.add(new Span("Nome Fantasia: " + cc.getNomeFantasia()));
                }
            } else {
                card.add(new Span("Cliente não informado."));
            }

            card.add(new H4("Jogo"));
            Jogo jogo = contrato.getJogo();
            if (jogo != null) {
                card.add(new Span("Código: " + jogo.getCodigo()));
                card.add(new Span("Nome: " + jogo.getNome()));
                card.add(new Span("Ano: " + jogo.getAno()));
                card.add(new Span("Valor diário: " + jogo.getValorDiario()));
                card.add(new Span("Categoria: " + jogo.getCategoria().getDescricao()));
            } else {
                card.add(new Span("Jogo não informado."));
            }

            card.add(new H4("Forma de Pagamento"));
            FormaPagamento formaPagamento = contrato.getFormaPagamento();
            if (formaPagamento != null) {
                card.add(new Span("Código: " + formaPagamento.getCod()));
                card.add(new Span("Dia de vencimento: " + formaPagamento.getDiaVencimento()));

                if (formaPagamento instanceof CartaoCredito) {
                    CartaoCredito cartao = (CartaoCredito) formaPagamento;
                    card.add(new Span("Tipo: Cartão de Crédito"));
                    card.add(new Span("Número: " + cartao.getNumero()));
                    card.add(new Span("Validade: " + cartao.getValidade()));
                } else if (formaPagamento instanceof PIX) {
                    PIX pix = (PIX) formaPagamento;
                    card.add(new Span("Tipo: PIX"));
                    card.add(new Span("Chave: " + pix.getChave()));
                } else {
                    card.add(new Span("Tipo: Forma de pagamento genérica"));
                }
            } else {
                card.add(new Span("Forma de pagamento não informada."));
            }

            add(card);
        }

        Button voltar = new Button("Voltar", e -> UI.getCurrent().navigate(""));
        add(voltar);
    }
}
