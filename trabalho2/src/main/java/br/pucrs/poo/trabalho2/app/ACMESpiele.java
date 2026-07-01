package br.pucrs.poo.trabalho2.app;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.io.File;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.node.ArrayNode;

import br.pucrs.poo.trabalho2.dados.*;

@PageTitle("ACME Spiele - Sistema de Locacao de Jogos")
@Route("")
public class ACMESpiele extends VerticalLayout {
    private Clientes clientes;
    private Jogos jogos;
    private Contratos contratos;

    public ACMESpiele() {
        clientes = VaadinSession.getCurrent().getAttribute(Clientes.class);
        if (clientes == null) {
            clientes = new Clientes();
            VaadinSession.getCurrent().setAttribute(Clientes.class, clientes);
        }
        jogos = VaadinSession.getCurrent().getAttribute(Jogos.class);
        if (jogos == null) {
            jogos = new Jogos();
            VaadinSession.getCurrent().setAttribute(Jogos.class, jogos);
        }
        contratos = VaadinSession.getCurrent().getAttribute(Contratos.class);
        if (contratos == null) {
            contratos = new Contratos();
            VaadinSession.getCurrent().setAttribute(Contratos.class, contratos);
        }

        Boolean dialogoExibido = (Boolean) VaadinSession.getCurrent().getAttribute("dialogoInicialExibido");
        if (dialogoExibido == null || !dialogoExibido) {
            VaadinSession.getCurrent().setAttribute("dialogoInicialExibido", true);

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
        }

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
            if (clientes == null || clientes.getCopia().isEmpty()) {
                Notification.show("Não há clientes cadastrados. Cadastre um cliente antes de cadastrar uma forma de pagamento.", 3000, Notification.Position.MIDDLE);
            } else {
                UI.getCurrent().navigate("cadastro-forma-pagamento");
            }
        });
        Button cadContrato = new Button("Cadastrar contratos", e -> {
            if (clientes == null || clientes.getCopia().isEmpty()) {
                Notification.show("Não há clientes cadastrados. Cadastre um cliente antes de cadastrar um contrato.", 3000, Notification.Position.MIDDLE);
            } else if (jogos == null || jogos.getCopia().isEmpty()) {
                Notification.show("Não há jogos cadastrados. Cadastre um jogo antes de cadastrar um contrato.", 3000, Notification.Position.MIDDLE);
            } else {
                UI.getCurrent().navigate("cadastro-contrato");
            }
        });
        VerticalLayout area1 = new VerticalLayout(subtitulo, cadCliente, cadJogo, cadFormaPagamento, cadContrato);
        add(area1);

        //// AREA DE RELATORIOS (5-7)
        Text subtitulo2 = new Text("Area de relatorios");
        Button relatorioJogos = new Button("Relatorio de jogos", e -> {
            if (jogos == null || jogos.getCopia().isEmpty()) {
                Notification.show("Não há jogos cadastrados.", 3000, Notification.Position.MIDDLE);
            } else {
                UI.getCurrent().navigate("relatorio-jogos");
            }
        });
        Button relatorioClientes = new Button("Relatorio de clientes", e -> {
            if (clientes == null || clientes.getCopia().isEmpty()) {
                Notification.show("Não há clientes cadastrados.", 3000, Notification.Position.MIDDLE);
            } else {
                UI.getCurrent().navigate("relatorio-clientes");
            }
        });
        Button relatorioContratos = new Button("Relatorio de contratos", e -> {
            if (contratos == null || contratos.getCopia().isEmpty()) {
                Notification.show("Não há contratos cadastrados.", 3000, Notification.Position.MIDDLE);
            } else {
                UI.getCurrent().navigate("relatorio-contratos");
            }
        });
        VerticalLayout area2 = new VerticalLayout(subtitulo2, relatorioJogos, relatorioClientes, relatorioContratos);
        add(area2);

        //// AREA ADMINISTRATIVA (8-10)
        Text subtitulo3 = new Text("Area administrativa");
        Button removerContrato = new Button("Remover contrato", e -> {
            if (contratos == null || contratos.getCopia().isEmpty()) {
                Notification.show("Não há contratos cadastrados.", 3000, Notification.Position.MIDDLE);
            } else {
                UI.getCurrent().navigate("remover-contrato");
            }
        });
        Button alterarCliente = new Button("Alterar cliente", e -> {
            if (clientes == null || clientes.getCopia().isEmpty()) {
                Notification.show("Não há clientes cadastrados.", 3000, Notification.Position.MIDDLE);
            } else {
                UI.getCurrent().navigate("alterar-cliente");
            }
        });
        Button consultar = new Button("Consultar", e -> {
            UI.getCurrent().navigate("consultas");
        });
        VerticalLayout area3 = new VerticalLayout(subtitulo3, removerContrato, alterarCliente, consultar);
        add(area3);

        //// AREA DE SALVAR/CARREGAR DADOS (11-12)
        Button salvarDados = new Button("Salvar dados", e -> {
            Dialog dialogo = new Dialog();
            dialogo.setCloseOnOutsideClick(false);

            Text pergunta = new Text("Qual o nome do arquivo para salvar os dados?");
            TextField arquivoField = new TextField("Nome do arquivo (sem extensão)");
            arquivoField.setValue("dados");

            Button confirmarBtn = new Button("Salvar", ev -> {
                String nome = arquivoField.getValue();
                if (nome == null || nome.isBlank()) {
                    Notification.show("Informe o nome do arquivo.", 3000, Notification.Position.MIDDLE);
                    return;
                }
                String arquivo = nome.trim() + ".json";
                salvaDados(arquivo);
                Notification.show("Dados salvos em: " + arquivo, 3000, Notification.Position.MIDDLE);
                dialogo.close();
            });
            Button cancelarBtn = new Button("Cancelar", ev -> dialogo.close());

            HorizontalLayout botoesDialogo = new HorizontalLayout(confirmarBtn, cancelarBtn);
            VerticalLayout conteudoDialogo = new VerticalLayout(pergunta, arquivoField, botoesDialogo);
            dialogo.add(conteudoDialogo);
            dialogo.open();
        });

        Button carregarDados = new Button("Carregar dados", e -> {
            Dialog dialogo = new Dialog();
            dialogo.setCloseOnOutsideClick(false);

            Text pergunta = new Text("Qual o nome do arquivo para carregar os dados?");
            TextField arquivoField = new TextField("Nome do arquivo (sem extensão)");
            arquivoField.setValue("dados");

            Button confirmarBtn = new Button("Carregar", ev -> {
                String nome = arquivoField.getValue();
                if (nome == null || nome.isBlank()) {
                    Notification.show("Informe o nome do arquivo.", 3000, Notification.Position.MIDDLE);
                    return;
                }
                String arquivo = nome.trim() + ".json";
                carregaDados(arquivo);
                Notification.show("Dados carregados de: " + arquivo, 3000, Notification.Position.MIDDLE);
                dialogo.close();
            });
            Button cancelarBtn = new Button("Cancelar", ev -> dialogo.close());

            HorizontalLayout botoesDialogo = new HorizontalLayout(confirmarBtn, cancelarBtn);
            VerticalLayout conteudoDialogo = new VerticalLayout(pergunta, arquivoField, botoesDialogo);
            dialogo.add(conteudoDialogo);
            dialogo.open();
        });

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
            Notification.show("Erro ao ler o arquivo de clientes: " + e.getMessage(), 3000, Notification.Position.TOP_END);
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
            Notification.show("Erro ao ler o arquivo de jogos: " + e.getMessage(), 3000, Notification.Position.TOP_END);
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
            Notification.show("Erro ao ler o arquivo de formas de pagamento: " + e.getMessage(), 3000, Notification.Position.TOP_END);
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
                        c.addContrato(contrato);
                        j.addContrato(contrato);
                    } else {
                        System.out.println("Cliente, jogo ou forma de pagamento não encontrado para a linha: " + linha);
                    }

                } catch (Exception e) {
                    System.out.println("Erro ao processar a linha do arquivo de contratos: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de contratos: " + e.getMessage());
            Notification.show("Erro ao ler o arquivo de contratos: " + e.getMessage(), 3000, Notification.Position.TOP_END);
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

    public void carregaDados(String arquivo) {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/yyyy");

        JsonNode raiz;
        try {
            raiz = mapper.readTree(new File(arquivo));
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo de dados " + arquivo + ": " + e.getMessage());
            Notification.show("Erro ao ler o arquivo de dados " + arquivo + ": " + e.getMessage(), 10000, Notification.Position.TOP_END);
            return;
        }

        // CLIENTES
        for (JsonNode noCliente : raiz.path("clientes")) {
            try {
                int tipo = noCliente.path("tipo").asInt();
                Cliente cliente = null;
                if (tipo == 1) {
                    cliente = new ClienteIndividual(
                        noCliente.path("numero").asInt(),
                        noCliente.path("nome").asString(),
                        noCliente.path("email").asString(),
                        noCliente.path("cpf").asString()
                    );
                } else if (tipo == 2) {
                    cliente = new ClienteCorporativo(
                        noCliente.path("numero").asInt(),
                        noCliente.path("nome").asString(),
                        noCliente.path("email").asString(),
                        noCliente.path("cnpj").asString(),
                        noCliente.path("nomeFantasia").asString()
                    );
                }

                if (cliente != null) {
                    for (JsonNode noFp : noCliente.path("formasPagamento")) {
                        int tipoFp = noFp.path("tipo").asInt();
                        FormaPagamento fp = null;
                        if (tipoFp == 1) {
                            fp = new CartaoCredito(
                                noFp.path("cod").asInt(),
                                noFp.path("diaVencimento").asInt(),
                                noFp.path("numero").asString(),
                                LocalDate.parse(noFp.path("validade").asString(), formatador)
                            );
                        } else if (tipoFp == 2) {
                            fp = new PIX(
                                noFp.path("cod").asInt(),
                                noFp.path("diaVencimento").asInt(),
                                noFp.path("chave").asString()
                            );
                        }
                        if (fp != null) {
                            cliente.addFormaPagamento(fp);
                        }
                    }

                    if (clientes.adicionar(cliente)) {
                        System.out.println("Cliente adicionado com sucesso: " + cliente.getNome());
                    } else {
                        System.out.println("Cliente já existe ou ID duplicado: " + cliente.getNumero());
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro ao processar cliente do arquivo de dados: " + e.getMessage());
            }
        }

        // JOGOS
        for (JsonNode noJogo : raiz.path("jogos")) {
            try {
                Jogo jogo = new Jogo(
                    noJogo.path("codigo").asInt(),
                    noJogo.path("nome").asString(),
                    noJogo.path("ano").asInt(),
                    noJogo.path("valorDiario").asDouble(),
                    Categoria.valueOf(noJogo.path("categoria").asString())
                );
                if (jogos.adicionar(jogo)) {
                    System.out.println("Jogo adicionado com sucesso: " + jogo.getNome());
                } else {
                    System.out.println("Jogo já existe ou ID duplicado: " + jogo.getCodigo());
                }
            } catch (Exception e) {
                System.out.println("Erro ao processar jogo do arquivo de dados: " + e.getMessage());
            }
        }

        // CONTRATOS
        for (JsonNode noContrato : raiz.path("contratos")) {
            try {
                Cliente c = clientes.buscar(noContrato.path("clienteNumero").asInt());
                Jogo j = jogos.buscar(noContrato.path("jogoCodigo").asInt());
                FormaPagamento fp = (c != null) ? c.buscarFormaPagamento(noContrato.path("formaPagamentoCod").asInt()) : null;

                if (c != null && j != null && fp != null) {
                    Contrato contrato = new Contrato(
                        noContrato.path("id").asInt(),
                        LocalDate.parse(noContrato.path("data").asString(), formatador),
                        noContrato.path("periodo").asInt(),
                        c,
                        j,
                        fp
                    );
                    if (contratos.adicionar(contrato)) {
                        c.addContrato(contrato);
                        j.addContrato(contrato);
                        System.out.println("Contrato adicionado com sucesso: " + contrato.descrever());
                    } else {
                        System.out.println("Contrato já existe para o jogo " + j.getNome() + " ou ID duplicado: " + contrato.getId());
                    }
                } else {
                    System.out.println("Cliente, jogo ou forma de pagamento não encontrado para o contrato do arquivo de dados.");
                }
            } catch (Exception e) {
                System.out.println("Erro ao processar contrato do arquivo de dados: " + e.getMessage());
            }
        }
    }

    public void salvaDados(String arquivo) {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/yyyy");

        ObjectNode raiz = mapper.createObjectNode();

        // CLIENTES
        ArrayNode arrayClientes = mapper.createArrayNode();
        for (Cliente c : clientes.getCopia()) {
            ObjectNode noCliente = mapper.createObjectNode();
            noCliente.put("numero", c.getNumero());
            noCliente.put("nome", c.getNome());
            noCliente.put("email", c.getEmail());

            if (c instanceof ClienteIndividual ci) {
                noCliente.put("tipo", 1);
                noCliente.put("cpf", ci.getCpf());
            } else if (c instanceof ClienteCorporativo cc) {
                noCliente.put("tipo", 2);
                noCliente.put("cnpj", cc.getCnpj());
                noCliente.put("nomeFantasia", cc.getNomeFantasia());
            }

            ArrayNode arrayFormasPagamento = mapper.createArrayNode();
            for (FormaPagamento fp : c.getFormasPagamento()) {
                ObjectNode noFp = mapper.createObjectNode();
                noFp.put("cod", fp.getCod());
                noFp.put("diaVencimento", fp.getDiaVencimento());
                if (fp instanceof CartaoCredito cartao) {
                    noFp.put("tipo", 1);
                    noFp.put("numero", cartao.getNumero());
                    noFp.put("validade", cartao.getValidade().format(formatador));
                } else if (fp instanceof PIX pix) {
                    noFp.put("tipo", 2);
                    noFp.put("chave", pix.getChave());
                }
                arrayFormasPagamento.add(noFp);
            }
            noCliente.set("formasPagamento", arrayFormasPagamento);

            ArrayNode arrayContratosCliente = mapper.createArrayNode();
            for (Contrato contrato : contratos.buscar(c)) {
                arrayContratosCliente.add(contrato.getId());
            }
            noCliente.set("contratos", arrayContratosCliente);

            arrayClientes.add(noCliente);
        }
        raiz.set("clientes", arrayClientes);

        // JOGOS
        ArrayNode arrayJogos = mapper.createArrayNode();
        for (Jogo j : jogos.getCopia()) {
            ObjectNode noJogo = mapper.createObjectNode();
            noJogo.put("codigo", j.getCodigo());
            noJogo.put("nome", j.getNome());
            noJogo.put("ano", j.getAno());
            noJogo.put("valorDiario", j.getValorDiario());
            noJogo.put("categoria", j.getCategoria().name());

            List<Contrato> contratosDoJogo = contratos.buscar(j);
            if (!contratosDoJogo.isEmpty()) {
                noJogo.put("contratoId", contratosDoJogo.get(0).getId());
            }

            arrayJogos.add(noJogo);
        }
        raiz.set("jogos", arrayJogos);

        // CONTRATOS
        ArrayNode arrayContratos = mapper.createArrayNode();
        for (Contrato contrato : contratos.getCopia()) {
            ObjectNode noContrato = mapper.createObjectNode();
            noContrato.put("id", contrato.getId());
            noContrato.put("data", contrato.getData().format(formatador));
            noContrato.put("periodo", contrato.getPeriodo());
            noContrato.put("clienteNumero", contrato.getCliente().getNumero());
            noContrato.put("jogoCodigo", contrato.getJogo().getCodigo());
            noContrato.put("formaPagamentoCod", contrato.getFormaPagamento().getCod());
            arrayContratos.add(noContrato);
        }
        raiz.set("contratos", arrayContratos);

        try {
            mapper.writeValue(new File(arquivo), raiz);
            System.out.println("Dados salvos com sucesso no arquivo: " + arquivo);
            Notification.show("Dados salvos com sucesso no arquivo: " + arquivo, 3000, Notification.Position.TOP_END);
        } catch (Exception e) {
            System.out.println("Erro ao salvar o arquivo de dados " + arquivo + ": " + e.getMessage());
            Notification.show("Erro ao salvar o arquivo de dados " + arquivo + ": " + e.getMessage(), 10000, Notification.Position.TOP_END);
        }
    }
}