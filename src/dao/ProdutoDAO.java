package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Produto;
import util.DBConnection;

public class ProdutoDAO {
//--------------------------------------------------------------------------------------------
	public void inserir(Produto produto) {
	    String sqlProduto = "INSERT INTO produto (nome, descricao, preco_base) VALUES (?, ?, ?)";
	    String sqlEstoque = "INSERT INTO estoque (nome, quantidade) VALUES (?, ?)";  // Inserir no estoque

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto, PreparedStatement.RETURN_GENERATED_KEYS);
	         PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque)) {

	        // Inserir o produto
	        stmtProduto.setString(1, produto.getNome());
	        stmtProduto.setString(2, produto.getDescricao());
	        stmtProduto.setDouble(3, produto.getPrecoBase());
	        stmtProduto.executeUpdate();

	        // Obter o ID do produto inserido
	        ResultSet rs = stmtProduto.getGeneratedKeys();
	        if (rs.next()) {
	            int idProduto = rs.getInt(1);

	            // Inserir no estoque com quantidade 0
	            stmtEstoque.setString(1, produto.getNome());  // Usando o nome do produto
	            stmtEstoque.setInt(2, 0);  // Quantidade inicial é 0
	            stmtEstoque.executeUpdate();
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

  //--------------------------------------------------------------------------------------------
    public List<Produto> listar() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPrecoBase(rs.getDouble("preco_base"));
                lista.add(produto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
  //--------------------------------------------------------------------------------------------
    public Produto buscarPorId(int id) {
        String sql = "SELECT * FROM produto WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPrecoBase(rs.getDouble("preco_base"));
                return produto;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
  //--------------------------------------------------------------------------------------------
    public void atualizar(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, preco_base = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPrecoBase());
            stmt.setInt(4, produto.getId());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  //--------------------------------------------------------------------------------------------
    public void deletar(int id) {
        String sql = "DELETE FROM produto WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
