package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Produto;
import util.DBConnection;

public class ProdutoDAO {
//--------------------------------------------------------------------------------------------
	public void inserir(Produto produto) {
	    String sqlProduto = "INSERT INTO produto (nome, descricao, preco_base, quantidade) VALUES (?, ?, ?, ?)";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto, PreparedStatement.RETURN_GENERATED_KEYS);)
	        {

	        // Inserir o produto
	        stmtProduto.setString(1, produto.getNome());
	        stmtProduto.setString(2, produto.getDescricao());
	        stmtProduto.setDouble(3, produto.getPrecoBase());
	        stmtProduto.setInt(4, 0);  // Quantidade inicial é 0
	        stmtProduto.executeUpdate();

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
                produto.setQuantidade(rs.getInt("quantidade"));
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
                produto.setQuantidade(rs.getInt("quantidade"));
                return produto;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
  //--------------------------------------------------------------------------------------------
    
    public void atualizar(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, preco_base = ?, quantidade = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPrecoBase());
            stmt.setInt(4, produto.getQuantidade());
            stmt.setInt(5, produto.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 //----------------------------------------------------------------   
    public void atualizarQuantidade(int id, int novaQuantidade) {
        String sql = "UPDATE estoque SET quantidade = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, novaQuantidade);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  //--------------------------------------------------------------------------------------------
    
    public void excluir(int id) {
    	String sqlPedidos = "DELETE FROM pedido WHERE id_produto = ?";
        String sqlProduto = "DELETE FROM produto WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Iniciar transação

            try (PreparedStatement stmtPedidos = conn.prepareStatement(sqlPedidos);
            	 PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto)) {

                // Exclui os pedidos primeiro
                stmtPedidos.setInt(1, id);
                stmtPedidos.executeUpdate();
                
            	// Exclui da tabela produto
                stmtProduto.setInt(1, id);
                stmtProduto.executeUpdate();

                conn.commit(); // Confirma as alterações

            } catch (SQLException e) {
                conn.rollback(); // Reverte se algo der errado
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
}
