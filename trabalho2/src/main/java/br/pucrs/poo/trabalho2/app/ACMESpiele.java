package br.pucrs.poo.trabalho2.app;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

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

        //// AREA DE CADASTRO (1-4)
        Text subtitulo = new Text("Area de cadastro");
        Button cadCliente = new Button("Cadastrar clientes", e -> {
            UI.getCurrent().navigate("cadastro-cliente");
        });
        Button cadJogo = new Button("Cadastrar jogos", e -> {
            UI.getCurrent().navigate("cadastro-jogo");
        });
        Button cadFormaPagamento = new Button("Cadastrar formas de pagamento", e -> {
            UI.getCurrent().navigate("cadastro-forma-pagamento");
        });
        Button cadContrato = new Button("Cadastrar contratos", e -> {
            UI.getCurrent().navigate("cadastro-contrato");
        });
        VerticalLayout area1 = new VerticalLayout(subtitulo, cadCliente, cadJogo, cadFormaPagamento, cadContrato);
        add(area1);

        //// AREA DE RELATORIOS (5-7)
        Text subtitulo2 = new Text("Area de relatorios");
        Button relatorioJogos = new Button("Relatorio de jogos", e -> {
            UI.getCurrent().navigate("relatorio-jogos");
        });
        Button relatorioClientes = new Button("Relatorio de clientes", e -> {
            UI.getCurrent().navigate("relatorio-clientes");
        });
        Button relatorioContratos = new Button("Relatorio de contratos", e -> {
            UI.getCurrent().navigate("relatorio-contratos");
        });
        VerticalLayout area2 = new VerticalLayout(subtitulo2, relatorioJogos, relatorioClientes, relatorioContratos);
        add(area2);

        //// AREA ADMINISTRATIVA (8-10)
        Text subtitulo3 = new Text("Area administrativa");
        Button removerContrato = new Button("Remover contrato", e -> {
            UI.getCurrent().navigate("remover-contrato");
        });
        Button alterarCliente = new Button("Alterar cliente", e -> {
            UI.getCurrent().navigate("alterar-cliente");
        });
        Button consultar = new Button("Consultar", e -> {
            UI.getCurrent().navigate("consultas");
        });
        VerticalLayout area3 = new VerticalLayout(subtitulo3, removerContrato, alterarCliente, consultar);
        add(area3);

        //// AREA DE SALVAR/CARREGAR DADOS (11-12)
        Button salvarDados = new Button("Salvar dados");
        Button carregarDados = new Button("Carregar dados");
        HorizontalLayout botoes = new HorizontalLayout(salvarDados, carregarDados);
        add(botoes);

        Button sair = new Button("Finalizar sistema", e -> {
            VaadinSession.getCurrent().getSession().invalidate();
            VaadinSession.getCurrent().close();
            System.exit(0);
        });
        add(sair);

    }

    /*
        O método inicializar() da classe ACMESpiele deve ler dados de arquivos de entrada para
        cadastrar alguns clientes, jogos e contratos iniciais (os formatos dos arquivos de entrada são
        apresentados no Apêndice do enunciado). Cada contrato lido do arquivo de contratos deve ser
        armazenado em uma fila. Após a leitura de todos os contratos do arquivo, cada contrato válido
        deve ser cadastrado no sistema
        Todo cliente precisa se cadastrar com seus dados; e pode ser individual (pessoa física) ou
        corporativo (pessoa jurídica). O método descrever() deve gerar uma String com os valores dos
        atributos separados por (;) ponto-e-vírgula.
        A empresa disponibiliza diversos jogos eletrônicos exclusivos (podem ser contratados apenas
        por um cliente de cada vez) para serem contratados para serem jogados por um determinado
        período (em dias) a partir da data do contrato.
        No contrato é definida uma forma de pagamento.
     */
    public void inicializar() {
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
