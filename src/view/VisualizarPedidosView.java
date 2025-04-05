package view;

import dao.PedidoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import model.Pedido;
import model.StatusPedido;

import java.time.LocalDate;
import java.util.List;

public class VisualizarPedidosView {

    private TableView<Pedido> tabela;
    private ObservableList<Pedido> dados;
    private ComboBox<StatusPedido> filtroStatus;

    public Scene getScene(Stage stage) {
        tabela = new TableView<>();
        dados = FXCollections.observableArrayList();
        filtroStatus = new ComboBox<>();
        filtroStatus.getItems().addAll(StatusPedido.values());
        filtroStatus.setPromptText("Filtrar por status");

        // Colunas
        TableColumn<Pedido, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        
     // Coluna para o nome do cliente
        TableColumn<Pedido, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getCliente().getNome()));

        // Coluna para o número do telefone do cliente
        TableColumn<Pedido, String> colNumero = new TableColumn<>("Número");
        colNumero.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getCliente().getTelefone()));


        TableColumn<Pedido, String> colProduto = new TableColumn<>("Produto");
        colProduto.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getProduto().getNome()));

        TableColumn<Pedido, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStatus().name()));

        TableColumn<Pedido, String> colDataEntrega = new TableColumn<>("Data de Entrega");
        colDataEntrega.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDataEntrega()));

        TableColumn<Pedido, StatusPedido> colAlterarStatus = new TableColumn<>("Alterar Status");
        colAlterarStatus.setCellFactory(tc -> new TableCell<>() {
            private final ComboBox<StatusPedido> comboBox = new ComboBox<>();

            {
                comboBox.getItems().addAll(StatusPedido.values());
                comboBox.setOnAction(e -> {
                    Pedido pedido = getTableView().getItems().get(getIndex());
                    StatusPedido novoStatus = comboBox.getValue();
                    atualizarStatusPedido(pedido, novoStatus);
                });
            }

            @Override
            protected void updateItem(StatusPedido item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Pedido pedido = getTableView().getItems().get(getIndex());
                    comboBox.setValue(pedido.getStatus());
                    setGraphic(comboBox);
                }
            }
        });

        tabela.getColumns().addAll(colId, colCliente, colNumero, colProduto, colStatus, colDataEntrega, colAlterarStatus);

        tabela.setItems(dados);
        
        

        // Botões
        Button btnAtualizar = new Button("🔄 Atualizar");
        Button btnVoltar = new Button("⬅ Voltar ao Menu");

        btnAtualizar.setOnAction(e -> carregarPedidos());
        btnVoltar.setOnAction(e -> {
            MenuView menu = new MenuView();
            stage.setScene(menu.getScene(stage));
        });

        filtroStatus.setOnAction(e -> carregarPedidos());

        HBox botoes = new HBox(10, filtroStatus, btnAtualizar, btnVoltar);
        VBox layout = new VBox(10, botoes, tabela);
        layout.setStyle("-fx-padding: 20;");

        carregarPedidos();

        return new Scene(layout, 950, 500);
    }

    private void carregarPedidos() {
        PedidoDAO dao = new PedidoDAO();
        List<Pedido> pedidos;
        if (filtroStatus.getValue() != null) {
            pedidos = dao.listarPorStatus(filtroStatus.getValue());
        } else {
            pedidos = dao.listarTodos();
        }
        dados.setAll(pedidos);
    }

    private void atualizarStatusPedido(Pedido pedido, StatusPedido novoStatus) {
        if (novoStatus != null && novoStatus != pedido.getStatus()) {
            PedidoDAO dao = new PedidoDAO();
            pedido.setStatus(novoStatus);

            dao.atualizarStatus(pedido.getId(), novoStatus);

            if (novoStatus == StatusPedido.ENTREGUE) {
                String dataEntrega = LocalDate.now().toString();
                pedido.setDataEntrega(dataEntrega);
                dao.atualizarDataEntrega(pedido.getId(), dataEntrega);
            }

            carregarPedidos();
        }
    }
}
