package view;

import dao.ProdutoDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.Produto;

public class VisualizarEstoqueView {

    private TableView<Produto> tabela;
    private ObservableList<Produto> dados;
    private TextField filtroNome;

    public Scene getScene(Stage stage) {
        tabela = new TableView<>();
        dados = FXCollections.observableArrayList();
        filtroNome = new TextField();
        filtroNome.setPromptText("Filtrar por nome");

        // Colunas
        TableColumn<Produto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Produto, String> colNome = new TableColumn<>("Produto");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));

        TableColumn<Produto, Integer> colQtd = new TableColumn<>("Quantidade");
        colQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colQtd.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colQtd.setOnEditCommit(event -> {
            Produto produto = event.getRowValue();
            Integer novaQuantidade = event.getNewValue();
            if (novaQuantidade != null && novaQuantidade >= 0) {
                produto.setQuantidade(novaQuantidade);
                atualizarProduto(produto); // <-- Salva no banco
            }
        });


        tabela.getColumns().addAll(colId, colNome, colQtd);
        tabela.setItems(dados);
        tabela.setEditable(true);

        // Botões
        Button btnAtualizar = new Button("🔄 Atualizar");
        Button btnVoltar = new Button("⬅ Voltar ao Menu");
        Button btnExcluir = new Button("🗑 Excluir");

        btnAtualizar.setOnAction(e -> carregarProdutos());
        btnVoltar.setOnAction(e -> {
            MenuView menu = new MenuView();
            stage.setScene(menu.getScene(stage));
        });
        
        btnExcluir.setOnAction(e -> {
            Produto selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
            	ProdutoDAO dao = new ProdutoDAO();
            	dao.excluir(selecionado.getId());
            	dados.remove(selecionado);
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Atenção");
                alert.setHeaderText("Nenhum produto selecionado");
                alert.setContentText("Selecione um produto para excluir.");
                alert.showAndWait();
            }
        });

        filtroNome.textProperty().addListener((obs, oldVal, newVal) -> carregarProdutos());

        HBox topo = new HBox(10, filtroNome, btnAtualizar, btnExcluir, btnVoltar);
        VBox layout = new VBox(10, topo, tabela);
        layout.setStyle("-fx-padding: 20;");

        carregarProdutos();

        return new Scene(layout, 750, 400);
    }

    private void carregarProdutos() {
        dados.setAll(new ProdutoDAO().listar().stream()
                .filter(p -> p.getNome().toLowerCase().contains(filtroNome.getText().toLowerCase()))
                .toList());
    }

    private void atualizarProduto(Produto produto) {
        ProdutoDAO dao = new ProdutoDAO();
        dao.atualizar(produto);
    }

}
