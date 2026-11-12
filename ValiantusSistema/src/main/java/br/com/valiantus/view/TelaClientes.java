package br.com.valiantus.view;
import br.com.valiantus.dao.*;
import br.com.valiantus.model.*;
import br.com.valiantus.exception.ValidacaoException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
public class TelaClientes extends JFrame {
    private final CrudDAO<Cliente> dao=new ClienteDAO(); // Exemplo de polimorfismo
    private final String[] nomes={"Nome","CPF","Telefone","E-mail","Endereço","Cidade","Estado"};
    private final JTextField[] campos=new JTextField[nomes.length];
    private final JTextField pesquisa=new JTextField(20);
    private final DefaultTableModel modelo=new DefaultTableModel(new String[]{"ID","Nome","CPF","Telefone","E-mail","Endereço","Cidade","Estado"},0){
        public boolean isCellEditable(int l,int c){return false;}
    };
    private final JTable tabela=new JTable(modelo);
    private int selecionado=0;
    public TelaClientes(){
        super("Valiantus - Clientes");setSize(1040,650);setLocationRelativeTo(null);setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel principal=new JPanel(new BorderLayout(10,10));principal.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));principal.setBackground(Estilo.FUNDO);
        principal.add(Estilo.titulo("Cadastro de clientes"),BorderLayout.NORTH);
        JPanel centro=new JPanel(new BorderLayout(8,8));centro.setOpaque(false);
        JPanel formulario=new JPanel(new GridLayout(0,2,8,8));
        for(int i=0;i<nomes.length;i++){formulario.add(new JLabel(nomes[i]+":"));campos[i]=new JTextField();formulario.add(campos[i]);}
        centro.add(formulario,BorderLayout.NORTH);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);tabela.setAutoCreateRowSorter(true);
        centro.add(new JScrollPane(tabela),BorderLayout.CENTER);
        JPanel busca=new JPanel(new FlowLayout(FlowLayout.LEFT));busca.add(new JLabel("Pesquisar nome ou CPF:"));busca.add(pesquisa);
        JButton buscar=new JButton("Pesquisar"),todos=new JButton("Mostrar todos");busca.add(buscar);busca.add(todos);
        centro.add(busca,BorderLayout.SOUTH);principal.add(centro,BorderLayout.CENTER);
        JPanel botoes=new JPanel(new FlowLayout());
        JButton salvar=Estilo.botao("Cadastrar"),atualizar=Estilo.botao("Atualizar"),excluir=Estilo.botao("Excluir"),limpar=new JButton("Limpar"),voltar=new JButton("Voltar");
        for(JButton b:new JButton[]{salvar,atualizar,excluir,limpar,voltar})botoes.add(b);
        principal.add(botoes,BorderLayout.SOUTH);add(principal);
        salvar.addActionListener(e->operacao("cadastrar"));atualizar.addActionListener(e->operacao("atualizar"));excluir.addActionListener(e->operacao("excluir"));
        limpar.addActionListener(e->limpar());voltar.addActionListener(e->dispose());
        buscar.addActionListener(e->carregar(pesquisa.getText().trim()));todos.addActionListener(e->{pesquisa.setText("");carregar("");});
        tabela.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting() && tabela.getSelectedRow()>=0){
            int linha=tabela.convertRowIndexToModel(tabela.getSelectedRow());selecionado=(int)modelo.getValueAt(linha,0);
            for(int i=0;i<campos.length;i++)campos[i].setText(String.valueOf(modelo.getValueAt(linha,i+1)));
        }});
        carregar("");
    }
    private Cliente ler()throws ValidacaoException{
        String nome=campos[0].getText().trim(),cpf=campos[1].getText().replaceAll("\\D","");
        if(nome.isBlank())throw new ValidacaoException("Informe o nome.");
        if(!cpfValido(cpf))throw new ValidacaoException("CPF inválido. Informe um CPF válido.");
        String email=campos[3].getText().trim();
        if(!email.isBlank()&&!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))throw new ValidacaoException("E-mail inválido.");
        return new Cliente(selecionado,nome,cpf,campos[2].getText().trim(),email,campos[4].getText().trim(),campos[5].getText().trim(),campos[6].getText().trim());
    }
    private boolean cpfValido(String cpf){
        if(!cpf.matches("\\d{11}")||cpf.chars().distinct().count()==1)return false;
        for(int dig=9;dig<=10;dig++){int soma=0;for(int i=0;i<dig;i++)soma+=(cpf.charAt(i)-'0')*(dig+1-i);
            int resto=(soma*10)%11;if(resto==10)resto=0;if(resto!=cpf.charAt(dig)-'0')return false;}
        return true;
    }
    private void operacao(String tipo){
        try{
            if(tipo.equals("excluir")){
                if(selecionado==0)throw new ValidacaoException("Selecione um cliente na tabela.");
                if(JOptionPane.showConfirmDialog(this,"Excluir este cliente?","Confirmação",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
                dao.excluir(selecionado);
            }else{
                if(tipo.equals("atualizar")&&selecionado==0)throw new ValidacaoException("Selecione um cliente para atualizar.");
                Cliente c=ler();if(tipo.equals("cadastrar"))dao.cadastrar(c);else dao.atualizar(c);
            }
            JOptionPane.showMessageDialog(this,"Operação realizada com sucesso.");limpar();carregar("");
        }catch(ValidacaoException ex){JOptionPane.showMessageDialog(this,ex.getMessage());}
        catch(SQLException ex){JOptionPane.showMessageDialog(this,ex.getMessage().contains("FOREIGN KEY")?"Este cliente possui veículos. Exclua os veículos antes.":ex.getMessage().contains("UNIQUE")?"CPF já cadastrado.":"Erro no banco: "+ex.getMessage());}
    }
    private void limpar(){selecionado=0;tabela.clearSelection();for(JTextField c:campos)c.setText("");}
    private void carregar(String filtro){
        try{modelo.setRowCount(0);List<Cliente> lista=dao.listar(filtro);
            for(Cliente c:lista)modelo.addRow(new Object[]{c.getId(),c.getNome(),c.getCpf(),c.getTelefone(),c.getEmail(),c.getEndereco(),c.getCidade(),c.getEstado()});
        }catch(SQLException ex){JOptionPane.showMessageDialog(this,"Erro ao listar clientes: "+ex.getMessage());}
    }
}
