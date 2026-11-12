package br.com.valiantus.view;
import br.com.valiantus.dao.*;
import br.com.valiantus.model.*;
import br.com.valiantus.exception.ValidacaoException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.Year;
import java.util.*;
public class TelaVeiculos extends JFrame {
    private final CrudDAO<Veiculo> dao=new VeiculoDAO();
    private final ClienteDAO clientesDAO=new ClienteDAO();
    private final JComboBox<OpcaoCliente> cliente=new JComboBox<>();
    private final String[] nomes={"Placa","Marca","Modelo","Ano","Cor"};
    private final JTextField[] campos=new JTextField[nomes.length];
    private final JTextField pesquisa=new JTextField(18);
    private final DefaultTableModel modelo=new DefaultTableModel(new String[]{"ID","Cliente ID","Placa","Marca","Modelo","Ano","Cor"},0){
        public boolean isCellEditable(int l,int c){return false;}
    };
    private final JTable tabela=new JTable(modelo);
    private int selecionado=0;
    private static class OpcaoCliente {
        final int id;final String nome;
        OpcaoCliente(int id,String nome){this.id=id;this.nome=nome;}
        public String toString(){return nome+" (ID "+id+")";}
    }
    public TelaVeiculos(){
        super("Valiantus - Veículos");setSize(980,630);setLocationRelativeTo(null);setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel principal=new JPanel(new BorderLayout(10,10));principal.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));principal.setBackground(Estilo.FUNDO);
        principal.add(Estilo.titulo("Cadastro de veículos"),BorderLayout.NORTH);
        JPanel centro=new JPanel(new BorderLayout(8,8));centro.setOpaque(false);
        JPanel form=new JPanel(new GridLayout(0,2,8,8));form.add(new JLabel("Cliente responsável:"));form.add(cliente);
        for(int i=0;i<nomes.length;i++){form.add(new JLabel(nomes[i]+":"));campos[i]=new JTextField();form.add(campos[i]);}
        centro.add(form,BorderLayout.NORTH);tabela.setAutoCreateRowSorter(true);tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        centro.add(new JScrollPane(tabela),BorderLayout.CENTER);
        JPanel busca=new JPanel(new FlowLayout(FlowLayout.LEFT));busca.add(new JLabel("Pesquisar placa, marca ou modelo:"));busca.add(pesquisa);
        JButton pesquisar=new JButton("Pesquisar"),todos=new JButton("Mostrar todos"),recarregar=new JButton("Atualizar clientes");
        busca.add(pesquisar);busca.add(todos);busca.add(recarregar);centro.add(busca,BorderLayout.SOUTH);principal.add(centro,BorderLayout.CENTER);
        JPanel botoes=new JPanel(new FlowLayout());
        JButton salvar=Estilo.botao("Cadastrar"),atualizar=Estilo.botao("Atualizar"),excluir=Estilo.botao("Excluir"),limpar=new JButton("Limpar"),voltar=new JButton("Voltar");
        for(JButton b:new JButton[]{salvar,atualizar,excluir,limpar,voltar})botoes.add(b);
        principal.add(botoes,BorderLayout.SOUTH);add(principal);
        salvar.addActionListener(e->operacao("cadastrar"));atualizar.addActionListener(e->operacao("atualizar"));excluir.addActionListener(e->operacao("excluir"));
        limpar.addActionListener(e->limpar());voltar.addActionListener(e->dispose());
        pesquisar.addActionListener(e->carregar(pesquisa.getText().trim()));
        todos.addActionListener(e->{pesquisa.setText("");carregar("");});
        recarregar.addActionListener(e->carregarClientes());
        tabela.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting()&&tabela.getSelectedRow()>=0){
            int linha=tabela.convertRowIndexToModel(tabela.getSelectedRow());selecionado=(int)modelo.getValueAt(linha,0);
            int id=(int)modelo.getValueAt(linha,1);
            for(int i=0;i<cliente.getItemCount();i++)if(cliente.getItemAt(i).id==id){cliente.setSelectedIndex(i);break;}
            for(int i=0;i<campos.length;i++)campos[i].setText(String.valueOf(modelo.getValueAt(linha,i+2)));
        }});
        carregarClientes();carregar("");
    }
    private void carregarClientes(){
        try{
            int atual=cliente.getSelectedItem() instanceof OpcaoCliente o?o.id:-1;
            cliente.removeAllItems();
            for(Cliente c:clientesDAO.listar(""))cliente.addItem(new OpcaoCliente(c.getId(),c.getNome()));
            for(int i=0;i<cliente.getItemCount();i++)if(cliente.getItemAt(i).id==atual){cliente.setSelectedIndex(i);break;}
        }catch(SQLException ex){JOptionPane.showMessageDialog(this,"Erro ao carregar clientes: "+ex.getMessage());}
    }
    private Veiculo ler()throws ValidacaoException{
        OpcaoCliente dono=(OpcaoCliente)cliente.getSelectedItem();
        if(dono==null)throw new ValidacaoException("Cadastre primeiro um cliente.");
        String placa=campos[0].getText().trim().toUpperCase().replace("-","");
        if(!placa.matches("[A-Z]{3}[0-9][A-Z0-9][0-9]{2}"))throw new ValidacaoException("Placa inválida (ex.: ABC1234 ou ABC1D23).");
        String marca=campos[1].getText().trim(),modelo=campos[2].getText().trim();
        if(marca.isBlank()||modelo.isBlank())throw new ValidacaoException("Informe marca e modelo.");
        int ano;
        try{ano=Integer.parseInt(campos[3].getText().trim());}catch(NumberFormatException e){throw new ValidacaoException("Ano deve ser um número.");}
        if(ano<1900||ano>Year.now().getValue()+1)throw new ValidacaoException("Ano fora do intervalo permitido.");
        return new Veiculo(selecionado,dono.id,placa,marca,modelo,ano,campos[4].getText().trim());
    }
    private void operacao(String tipo){
        try{
            if(tipo.equals("excluir")){
                if(selecionado==0)throw new ValidacaoException("Selecione um veículo.");
                if(JOptionPane.showConfirmDialog(this,"Excluir este veículo?","Confirmação",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
                dao.excluir(selecionado);
            }else{
                if(tipo.equals("atualizar")&&selecionado==0)throw new ValidacaoException("Selecione um veículo para atualizar.");
                Veiculo v=ler();if(tipo.equals("cadastrar"))dao.cadastrar(v);else dao.atualizar(v);
            }
            JOptionPane.showMessageDialog(this,"Operação realizada com sucesso.");limpar();carregar("");
        }catch(ValidacaoException ex){JOptionPane.showMessageDialog(this,ex.getMessage());}
        catch(SQLException ex){JOptionPane.showMessageDialog(this,ex.getMessage().contains("UNIQUE")?"Placa já cadastrada.":"Erro no banco: "+ex.getMessage());}
    }
    private void limpar(){selecionado=0;tabela.clearSelection();for(JTextField c:campos)c.setText("");}
    private void carregar(String filtro){
        try{modelo.setRowCount(0);
            for(Veiculo v:dao.listar(filtro))modelo.addRow(new Object[]{v.getId(),v.getClienteId(),v.getPlaca(),v.getMarca(),v.getModelo(),v.getAno(),v.getCor()});
        }catch(SQLException ex){JOptionPane.showMessageDialog(this,"Erro ao listar veículos: "+ex.getMessage());}
    }
}
