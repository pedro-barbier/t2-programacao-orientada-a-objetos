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
import br.pucrs.poo.trabalho2.dados.Clientes;
import br.pucrs.poo.trabalho2.dados.FormaPagamento;
import br.pucrs.poo.trabalho2.dados.PIX;

@PageTitle("Relatório de Clientes")
@Route("relatorio-clientes")
public class RelatorioClientes extends VerticalLayout {

    public RelatorioClientes() {

        H2 titulo = new H2("Relatório de Clientes");
        add(titulo);

        Clientes clientes = VaadinSession.getCurrent().getAttribute(Clientes.class);

        if (clientes == null || clientes.getCopia().isEmpty()) {

            add(new Text("Nenhum cliente cadastrado."));

            Button voltar = new Button("Voltar",
                    e -> UI.getCurrent().navigate(""));

            add(voltar);

            return;
        }

        for (Cliente cliente : clientes.getCopia()) {

            VerticalLayout card = new VerticalLayout();

            card.setSpacing(true);
            card.setPadding(true);

            card.getStyle()
                    .set("border", "1px solid lightgray")
                    .set("border-radius", "8px")
                    .set("margin-bottom", "20px")
                    .set("padding", "15px");

            card.add(new H3(cliente.getNumero() + " - " + cliente.getNome()));

            card.add(new Span("Email: " + cliente.getEmail()));
            
            if (cliente instanceof ClienteIndividual) {

                ClienteIndividual ci = (ClienteIndividual) cliente;

                card.add(new Span("Tipo: Individual"));
                card.add(new Span("CPF: " + ci.getCpf()));

            } else {

                ClienteCorporativo cc = (ClienteCorporativo) cliente;

                card.add(new Span("Tipo: Corporativo"));
                card.add(new Span("CNPJ: " + cc.getCnpj()));
                card.add(new Span("Nome Fantasia: " + cc.getNomeFantasia()));
            }

            card.add(new H4("Formas de Pagamento"));

            if (cliente.getFormasPagamento().isEmpty()) {

                card.add(new Span("Nenhuma forma de pagamento cadastrada."));

            } else {
                for (FormaPagamento fp : cliente.getFormasPagamento()) {

                    VerticalLayout pagamento = new VerticalLayout();

                    pagamento.setPadding(false);
                    pagamento.setSpacing(false);

                    pagamento.getStyle()
                            .set("margin-left", "20px")
                            .set("border-left", "4px solid gray")
                            .set("padding-left", "10px");
                    
                    if (fp instanceof CartaoCredito) {

                        CartaoCredito cartao = (CartaoCredito) fp;

                        pagamento.add(new Span("Cartão de Crédito"));
                        pagamento.add(new Span("Código: " + cartao.getCod()));
                        pagamento.add(new Span("Número: " + cartao.getNumero()));
                        pagamento.add(new Span("Dia de Vencimento: "
                                + cartao.getDiaVencimento()));
                        pagamento.add(new Span("Validade: "
                                + cartao.getValidade()));

                    } else {
                        PIX pix = (PIX) fp;
                        pagamento.add(new Span("PIX"));
                        pagamento.add(new Span("Código: " + pix.getCod()));
                        pagamento.add(new Span("Dia de Vencimento: "
                                + pix.getDiaVencimento()));
                        pagamento.add(new Span("Chave: " + pix.getChave()));
                    }
                    card.add(pagamento);
                }
            }
        
            add(card);

            
        }

        Button voltar = new Button("Voltar",
                    e -> UI.getCurrent().navigate(""));

            add(voltar);
    }   
    
}