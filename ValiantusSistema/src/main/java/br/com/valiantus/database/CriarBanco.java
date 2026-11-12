package br.com.valiantus.database;
import br.com.valiantus.service.AutenticacaoService;
import java.sql.*;
public class CriarBanco {
    public static void iniciar() throws Exception {
        try (Connection c = Conexao.abrir(); Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE IF NOT EXISTS usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, usuario TEXT NOT NULL UNIQUE, senha_hash TEXT NOT NULL)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS clientes (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, cpf TEXT NOT NULL UNIQUE, telefone TEXT, email TEXT, endereco TEXT, cidade TEXT, estado TEXT)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS veiculos (id INTEGER PRIMARY KEY AUTOINCREMENT, cliente_id INTEGER NOT NULL, placa TEXT NOT NULL UNIQUE, marca TEXT NOT NULL, modelo TEXT NOT NULL, ano INTEGER NOT NULL, cor TEXT, FOREIGN KEY(cliente_id) REFERENCES clientes(id) ON DELETE RESTRICT)");
            try (ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM usuarios")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    try (PreparedStatement p = c.prepareStatement("INSERT INTO usuarios(usuario,senha_hash) VALUES (?,?)")) {
                        p.setString(1,"admin"); p.setString(2, AutenticacaoService.gerarHash("Admin@123".toCharArray())); p.executeUpdate();
                    }
                }
            }
        }
    }
}
