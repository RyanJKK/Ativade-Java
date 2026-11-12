package br.com.valiantus.dao;
import br.com.valiantus.database.Conexao;
import br.com.valiantus.model.Veiculo;
import java.sql.*;
import java.util.*;
public class VeiculoDAO implements CrudDAO<Veiculo> {
    public void cadastrar(Veiculo x)throws SQLException{gravar(x,false);}
    public void atualizar(Veiculo x)throws SQLException{gravar(x,true);}
    private void gravar(Veiculo x,boolean atualizar)throws SQLException{
        String sql=atualizar?"UPDATE veiculos SET cliente_id=?,placa=?,marca=?,modelo=?,ano=?,cor=? WHERE id=?":
          "INSERT INTO veiculos(cliente_id,placa,marca,modelo,ano,cor) VALUES(?,?,?,?,?,?)";
        try(Connection c=Conexao.abrir();PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,x.getClienteId());p.setString(2,x.getPlaca());p.setString(3,x.getMarca());
            p.setString(4,x.getModelo());p.setInt(5,x.getAno());p.setString(6,x.getCor());
            if(atualizar)p.setInt(7,x.getId());
            if(p.executeUpdate()==0)throw new SQLException("Veículo não encontrado.");
        }
    }
    public void excluir(int id)throws SQLException{
        try(Connection c=Conexao.abrir();PreparedStatement p=c.prepareStatement("DELETE FROM veiculos WHERE id=?")){
            p.setInt(1,id);if(p.executeUpdate()==0)throw new SQLException("Veículo não encontrado.");
        }
    }
    public List<Veiculo> listar(String filtro)throws SQLException{
        List<Veiculo> lista=new ArrayList<>();
        try(Connection c=Conexao.abrir();PreparedStatement p=c.prepareStatement("SELECT * FROM veiculos WHERE placa LIKE ? OR marca LIKE ? OR modelo LIKE ? ORDER BY id DESC")){
            for(int i=1;i<=3;i++)p.setString(i,"%"+filtro+"%");
            try(ResultSet r=p.executeQuery()){while(r.next())lista.add(new Veiculo(r.getInt("id"),r.getInt("cliente_id"),r.getString("placa"),r.getString("marca"),r.getString("modelo"),r.getInt("ano"),r.getString("cor")));}
        }
        return lista;
    }
}
