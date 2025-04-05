package view;

import dao.EstoqueDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
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

        // Definindo as colunas da tabela
        TableColumn<Estoque, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Atualizando a exibição para mostrar o nome diretamente, sem necessidade do Produto
        TableColumn<Estoque, String> colProduto = new TableColumn<>("Produto");
        colProduto.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));

        TableColumn<Estoque, Integer> colQtd = new TableColumn<>("Quantidade");
        colQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        
        // Tornando a coluna de quantidade editável
        colQtd.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colQtd.setOnEditCommit(event -> {
            Estoque estoque = event.getRowValue();
            Integer novaQuantidade = event.getNewValue();
            if (novaQuantidade != null) {
                estoque.setQuantidade(novaQuantidade);
                atualizarEstoque(estoque);  // Atualiza no banco de dados
            }
        });

        tabela.getColumns().addAll(colId, colProduto, colQtd);
        tabela.setItems(dados);
        tabela.setEditable(true);  // Permite edição na tabela

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
            .filter(e -> e.getNome().toLowerCase().contains(filtro))  // Usando nome diretamente
            .toList());
    }

    private void atualizarEstoque(Estoque estoque) {
        EstoqueDAO dao = new EstoqueDAO();
        dao.atualizarQuantidade(estoque.getId(), estoque.getQuantidade());  // Atualiza no banco de dados
    }
}
