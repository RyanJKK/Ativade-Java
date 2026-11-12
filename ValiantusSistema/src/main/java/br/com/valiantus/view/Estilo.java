package br.com.valiantus.view;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
public class Estilo {
    public static final Color PRETO=new Color(25,25,25), VERMELHO=new Color(190,30,38), FUNDO=new Color(245,245,245);
    public static void configurar(){
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch(Exception ignored){}
    }
    public static JButton botao(String nome){
        JButton b=new JButton(nome);
        b.setUI(new BasicButtonUI());
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(145,20,27)),
            BorderFactory.createEmptyBorder(9,16,9,16)));
        b.setBackground(VERMELHO);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif",Font.BOLD,13));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
    public static JLabel titulo(String texto){
        JLabel l=new JLabel(texto);
        l.setFont(new Font("SansSerif",Font.BOLD,21));
        l.setForeground(PRETO);
        return l;
    }
}
