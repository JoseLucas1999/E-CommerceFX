package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import dao.ClienteDAO;
import dao.ProdutoDAO;
import dao.PedidoDAO;
import model.*;

import java.time.LocalDate;
import java.util.List;

public class FazerPedidoView {

    public Scene getScene(Stage stage) {
        Label title = new Label("Novo Pedido");

        ComboBox<Cliente> clienteCombo = new ComboBox<>();
        ComboBox<Produto> produtoCombo = new ComboBox<>();
        DatePicker dataEntregaPicker = new DatePicker();
        TextField valorTotalField = new TextField();

        clienteCombo.setPromptText("Selecione o cliente");
        produtoCombo.setPromptText("Selecione o produto");
        dataEntregaPicker.setPromptText("Data de entrega");
        valorTotalField.setPromptText("Valor total (R$)");

        // Carrega clientes e produtos
        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> clientes = clienteDAO.listarTodos();
        clienteCombo.getItems().addAll(clientes);

        ProdutoDAO produtoDAO = new ProdutoDAO();
        List<Produto> produtos = produtoDAO.listar();
        produtoCombo.getItems().addAll(produtos);

        Button salvarBtn = new Button("Salvar Pedido");
        Label msgLabel = new Label();

        salvarBtn.setOnAction(e -> {
            try {
                Cliente cliente = clienteCombo.getValue();
                Produto produto = produtoCombo.getValue();
                LocalDate dataEntrega = dataEntregaPicker.getValue();
                double valorTotal = Double.parseDouble(valorTotalField.getText());

                if (cliente == null || produto == null || dataEntrega == null) {
                    msgLabel.setText("❌ Preencha todos os campos.");
                    return;
                }

                String dataPedido = LocalDate.now().toString();
                Pedido pedido = new Pedido(
                        0,
                        cliente,
                        produto,
                        dataPedido,
                        dataEntrega.toString(),
                        valorTotal,
                        StatusPedido.AGUARDANDO_PRODUCAO,
                        StatusPagamento.PENDENTE
                );

                PedidoDAO pedidoDAO = new PedidoDAO();
                pedidoDAO.salvarPedido(pedido);

                msgLabel.setText("✅ Pedido cadastrado com sucesso!");
                clienteCombo.setValue(null);
                produtoCombo.setValue(null);
                dataEntregaPicker.setValue(null);
                valorTotalField.clear();

            } catch (Exception ex) {
                msgLabel.setText("❌ Erro ao cadastrar o pedido.");
                ex.printStackTrace();
            }
        });

        Button voltarBtn = new Button("Voltar");
        voltarBtn.setOnAction(e -> {
            MenuView menu = new MenuView();
            stage.setScene(menu.getScene(stage));
        });
        
        title.getStyleClass().add("label-title");
        clienteCombo.getStyleClass().add("combo-box");
        produtoCombo.getStyleClass().add("combo-box");
        dataEntregaPicker.getStyleClass().add("date-picker");
        valorTotalField.getStyleClass().add("text-field");
        salvarBtn.getStyleClass().add("button");
        voltarBtn.getStyleClass().add("button");
        msgLabel.getStyleClass().add("label-msg");

        /*VBox vbox = new VBox(10, title, clienteCombo, produtoCombo, dataEntregaPicker, valorTotalField, salvarBtn, voltarBtn, msgLabel);
        vbox.getStyleClass().add("vbox-pedido");

        Scene scene = new Scene(vbox, 400, 400);
        scene.getStylesheets().add(getClass().getResource("pedido.css").toExternalForm());
        return scene;*/
        
        VBox vbox = new VBox(10, title, clienteCombo, produtoCombo, dataEntregaPicker, valorTotalField, salvarBtn, voltarBtn, msgLabel);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        return new Scene(vbox, 400, 400);

    }
}
