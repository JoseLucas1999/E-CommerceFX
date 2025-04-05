package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuView {

    public Scene getScene(Stage stage) {
        Label title = new Label("📋 Menu Principal");

        Button produtoBtn = new Button("Cadastrar Produto");
        Button clienteBtn = new Button("Cadastrar Cliente");
        Button pedidoBtn = new Button("Fazer Pedido");
        Button visualizarPedidosBtn = new Button("Visualizar Pedidos");
        Button estoqueBtn = new Button("Visualizar Estoque");
        Button sairBtn = new Button("Sair");

        produtoBtn.setOnAction(e -> {
            ProdutoView produtoView = new ProdutoView();
            stage.setScene(produtoView.getScene(stage));
        });

        clienteBtn.setOnAction(e -> {
            ClienteView clienteView = new ClienteView();
            stage.setScene(clienteView.getScene(stage));
        });

        pedidoBtn.setOnAction(e -> {
            PedidoView pedidoView = new PedidoView();
            stage.setScene(pedidoView.getScene(stage));
        });

        visualizarPedidosBtn.setOnAction(e -> {
            VisualizarPedidosView pedidosView = new VisualizarPedidosView();
            stage.setScene(pedidosView.getScene(stage));
        });


        estoqueBtn.setOnAction(e -> {
            VisualizarEstoqueView visualizarEstoqueView = new VisualizarEstoqueView();
            stage.setScene(visualizarEstoqueView.getScene(stage));
        });

        sairBtn.setOnAction(e -> {
            LoginView loginView = new LoginView();
            stage.setScene(loginView.getScene(stage));
        });

        VBox vbox = new VBox(12, title, produtoBtn, clienteBtn, pedidoBtn, visualizarPedidosBtn, estoqueBtn, sairBtn);
        vbox.setStyle("-fx-padding: 30; -fx-alignment: center;");

        return new Scene(vbox, 400, 400);
    }
}
