package br.com.valiantus;
import br.com.valiantus.database.CriarBanco;
import br.com.valiantus.view.*;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        Estilo.configurar();
        try { CriarBanco.iniciar(); }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Não foi possível iniciar o banco de dados: "+e.getMessage());
            return;
        }
        SwingUtilities.invokeLater(()->new TelaLogin().setVisible(true));
    }
}
