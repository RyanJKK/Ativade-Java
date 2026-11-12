package br.com.valiantus.view;
import br.com.valiantus.dao.UsuarioDAO;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
public class TelaLogin extends JFrame {
    private final JTextField usuario=new JTextField(18);
    private final JPasswordField senha=new JPasswordField(18);
    public TelaLogin(){
        super("Valiantus - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);setSize(430,340);setLocationRelativeTo(null);setResizable(false);
        JPanel painel=new JPanel(new GridBagLayout());painel.setBackground(Estilo.FUNDO);
        GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(9,12,9,12);g.fill=GridBagConstraints.HORIZONTAL;g.gridx=0;g.gridwidth=2;
        JLabel titulo=Estilo.titulo("VALIANTUS");titulo.setHorizontalAlignment(SwingConstants.CENTER);painel.add(titulo,g);
        g.gridy=1;JLabel sub=new JLabel("PROTEÇÃO VEICULAR",SwingConstants.CENTER);painel.add(sub,g);
        g.gridwidth=1;g.gridy=2;painel.add(new JLabel("Usuário:"),g);g.gridx=1;painel.add(usuario,g);
        g.gridx=0;g.gridy=3;painel.add(new JLabel("Senha:"),g);g.gridx=1;painel.add(senha,g);
        JButton entrar=Estilo.botao("Entrar"),sair=new JButton("Sair");
        g.gridx=0;g.gridy=4;painel.add(entrar,g);g.gridx=1;painel.add(sair,g);
        entrar.addActionListener(e->entrar());senha.addActionListener(e->entrar());sair.addActionListener(e->dispose());
        add(painel);
    }
    private void entrar(){
        char[] valor=senha.getPassword();
        try{
            if(new UsuarioDAO().autenticar(usuario.getText().trim(),valor)){
                dispose();new TelaPrincipal().setVisible(true);
            }else JOptionPane.showMessageDialog(this,"Usuário ou senha incorretos.");
        }catch(Exception ex){JOptionPane.showMessageDialog(this,"Erro ao acessar o banco: "+ex.getMessage());}
        finally{Arrays.fill(valor,'\0');senha.setText("");}
    }
}
