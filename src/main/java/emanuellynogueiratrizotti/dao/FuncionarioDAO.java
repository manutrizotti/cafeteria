package emanuellynogueiratrizotti.dao;

import emanuellynogueiratrizotti.model.Funcionario;
import emanuellynogueiratrizotti.util.ConexaoDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    // 1. Inserir
    public void salvar(Funcionario f) {
        String sql = "INSERT INTO funcionarios (nome, idade, cargo) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, f.getNome());
            stmt.setInt(2, f.getIdade());
            stmt.setString(3, f.getCargo());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Erro ao salvar funcionário: " + e.getMessage()); }
    }

    // 2. Listar Todos
    public List<Funcionario> listarTodos() {
        String sql = "SELECT * FROM funcionarios ORDER BY id_funcionario ASC";
        List<Funcionario> lista = new ArrayList<>();
        try (Connection conn = ConexaoDB.conectar(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearFuncionario(rs));
            }
        } catch (SQLException e) { throw new RuntimeException("Erro ao listar funcionários: " + e.getMessage()); }
        return lista;
    }

    // 3. Remover
    public void remover(int idFuncionario) {
        String sql = "DELETE FROM funcionarios WHERE id_funcionario = ?";
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFuncionario);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Erro ao remover funcionário: " + e.getMessage()); }
    }

    // 4. Mapeador (A ponte entre o Banco e o Java)
    private Funcionario mapearFuncionario(ResultSet rs) throws SQLException {
        Funcionario f = new Funcionario();
        f.setIdFuncionario(rs.getInt("id_funcionario"));
        f.setNome(rs.getString("nome"));
        f.setIdade(rs.getInt("idade"));
        f.setCargo(rs.getString("cargo"));
        return f;
    }
    
    // 5. Alterar
    public void alterar(Funcionario f) {
        String sql = "UPDATE funcionarios SET nome = ?, idade = ?, cargo = ? WHERE id_funcionario = ?";
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, f.getNome());
            stmt.setInt(2, f.getIdade());
            stmt.setString(3, f.getCargo());
            stmt.setInt(4, f.getIdFuncionario()); // O ID da linha que será alterada
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Erro ao alterar funcionário: " + e.getMessage()); }
    }
}