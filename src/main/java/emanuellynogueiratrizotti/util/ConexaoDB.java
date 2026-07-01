package emanuellynogueiratrizotti.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
    
    // Credenciais de conexão
    private static final String URL = "jdbc:postgresql://localhost:5432/cafeteria_db";
    private static final String USER = "java_user";
    private static final String PASSWORD = "123456";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Falha na conexão com o banco de dados!", e);
        }
    }
}