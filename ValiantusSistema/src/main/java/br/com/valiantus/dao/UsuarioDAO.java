package br.com.valiantus.dao;
import br.com.valiantus.database.Conexao;
import br.com.valiantus.service.AutenticacaoService;
import java.sql.*;
public class UsuarioDAO {
    public boolean autenticar(String usuario,char[] senha)throws Exception{
        try(Connection c=Conexao.abrir();PreparedStatement p=c.prepareStatement("SELECT senha_hash FROM usuarios WHERE usuario=?")){
            p.setString(1,usuario);
            try(ResultSet r=p.executeQuery()){return r.next() && AutenticacaoService.verificar(senha,r.getString(1));}
        }
    }
}
