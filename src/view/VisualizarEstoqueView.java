package view;

import dao.EstoqueDAO;
import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import model.Estoque;

public class VisualizarEstoqueView {

    private TableView<Estoque> tabela;
    private ObservableList<Estoque> dados;
    private TextField filtroNome;

    public Scene getScene(Stage stage) {
        tabela = new TableView<>();
        dados = FXCollections.observableArrayList();
        filtroNome = new TextField();
        filtroNome.setPromptText("Filtrar por nome");

        TableColumn<Estoque, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Estoque, String> colProduto = new TableColumn<>("Produto");
        colProduto.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getProduto().getNome()));

        TableColumn<Estoque, Integer> colQtd = new TableColumn<>("Quantidade");
        colQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        tabela.getColumns().addAll(colId, colProduto, colQtd);
        tabela.setItems(dados);

        // Botões
        Button btnAtualizar = new Button("🔄 Atualizar");
        Button btnVoltar = new Button("⬅ Voltar ao Menu");

        btnAtualizar.setOnAction(e -> carregarEstoque());
        btnVoltar.setOnAction(e -> {
            MenuView menu = new MenuView();
            stage.setScene(menu.getScene(stage));
        });

        filtroNome.textProperty().addListener((obs, oldVal, newVal) -> carregarEstoque());

        HBox topo = new HBox(10, filtroNome, btnAtualizar, btnVoltar);
        VBox layout = new VBox(10, topo, tabela);
        layout.setStyle("-fx-padding: 20;");

        carregarEstoque();

        return new Scene(layout, 700, 400);
    }

    private void carregarEstoque() {
        EstoqueDAO dao = new EstoqueDAO();
        String filtro = filtroNome.getText().toLowerCase();

        dados.setAll(dao.listarEstoque().stream()
            .filter(e -> e.getProduto().getNome().toLowerCase().contains(filtro))
            .toList());
    }
}
