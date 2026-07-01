package emanuellynogueiratrizotti.dao;

import emanuellynogueiratrizotti.model.Comanda;
import emanuellynogueiratrizotti.util.ConexaoDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComandaDAO {

    // 1. Inserir (Abrir comanda)
    public void abrirComanda(Comanda c) {
        String sql = "INSERT INTO comandas (id_mesa, id_funcionario_caixa, id_funcionario_garcom) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, c.getIdMesa());
            stmt.setInt(2, c.getIdFuncionarioCaixa());
            stmt.setInt(3, c.getIdFuncionarioGarcom());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Erro ao abrir comanda: " + e.getMessage()); }
    }

    // 2. Listar por status (ABERTA ou FECHADA)
    public List<Comanda> listarPorStatus(String status) {
        String sql = "SELECT * FROM comandas WHERE status = ?";
        List<Comanda> lista = new ArrayList<>();
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) { 
                lista.add(mapearComanda(rs)); 
            }
        } catch (SQLException e) { throw new RuntimeException("Erro ao listar por status: " + e.getMessage()); }
        return lista;
    }

    // 3. Calcular Total com Gorjeta
    public double calcularTotal(int idComanda, boolean incluirGorjeta) {
        String sql = "SELECT c.gorjeta, " +
                     "COALESCE(SUM(p.preco * i.qtd), 0) AS subtotal " +
                     "FROM comandas c " +
                     "LEFT JOIN itens i ON c.id_comanda = i.id_comanda " +
                     "LEFT JOIN produtos p ON i.id_produto = p.id_produto " +
                     "WHERE c.id_comanda = ? " +
                     "GROUP BY c.gorjeta";
        
        double total = 0;
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idComanda);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                double subtotal = rs.getDouble("subtotal");
                double gorjetaBD = rs.getDouble("gorjeta");
                
                total = subtotal; // Valor inicial é apenas o consumo dos itens
                
                if (incluirGorjeta) {
                    total += gorjetaBD; // Adiciona o valor exato gravado no banco
                }
            }
        } catch (SQLException e) { throw new RuntimeException("Erro ao calcular total: " + e.getMessage()); }
        return total;
    }

    // 4. Contar abertas
    public int contarAbertas() {
        String sql = "SELECT COUNT(*) FROM comandas WHERE status = 'ABERTA'";
        try (Connection conn = ConexaoDB.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { throw new RuntimeException("Erro ao contar abertas: " + e.getMessage()); }
        return 0;
    }
    
    // 5. Listar todas
    public List<Comanda> listarTodas() {
        String sql = "SELECT * FROM comandas ORDER BY data DESC";
        List<Comanda> lista = new ArrayList<>();
        try (Connection conn = ConexaoDB.conectar(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(mapearComanda(rs));
            }
        } catch (SQLException e) { throw new RuntimeException("Erro ao listar todas: " + e.getMessage()); }
        return lista;
    }

    // 6. Remover comanda
    public void remover(int idComanda) {
        String sql = "DELETE FROM comandas WHERE id_comanda = ?";
        try (Connection conn = ConexaoDB.conectar(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idComanda);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Erro ao remover: " + e.getMessage()); }
    }
    
    // 7. Alterar Comanda (Para fechar a conta, adicionar forma de pgto, etc)
    public void alterar(Comanda c) {
        String sql = "UPDATE comandas SET status = ?, id_mesa = ?, id_funcionario_caixa = ?, " +
                     "forma_pgto = ?, id_funcionario_garcom = ?, gorjeta = ? WHERE id_comanda = ?";
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getStatus());
            stmt.setInt(2, c.getIdMesa());
            stmt.setInt(3, c.getIdFuncionarioCaixa());
            stmt.setString(4, c.getFormaPgto());
            stmt.setInt(5, c.getIdFuncionarioGarcom());
            stmt.setDouble(6, c.getGorjeta());
            stmt.setInt(7, c.getIdComanda());
            stmt.executeUpdate();
        } catch (SQLException e) { 
            throw new RuntimeException("Erro ao alterar comanda: " + e.getMessage()); 
        }
    }

    // 8. Subconsulta + Agregação na mesma instrução
    // Lista comandas cujo valor total (itens + gorjeta) seja maior que um valor estipulado
    public List<Comanda> listarComandasAcimaDeValor(double valorMinimo) {
        String sql = "SELECT c.* FROM comandas c " +
                     "WHERE (COALESCE((SELECT SUM(p.preco * i.qtd) " +
                     "                 FROM itens i " +
                     "                 JOIN produtos p ON i.id_produto = p.id_produto " +
                     "                 WHERE i.id_comanda = c.id_comanda), 0) + COALESCE(c.gorjeta, 0)) > ? " +
                     "ORDER BY c.data DESC";
        
        List<Comanda> lista = new ArrayList<>();
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, valorMinimo); 
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearComanda(rs));
            }
        } catch (SQLException e) { 
            throw new RuntimeException("Erro na consulta de agregação: " + e.getMessage()); 
        }
        return lista;
    }
    
    // NOVO MÉTODO: Listar junção de Comandas e Funcionários (Garçons)
    public List<String> listarComandasEGarcons() {
        // Junção entre a tabela comandas e a tabela funcionarios
        String sql = "SELECT c.id_comanda, c.status, f.nome AS nome_garcom " +
                     "FROM comandas c " +
                     "JOIN funcionarios f ON c.id_funcionario_garcom = f.id_funcionario " +
                     "ORDER BY c.id_comanda DESC";
        
        List<String> listaResultados = new ArrayList<>();
        
        try (Connection conn = ConexaoDB.conectar(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int idComanda = rs.getInt("id_comanda");
                String status = rs.getString("status");
                String nomeGarcom = rs.getString("nome_garcom");
                
                // Formata o resultado mesclado para a lista
                String linha = "Comanda: " + idComanda + " | Status: " + status + " | Garçom: " + nomeGarcom;
                listaResultados.add(linha);
            }
        } catch (SQLException e) { 
            throw new RuntimeException("Erro ao listar com junção: " + e.getMessage()); 
        }
        
        return listaResultados;
    }

    // MÉTODO AUXILIAR QUE FALTAVA (O "MAQUINA DE MAPEAMENTO")
    private Comanda mapearComanda(ResultSet rs) throws SQLException {
        Comanda c = new Comanda();
        c.setIdComanda(rs.getInt("id_comanda"));
        c.setStatus(rs.getString("status"));
        c.setData(rs.getTimestamp("data"));
        c.setIdMesa(rs.getInt("id_mesa"));
        c.setIdFuncionarioCaixa(rs.getInt("id_funcionario_caixa"));
        c.setFormaPgto(rs.getString("forma_pgto"));
        c.setIdFuncionarioGarcom(rs.getInt("id_funcionario_garcom"));
        c.setGorjeta(rs.getDouble("gorjeta"));
        return c;
    }
}