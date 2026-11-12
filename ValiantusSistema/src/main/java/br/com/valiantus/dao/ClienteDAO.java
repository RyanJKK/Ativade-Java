package br.com.valiantus.dao;
import br.com.valiantus.database.Conexao;
import br.com.valiantus.model.Cliente;
import java.sql.*;
import java.util.*;
public class ClienteDAO implements CrudDAO<Cliente> {
    public void cadastrar(Cliente x) throws SQLException { gravar(x,false); }
    public void atualizar(Cliente x) throws SQLException { gravar(x,true); }
    private void gravar(Cliente x,boolean atualizar) throws SQLException {
        String sql=atualizar?"UPDATE clientes SET nome=?,cpf=?,telefone=?,email=?,endereco=?,cidade=?,estado=? WHERE id=?":
          "INSERT INTO clientes(nome,cpf,telefone,email,endereco,cidade,estado) VALUES(?,?,?,?,?,?,?)";
        try(Connection c=Conexao.abrir();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,x.getNome());p.setString(2,x.getCpf());p.setString(3,x.getTelefone());p.setString(4,x.getEmail());
            p.setString(5,x.getEndereco());p.setString(6,x.getCidade());p.setString(7,x.getEstado());
            if(atualizar)p.setInt(8,x.getId());
            if(p.executeUpdate()==0)throw new SQLException("Cliente não encontrado.");
        }
    }
    public void excluir(int id)throws SQLException{
        try(Connection c=Conexao.abrir();PreparedStatement p=c.prepareStatement("DELETE FROM clientes WHERE id=?")){
            p.setInt(1,id);if(p.executeUpdate()==0)throw new SQLException("Cliente não encontrado.");
        }
    }
    public List<Cliente> listar(String filtro)throws SQLException{
        List<Cliente> lista=new ArrayList<>();
        try(Connection c=Conexao.abrir();PreparedStatement p=c.prepareStatement("SELECT * FROM clientes WHERE nome LIKE ? OR cpf LIKE ? ORDER BY nome")){
            p.setString(1,"%"+filtro+"%");p.setString(2,"%"+filtro+"%");
            try(ResultSet r=p.executeQuery()){while(r.next())lista.add(new Cliente(r.getInt("id"),r.getString("nome"),r.getString("cpf"),r.getString("telefone"),r.getString("email"),r.getString("endereco"),r.getString("cidade"),r.getString("estado")));}
        }
        return lista;
    }
}
