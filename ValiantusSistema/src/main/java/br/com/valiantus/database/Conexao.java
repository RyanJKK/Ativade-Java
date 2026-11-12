package br.com.valiantus.database;
import java.sql.*;
import java.nio.file.*;
public class Conexao {
    private static final String URL;
    static {
        Path pasta = Paths.get(System.getProperty("user.home"), ".valiantus-academico");
        try { Files.createDirectories(pasta); }
        catch (Exception e) { throw new ExceptionInInitializerError(e); }
        URL = "jdbc:sqlite:" + pasta.resolve("valiantus.db").toAbsolutePath();
    }
    public static Connection abrir() throws SQLException {
        Connection c = DriverManager.getConnection(URL);
        try (Statement s = c.createStatement()) { s.execute("PRAGMA foreign_keys = ON"); }
        return c;
    }
}
