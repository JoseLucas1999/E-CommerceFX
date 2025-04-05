package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;
import model.Pedido;
import model.Produto;
import model.StatusPedido;
import util.DBConnection;


public class PedidoDAO {

//-------------------------------------------------------------------------------------------
    public void salvarPedido(Pedido pedido) {
        String sql = "INSERT INTO pedido (id_cliente, id_produto, data_pedido, data_entrega, valor_total, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Define a data atual como data do pedido
            String dataPedido = LocalDate.now().toString();
            pedido.setDataPedido(dataPedido);

            stmt.setInt(1, pedido.getCliente().getId());
            stmt.setInt(2, pedido.getProduto().getId());
            stmt.setString(3, pedido.getDataPedido());
            stmt.setString(4, pedido.getDataEntrega());
            stmt.setDouble(5, pedido.getValorTotal());
            stmt.setString(6, pedido.getStatus().name());

            stmt.executeUpdate();

            // Recupera o ID gerado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                pedido.setId(rs.getInt(1));
            }

            // Atualiza o estoque após salvar o pedido
            EstoqueDAO estoqueDAO = new EstoqueDAO();
            estoqueDAO.diminuirQuantidade(pedido.getProduto().getId(), 1); // Altere 1 para a quantidade real, se houver

        } catch (Exception e) {
            System.out.println("Erro ao salvar o pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

  //-------------------------------------------------------------------------------------------
    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nome as nome_cliente, pr.nome as nome_produto " +
                     "FROM pedido p " +
                     "JOIN cliente c ON p.id_cliente = c.id " +
                     "JOIN produto pr ON p.id_produto = pr.id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt("id"));

                Cliente c = new Cliente();
                c.setId(rs.getInt("id_cliente"));
                c.setNome(rs.getString("nome_cliente")); // Nome importante para a tabela

                Produto pr = new Produto();
                pr.setId(rs.getInt("id_produto"));
                pr.setNome(rs.getString("nome_produto")); // Nome importante para a tabela

                p.setCliente(c);
                p.setProduto(pr);
                p.setDataPedido(rs.getString("data_pedido"));
                p.setDataEntrega(rs.getString("data_entrega"));
                p.setValorTotal(rs.getDouble("valor_total"));
                p.setStatus(StatusPedido.valueOf(rs.getString("status")));

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    //-------------------------------------------------------------------------------------------    
    public List<Pedido> listarPorStatus(StatusPedido status) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, c.nome as nome_cliente, pr.nome as nome_produto " +
                     "FROM pedido p " +
                     "JOIN cliente c ON p.id_cliente = c.id " +
                     "JOIN produto pr ON p.id_produto = pr.id " +
                     "WHERE p.status = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt("id"));

                Cliente c = new Cliente();
                c.setId(rs.getInt("id_cliente"));
                c.setNome(rs.getString("nome_cliente"));

                Produto pr = new Produto();
                pr.setId(rs.getInt("id_produto"));
                pr.setNome(rs.getString("nome_produto"));

                p.setCliente(c);
                p.setProduto(pr);
                p.setDataPedido(rs.getString("data_pedido"));
                p.setDataEntrega(rs.getString("data_entrega"));
                p.setValorTotal(rs.getDouble("valor_total"));
                p.setStatus(StatusPedido.valueOf(rs.getString("status")));

                pedidos.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pedidos;
    }

  //-------------------------------------------------------------------------------------------
    public List<Pedido> listarPedidosProntosParaEntrega() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, c.id as cid, c.nome as cnome, c.telefone, c.endereco, c.email, " +
                     "pr.id as pid, pr.nome as pnome, pr.descricao, pr.preco_base " +
                     "FROM pedido p " +
                     "JOIN cliente c ON p.id_cliente = c.id " +
                     "JOIN produto pr ON p.id_produto = pr.id " +
                     "WHERE p.status = 'PRONTO_PARA_ENTREGA'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Cliente
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("cid"));
                cliente.setNome(rs.getString("cnome"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEndereco(rs.getString("endereco"));
                cliente.setEmail(rs.getString("email"));

                // Produto
                Produto produto = new Produto();
                produto.setId(rs.getInt("pid"));
                produto.setNome(rs.getString("pnome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPrecoBase(rs.getDouble("preco_base"));

                // Pedido
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setCliente(cliente);
                pedido.setProduto(produto);
                pedido.setDataPedido(rs.getString("data_pedido"));
                pedido.setDataEntrega(rs.getString("data_entrega"));
                pedido.setValorTotal(rs.getDouble("valor"));
                pedido.setStatus(StatusPedido.valueOf(rs.getString("status")));

                pedidos.add(pedido);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pedidos;
    }

  //-------------------------------------------------------------------------------------------
    public void atualizarStatus(int idPedido, StatusPedido novoStatus) {
        String sql;

        if (novoStatus == StatusPedido.ENTREGUE) {
            sql = "UPDATE pedido SET status = ?, data_entrega = CURRENT_DATE WHERE id = ?";
        } else {
            sql = "UPDATE pedido SET status = ? WHERE id = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoStatus.name());
            stmt.setInt(2, idPedido);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


  //-------------------------------------------------------------------------------------------
    public void deletar(int pedidoId) {
        String sql = "DELETE FROM pedido WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pedidoId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//-------------------------------------------------------------------------------------------
    public void atualizarDataEntrega(int pedidoId, String dataEntrega) {
        String sql = "UPDATE pedido SET data_entrega = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dataEntrega);
            stmt.setInt(2, pedidoId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
