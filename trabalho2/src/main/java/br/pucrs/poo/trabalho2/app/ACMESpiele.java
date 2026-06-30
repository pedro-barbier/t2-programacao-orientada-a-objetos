package br.pucrs.poo.trabalho2.app;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
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
        Dialog dialogo = new Dialog();
        dialogo.setCloseOnOutsideClick(false);
        dialogo.setCloseOnEsc(false);

        Text pergunta = new Text("Deseja carregar os dados iniciais do sistema?");

        Button simBtn = new Button("Sim", e -> {
            inicializar();
            dialogo.close();
        });
        Button naoBtn = new Button("Não", e -> dialogo.close());

        HorizontalLayout botoesDialogo = new HorizontalLayout(simBtn, naoBtn);
        botoesDialogo.setJustifyContentMode(JustifyContentMode.START);

        VerticalLayout conteudoDialogo = new VerticalLayout(pergunta, botoesDialogo);
        dialogo.add(conteudoDialogo);
        dialogo.open();

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

    public void inicializar() {
        // CLIENTES
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/dados/CLIENTESINICIAL.CSV")))) {
            String linha = br.readLine();
            while ((linha = br.readLine()) != null) {
                try {
                    String[] valores = linha.split(";");
                    int tipo = Integer.parseInt(valores[3]);
                    if (tipo == 1) {
                        clientes.adicionar(
                            new ClienteIndividual(
                                Integer.parseInt(valores[0]), 
                                valores[1],
                                valores[2], 
                                valores[4]
                            )
                        );
                    } else if (tipo == 2) {
                        clientes.adicionar(
                            new ClienteCorporativo(
                                Integer.parseInt(valores[0]), 
                                valores[1],
                                valores[2], 
                                valores[4], 
                                valores[5]
                            )
                        );
                    }
                    System.out.println("Cliente adicionado com sucesso: " + valores[1]);
                } catch (Exception e) {
                    System.out.println("Erro ao processar a linha do arquivo de clientes: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de clientes: " + e.getMessage());
        }

        // JOGOS
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/dados/JOGOSINICIAL.CSV")))) {
            String linha = br.readLine();
            while ((linha = br.readLine()) != null) {
                try {
                    String[] valores = linha.split(";");
                    jogos.adicionar(
                        new Jogo(
                            Integer.parseInt(valores[0]), 
                            valores[1], 
                            Integer.parseInt(valores[2]), 
                            Double.parseDouble(valores[3]), 
                            Categoria.valueOf(valores[4])
                        )
                    );
                    System.out.println("Jogo adicionado com sucesso: " + valores[1]);
                } catch (Exception e) {
                    System.out.println("Erro ao processar a linha do arquivo de jogos: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de jogos: " + e.getMessage());
        }

        // FORMAS DE PAGAMENTO
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/yyyy");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/dados/FORMASPAGAMENTOINICIAL.CSV")))) {
            String linha = br.readLine();
            while ((linha = br.readLine()) != null) {
                try {
                    String[] valores = linha.split(";");

                    FormaPagamento fp = null;

                    int tipo = Integer.parseInt(valores[3]);
                    if (tipo == 1) {
                        fp = new CartaoCredito(
                            Integer.parseInt(valores[0]),
                            Integer.parseInt(valores[1]), 
                            valores[4],
                            LocalDate.parse(valores[5], formatador)
                        );
                    } else if (tipo == 2) {
                        fp = new PIX(
                            Integer.parseInt(valores[0]),
                            Integer.parseInt(valores[1]),
                            valores[4]
                        );
                    }
                    
                    Cliente c = clientes.buscar(Integer.parseInt(valores[2]));
                    if (c!=null && fp!=null) {
                        c.addFormaPagamento(fp);
                        System.out.println("Forma de pagamento adicionada com sucesso para o cliente: " + c.getNome());
                    } else {
                        System.out.println("Cliente não encontrado ou forma de pagamento inválida para a linha: " + linha);
                    }

                } catch (Exception e) {
                    System.out.println("Erro ao processar a linha do arquivo de formas de pagamento: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de formas de pagamento: " + e.getMessage());
        }

        // CONTRATOS
        Queue<Contrato> contratos = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/dados/CONTRATOSINICIAL.CSV")))) {
            String linha = br.readLine();
            while ((linha = br.readLine()) != null) {
                try {
                    String[] valores = linha.split(";");

                    Cliente c = clientes.buscar(Integer.parseInt(valores[3]));
                    Jogo j = jogos.buscar(Integer.parseInt(valores[4]));
                    FormaPagamento fp = c.buscarFormaPagamento(Integer.parseInt(valores[5]));

                    if (c != null && j != null && fp != null) {
                        Contrato contrato = new Contrato(
                            Integer.parseInt(valores[0]), 
                            LocalDate.parse(valores[1], formatador), 
                            Integer.parseInt(valores[2]), 
                            c, 
                            j, 
                            fp
                        );
                        contratos.add(contrato);
                    } else {
                        System.out.println("Cliente, jogo ou forma de pagamento não encontrado para a linha: " + linha);
                    }

                } catch (Exception e) {
                    System.out.println("Erro ao processar a linha do arquivo de contratos: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de contratos: " + e.getMessage());
        }
        while (!contratos.isEmpty()) {
            Contrato contrato = contratos.poll();
            if (!this.contratos.adicionar(contrato)) {
                System.out.println("Contrato já existe para o jogo " + contrato.getJogo().getNome() + " ou ID duplicado: " + contrato.getId());
            } else {
                System.out.println("Contrato adicionado com sucesso: " + contrato.descrever());
            }
        }
    }
}