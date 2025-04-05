package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Estoque;
import util.DBConnection;

public class EstoqueDAO {

    public List<Estoque> listarEstoque() {
        List<Estoque> lista = new ArrayList<>();
        String sql = "SELECT id, nome, quantidade FROM estoque";  // Agora consulta a tabela estoque

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Estoque estoque = new Estoque();
                estoque.setId(rs.getInt("id"));
                estoque.setNome(rs.getString("nome"));
                estoque.setQuantidade(rs.getInt("quantidade"));
                lista.add(estoque);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Estoque buscarPorId(int id) {
        String sql = "SELECT * FROM estoque WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Estoque estoque = new Estoque();
                estoque.setId(rs.getInt("id"));
                estoque.setNome(rs.getString("nome"));
                estoque.setQuantidade(rs.getInt("quantidade"));

                return estoque;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

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

    public void diminuirQuantidade(int id, int quantidadeVendida) {
        Estoque estoque = buscarPorId(id);
        if (estoque != null) {
            int novaQuantidade = estoque.getQuantidade() - quantidadeVendida;
            atualizarQuantidade(id, novaQuantidade);
        }
    }
}
