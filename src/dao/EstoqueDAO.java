package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Estoque;
import model.Produto;
import util.DBConnection;

public class EstoqueDAO {

    public List<Estoque> listarEstoque() {
        List<Estoque> lista = new ArrayList<>();
        String sql = "SELECT e.id, e.quantidade, p.id AS produto_id, p.nome, p.descricao, p.preco_base " +
                     "FROM estoque e JOIN produto p ON e.produto_id = p.id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("produto_id"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPrecoBase(rs.getDouble("preco_base"));

                Estoque estoque = new Estoque();
                estoque.setId(rs.getInt("id"));
                estoque.setProduto(produto);
                estoque.setQuantidade(rs.getInt("quantidade"));

                lista.add(estoque);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    public Estoque buscarPorProdutoId(int produtoId) {
        String sql = "SELECT * FROM estoque WHERE id_produto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produtoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Estoque estoque = new Estoque();
                Produto produto = new Produto();
                produto.setId(rs.getInt("id_produto"));

                estoque.setId(rs.getInt("id"));
                estoque.setProduto(produto);
                estoque.setQuantidade(rs.getInt("quantidade"));

                return estoque;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void atualizarQuantidade(int produtoId, int novaQuantidade) {
        String sql = "UPDATE estoque SET quantidade = ? WHERE id_produto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, novaQuantidade);
            stmt.setInt(2, produtoId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void diminuirQuantidade(int produtoId, int quantidadeVendida) {
        Estoque estoque = buscarPorProdutoId(produtoId);
        if (estoque != null) {
            int novaQuantidade = estoque.getQuantidade() - quantidadeVendida;
            atualizarQuantidade(produtoId, novaQuantidade);
        }
    }
}
