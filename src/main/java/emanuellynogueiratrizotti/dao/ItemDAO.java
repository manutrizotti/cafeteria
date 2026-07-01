package emanuellynogueiratrizotti.dao;

import emanuellynogueiratrizotti.model.Item;
import emanuellynogueiratrizotti.util.ConexaoDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    // 1. Adicionar item à comanda (Com lógica inteligente de soma)
    public void adicionarItem(Item i) {
        // Passo 1: Verificar se o produto já foi lançado nesta comanda
        String sqlVerificar = "SELECT qtd FROM itens WHERE id_comanda = ? AND id_produto = ?";
        
        try (Connection conn = ConexaoDB.conectar(); 
             PreparedStatement stmtVerificar = conn.prepareStatement(sqlVerificar)) {
            
            stmtVerificar.setInt(1, i.getIdComanda());
            stmtVerificar.setInt(2, i.getIdProduto());
            ResultSet rs = stmtVerificar.executeQuery();
            
            if (rs.next()) {
                // Passo 2 (Cenário A): O item JÁ EXISTE. 
                // Pegamos a quantidade que já estava no banco e somamos com a nova que o garçom digitou.
                int qtdExistente = rs.getInt("qtd");
                int novaQtd = qtdExistente + i.getQtd();
                
                // Atualiza a quantidade e o cozinheiro (fica registrado o último cozinheiro que mexeu no pedido)
                String sqlUpdate = "UPDATE itens SET qtd = ?, id_funcionario_cozinheiro = ? WHERE id_comanda = ? AND id_produto = ?";
                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                    stmtUpdate.setInt(1, novaQtd);
                    stmtUpdate.setInt(2, i.getIdFuncionarioCozinheiro());
                    stmtUpdate.setInt(3, i.getIdComanda());
                    stmtUpdate.setInt(4, i.getIdProduto());
                    stmtUpdate.executeUpdate();
                }
                
            } else {
                // Passo 3 (Cenário B): O item NÃO EXISTE na comanda. Faz o INSERT normal.
                String sqlInsert = "INSERT INTO itens (id_comanda, id_produto, qtd, id_funcionario_cozinheiro) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                    stmtInsert.setInt(1, i.getIdComanda());
                    stmtInsert.setInt(2, i.getIdProduto());
                    stmtInsert.setInt(3, i.getQtd());
                    stmtInsert.setInt(4, i.getIdFuncionarioCozinheiro());
                    stmtInsert.executeUpdate();
                }
            }
            
        } catch (SQLException e) { 
            throw new RuntimeException("Erro ao processar item: " + e.getMessage()); 
        }
    }

    // 2. Listar itens de uma comanda específica (JOIN para trazer os nomes)
    public List<Item> listarPorComanda(int idComanda) {
        String sql = "SELECT i.*, p.nome AS nome_produto, f.nome AS nome_cozinheiro " +
                     "FROM itens i " +
                     "JOIN produtos p ON i.id_produto = p.id_produto " +
                     "JOIN funcionarios f ON i.id_funcionario_cozinheiro = f.id_funcionario " +
                     "WHERE i.id_comanda = ?";
        
        List<Item> lista = new ArrayList<>();
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idComanda);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearItem(rs));
            }
        } catch (SQLException e) { 
            throw new RuntimeException("Erro ao listar itens: " + e.getMessage()); 
        }
        return lista;
    }

    // 3. Remover item da comanda
    public void removerItem(int idComanda, int idProduto) {
        String sql = "DELETE FROM itens WHERE id_comanda = ? AND id_produto = ?";
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idComanda);
            stmt.setInt(2, idProduto);
            stmt.executeUpdate();
        } catch (SQLException e) { 
            throw new RuntimeException("Erro ao remover item: " + e.getMessage()); 
        }
    }

    // Método auxiliar de mapeamento inteligente
    private Item mapearItem(ResultSet rs) throws SQLException {
        Item i = new Item();
        i.setIdComanda(rs.getInt("id_comanda"));
        i.setIdProduto(rs.getInt("id_produto"));
        i.setQtd(rs.getInt("qtd"));
        i.setIdFuncionarioCozinheiro(rs.getInt("id_funcionario_cozinheiro"));
        
        // Bloco de segurança: tenta buscar as colunas de texto do JOIN se elas existirem na consulta
        try {
            i.setNomeProduto(rs.getString("nome_produto"));
            i.setNomeCozinheiro(rs.getString("nome_cozinheiro"));
        } catch (SQLException e) {
            // Se o método for chamado em um SELECT simples sem JOIN, ignora os nomes para não dar erro
        }
        
        return i;
    }
}