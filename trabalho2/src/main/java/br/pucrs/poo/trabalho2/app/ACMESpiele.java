package br.pucrs.poo.trabalho2.app;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;

import br.pucrs.poo.trabalho2.dados.*;

@PageTitle("ACME Spiele - Sistema de Locacao de Jogos")
@Route("")
public class ACMESpiele extends VerticalLayout {
    private Clientes clientes = new Clientes();
    private Jogos jogos = new Jogos();
    private Contratos contratos = new Contratos();

    public ACMESpiele() {
        // carregarDados();

        Text titulo = new Text("ACME Spiele");
        add(titulo);

        Text subtitulo = new Text("Area de cadastro");
        Button cadCliente = new Button("Cadastrar clientes");
        Button cadJogo = new Button("Cadastrar jogos");
        Button cadFormaPagamento = new Button("Cadastrar formas de pagamento");
        Button cadContrato = new Button("Cadastrar contratos");
        VerticalLayout area1 = new VerticalLayout(subtitulo, cadCliente, cadJogo, cadFormaPagamento, cadContrato);
        add(area1);

        Text subtitulo2 = new Text("Area de relatorios");
        Button relatorioJogos = new Button("Relatorio de jogos");
        Button relatorioClientes = new Button("Relatorio de clientes");
        Button relatorioContratos = new Button("Relatorio de contratos");
        VerticalLayout area2 = new VerticalLayout(subtitulo2, relatorioJogos, relatorioClientes, relatorioContratos);
        add(area2);

        Text subtitulo3 = new Text("Area administrativa");
        Button removerContrato = new Button("Remover contrato");
        Button alterarCliente = new Button("Alterar cliente");
        Button consultar = new Button("Consultar");
        VerticalLayout area3 = new VerticalLayout(subtitulo3, removerContrato, alterarCliente, consultar);
        add(area3);

        Button salvarDados = new Button("Salvar dados");
        Button carregarDados = new Button("Carregar dados");
        HorizontalLayout botoes = new HorizontalLayout(salvarDados, carregarDados);
        add(botoes);

        Button sair = new Button("Finalizar sistema");
        add(sair);

    }

    public void inicializar() {

    }

    public void carregarDados() {
        // CONTRATOS
        Queue<Contrato> contratos = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/dados/CONTRATOSINICIAL.csv"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] valores = linha.split(";");

            }
        } catch (Exception e) {

        }
    }
}
