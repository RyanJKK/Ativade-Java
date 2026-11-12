package br.com.valiantus.view;

import br.com.valiantus.database.Conexao;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TelaPrincipal extends JFrame {
    private final JLabel totalClientes = new JLabel("0", SwingConstants.CENTER);
    private final JLabel totalVeiculos = new JLabel("0", SwingConstants.CENTER);
    private final JLabel situacao = new JLabel("Banco de dados: verificando...");

    public TelaPrincipal() {
        super("Valiantus - Painel principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(760, 530);
        setMinimumSize(new Dimension(680, 480));
        setLocationRelativeTo(null);

        JPanel principal = new JPanel(new BorderLayout(0, 16));
        principal.setBackground(Estilo.FUNDO);

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(Estilo.PRETO);
        cabecalho.setBorder(BorderFactory.createEmptyBorder(22, 25, 22, 25));
        JLabel marca = new JLabel("VALIANTUS");
        marca.setFont(new Font("SansSerif", Font.BOLD, 29));
        marca.setForeground(Color.WHITE);
        JLabel descricao = new JLabel("PROTEÇÃO VEICULAR  |  SISTEMA DE CADASTRO");
        descricao.setForeground(new Color(210,210,210));
        JPanel textos = new JPanel(new GridLayout(2,1,0,4));
        textos.setOpaque(false);
        textos.add(marca);
        textos.add(descricao);
        cabecalho.add(textos, BorderLayout.WEST);
        principal.add(cabecalho, BorderLayout.NORTH);

        JPanel conteudo = new JPanel(new BorderLayout(12, 14));
        conteudo.setOpaque(false);
        conteudo.setBorder(BorderFactory.createEmptyBorder(5, 24, 10, 24));

        JLabel boasVindas = Estilo.titulo("Painel de controle");
        conteudo.add(boasVindas, BorderLayout.NORTH);

        JPanel meio = new JPanel(new BorderLayout(12, 18));
        meio.setOpaque(false);
        JPanel numeros = new JPanel(new GridLayout(1,2,14,0));
        numeros.setOpaque(false);
        numeros.add(cartao("CLIENTES CADASTRADOS", totalClientes));
        numeros.add(cartao("VEÍCULOS CADASTRADOS", totalVeiculos));
        meio.add(numeros, BorderLayout.NORTH);

        JPanel acoes = new JPanel(new GridLayout(2,2,14,14));
        acoes.setOpaque(false);
        JButton clientes = Estilo.botao("Gerenciar clientes");
        JButton veiculos = Estilo.botao("Gerenciar veículos");
        JButton atualizar = Estilo.botao("Atualizar painel");
        JButton sobre = Estilo.botao("Sobre o sistema");
        acoes.add(clientes);
        acoes.add(veiculos);
        acoes.add(atualizar);
        acoes.add(sobre);
        meio.add(acoes, BorderLayout.CENTER);
        conteudo.add(meio, BorderLayout.CENTER);
        principal.add(conteudo, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new BorderLayout(12,0));
        rodape.setBorder(BorderFactory.createEmptyBorder(8,24,15,24));
        rodape.setBackground(Estilo.FUNDO);
        rodape.add(situacao, BorderLayout.CENTER);
        JButton sair = new JButton("Sair da conta");
        rodape.add(sair, BorderLayout.EAST);
        principal.add(rodape, BorderLayout.SOUTH);
        add(principal);

        clientes.addActionListener(e -> abrirClientes());
        veiculos.addActionListener(e -> abrirVeiculos());
        atualizar.addActionListener(e -> atualizarResumo());
        sobre.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "VALIANTUS - PROTEÇÃO VEICULAR\n" +
            "Projeto acadêmico de cadastro de clientes e veículos.\n" +
            "Integrantes: Diego Carlos Raimundo Silva e Bruno Ryan Pacas Veras.\n" +
            "Tecnologias: Java Swing, SQLite e JDBC.\n" +
            "Funcionalidades: login, cadastro, consulta, edição e exclusão.\n" +
            "Sistema demonstrativo, não oficial da empresa.",
            "Sobre o sistema", JOptionPane.INFORMATION_MESSAGE));
        sair.addActionListener(e -> {
            dispose();
            new TelaLogin().setVisible(true);
        });
        atualizarResumo();
    }

    private JPanel cartao(String nome, JLabel numero) {
        JPanel painel = new JPanel(new BorderLayout(0,8));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210,210,210)),
            BorderFactory.createEmptyBorder(12,12,12,12)));
        JLabel titulo = new JLabel(nome, SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif",Font.BOLD,12));
        numero.setFont(new Font("SansSerif",Font.BOLD,30));
        numero.setForeground(Estilo.VERMELHO);
        painel.add(titulo,BorderLayout.NORTH);
        painel.add(numero,BorderLayout.CENTER);
        return painel;
    }

    private int contar(Connection c, String tabela) throws SQLException {
        // Os nomes das tabelas são constantes internas, não dados fornecidos pelo usuário.
        try (Statement s = c.createStatement();
             ResultSet r = s.executeQuery("SELECT COUNT(*) FROM " + tabela)) {
            return r.next() ? r.getInt(1) : 0;
        }
    }

    private void atualizarResumo() {
        try (Connection c = Conexao.abrir()) {
            totalClientes.setText(String.valueOf(contar(c,"clientes")));
            totalVeiculos.setText(String.valueOf(contar(c,"veiculos")));
            situacao.setText("Banco de dados: conectado");
        } catch (SQLException ex) {
            situacao.setText("Banco de dados: erro de conexão");
            JOptionPane.showMessageDialog(this,"Erro ao atualizar painel: "+ex.getMessage());
        }
    }

    private void abrirClientes() {
        TelaClientes tela = new TelaClientes();
        tela.setVisible(true);
        tela.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent e) { atualizarResumo(); }
        });
    }

    private void abrirVeiculos() {
        TelaVeiculos tela = new TelaVeiculos();
        tela.setVisible(true);
        tela.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent e) { atualizarResumo(); }
        });
    }
}
