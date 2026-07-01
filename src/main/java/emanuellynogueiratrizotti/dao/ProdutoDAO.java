package emanuellynogueiratrizotti.dao;

import emanuellynogueiratrizotti.model.Produto;
import emanuellynogueiratrizotti.util.ConexaoDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    // 1. Inserir
    public void salvar(Produto p) {
        String sql = "INSERT INTO produtos (nome, preco, categoria, tempo_preparo) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setDouble(2, p.getPreco());
            stmt.setString(3, p.getCategoria());
            stmt.setInt(4, p.getTempoPreparo());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Erro ao salvar: " + e.getMessage()); }
    }

    // 2. Listar Todos
    public List<Produto> listarTodos() {
        return buscarProdutos("SELECT * FROM produtos ORDER BY id_produto ASC");
    }

    // 3. Listar por Categoria
    public List<Produto> listarPorCategoria(String categoria) {
        String sql = "SELECT * FROM produtos WHERE categoria = ?";
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return produtos;
    }

    // 4. Remover
    public void remover(int idProduto) {
        String sql = "DELETE FROM produtos WHERE id_produto = ?";
        try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProduto);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
 
    // 5. Alterar
    public void alterar(Produto p) {
    String sql = "UPDATE produtos SET nome=?, preco=?, categoria=?, tempo_preparo=? WHERE id_produto=?";
    try (Connection conn = ConexaoDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, p.getNome());
        stmt.setDouble(2, p.getPreco());
        stmt.setString(3, p.getCategoria());
        stmt.setInt(4, p.getTempoPreparo());
        stmt.setInt(5, p.getIdProduto()); // ID é essencial aqui!
        stmt.executeUpdate();
    } catch (SQLException e) { throw new RuntimeException(e); }
}

    // Método auxiliar para evitar repetição de código
    private Produto mapearProduto(ResultSet rs) throws SQLException {
        return new Produto(rs.getInt("id_produto"), rs.getString("nome"), 
                           rs.getDouble("preco"), rs.getString("categoria"), rs.getInt("tempo_preparo"));
    }

    private List<Produto> buscarProdutos(String sql) {
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = ConexaoDB.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) { produtos.add(mapearProduto(rs)); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return produtos;
    }
}