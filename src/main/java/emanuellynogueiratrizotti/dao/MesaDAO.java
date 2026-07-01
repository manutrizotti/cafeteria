package emanuellynogueiratrizotti.dao;

import emanuellynogueiratrizotti.model.Mesa;
import emanuellynogueiratrizotti.util.ConexaoDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MesaDAO {

    // 1. Inserir Mesa
    public void salvar(Mesa m) {
        String sql = "INSERT INTO mesas (numero, status_ocupacao) VALUES (?, ?)";
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, m.getNumero());
            stmt.setString(2, m.getStatusOcupacao());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Erro ao salvar mesa: " + e.getMessage()); }
    }

    // 2. Atualizar Status (Essencial para o sistema de ocupação)
    public void atualizarStatus(int idMesa, String novoStatus) {
        String sql = "UPDATE mesas SET status_ocupacao = ? WHERE id_mesa = ?";
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoStatus);
            stmt.setInt(2, idMesa);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Erro ao atualizar status: " + e.getMessage()); }
    }

    // 3. Listar todas
    public List<Mesa> listarTodas() {
        String sql = "SELECT * FROM mesas ORDER BY id_mesa ASC";
        List<Mesa> lista = new ArrayList<>();
        try (Connection conn = ConexaoDB.conectar(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearMesa(rs));
            }
        } catch (SQLException e) { throw new RuntimeException("Erro ao listar mesas: " + e.getMessage()); }
        return lista;
    }

    // 4. Remover
    public void remover(int idMesa) {
        String sql = "DELETE FROM mesas WHERE id_mesa = ?";
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMesa);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Erro ao remover mesa: " + e.getMessage()); }
    }
    
    // 5. Contar mesas disponíveis
    public int contarMesasDisponiveis() {
        String sql = "SELECT COUNT(*) FROM mesas WHERE status_ocupacao = 'Disponível'";
        try (Connection conn = ConexaoDB.conectar(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1); // Retorna o valor da contagem
            }
        } catch (SQLException e) { 
            throw new RuntimeException("Erro ao contar mesas: " + e.getMessage()); 
        }
        return 0;
    }
    
    // 6. Listar mesas filtradas por status
    public List<Mesa> listarPorStatus(String status) {
        String sql = "SELECT * FROM mesas WHERE status_ocupacao = ? ORDER BY numero";
        List<Mesa> lista = new ArrayList<>();
        
        try (Connection conn = ConexaoDB.conectar(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearMesa(rs));
            }
        } catch (SQLException e) { 
            throw new RuntimeException("Erro ao filtrar mesas: " + e.getMessage()); 
        }
        return lista;
    }
    
    // 7. Alterar
    public void alterar(Mesa m) {
        String sql = "UPDATE mesas SET numero = ?, status_ocupacao = ? WHERE id_mesa = ?";
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, m.getNumero());
            stmt.setString(2, m.getStatusOcupacao());
            stmt.setInt(3, m.getIdMesa());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Erro ao alterar mesa: " + e.getMessage()); }
    }

    // Método auxiliar de mapeamento
    private Mesa mapearMesa(ResultSet rs) throws SQLException {
        Mesa m = new Mesa();
        m.setIdMesa(rs.getInt("id_mesa"));
        m.setNumero(rs.getInt("numero"));
        m.setStatusOcupacao(rs.getString("status_ocupacao"));
        return m;
    }
}