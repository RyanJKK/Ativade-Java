package br.com.valiantus.dao;
import java.sql.SQLException;
import java.util.List;
public interface CrudDAO<T> {
    void cadastrar(T objeto) throws SQLException;
    void atualizar(T objeto) throws SQLException;
    void excluir(int id) throws SQLException;
    List<T> listar(String filtro) throws SQLException;
}
