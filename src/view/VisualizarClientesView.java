package view;

import dao.ClienteDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Cliente;

import java.util.List;

public class VisualizarClientesView {

    private TableView<Cliente> tabela;
    private ObservableList<Cliente> dados;
    

    public Scene getScene(Stage stage) {
        tabela = new TableView<>();
        dados = FXCollections.observableArrayList();
        tabela.setEditable(true); // ✅ permite edição na tabela

        // Coluna Nome (apenas visualização)
     // Coluna Nome (editável)
        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setCellFactory(TextFieldTableCell.forTableColumn());
        colNome.setOnEditCommit(event -> {
            Cliente cliente = event.getRowValue();
            cliente.setNome(event.getNewValue());
            new ClienteDAO().atualizar(cliente);
        });


        // Coluna Telefone (editável)
        TableColumn<Cliente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colTelefone.setCellFactory(TextFieldTableCell.forTableColumn());
        colTelefone.setOnEditCommit(event -> {
            Cliente cliente = event.getRowValue();
            cliente.setTelefone(event.getNewValue());
            new ClienteDAO().atualizar(cliente); // Atualiza no banco
        });

        // Coluna Email (editável, aceita null)
        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(data -> {
            String email = data.getValue().getEmail();
            return new SimpleStringProperty(email != null ? email : "");
        });
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setOnEditCommit(event -> {
            Cliente cliente = event.getRowValue();
            String novoEmail = event.getNewValue().isBlank() ? null : event.getNewValue();
            cliente.setEmail(novoEmail);
            new ClienteDAO().atualizar(cliente);
        });

        // Coluna Endereço (editável)
        TableColumn<Cliente, String> colEndereco = new TableColumn<>("Endereço");
        colEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        colEndereco.setCellFactory(TextFieldTableCell.forTableColumn());
        colEndereco.setOnEditCommit(event -> {
            Cliente cliente = event.getRowValue();
            cliente.setEndereco(event.getNewValue());
            new ClienteDAO().atualizar(cliente);
        });

        tabela.getColumns().addAll(colNome, colTelefone, colEmail, colEndereco);
        tabela.setItems(dados);

        Button btnVoltar = new Button("⬅ Voltar ao Menu");
        btnVoltar.setOnAction(e -> {
            MenuView menu = new MenuView();
            stage.setScene(menu.getScene(stage));
        });

        VBox layout = new VBox(10, btnVoltar, tabela);
        layout.setStyle("-fx-padding: 20;");

        carregarClientes();

        return new Scene(layout, 700, 500);
    }

    private void carregarClientes() {
        ClienteDAO dao = new ClienteDAO();
        List<Cliente> clientes = dao.listarTodos();
        dados.setAll(clientes);
    }
}
