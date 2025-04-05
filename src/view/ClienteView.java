package view;

import dao.ClienteDAO;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Cliente;

public class ClienteView {

    public Scene getScene(Stage stage) {
        Label title = new Label("Cadastro de Cliente");

        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome");

        TextField telefoneField = new TextField();
        telefoneField.setPromptText("Telefone");

        TextField enderecoField = new TextField();
        enderecoField.setPromptText("Endereço");

        TextField emailField = new TextField();
        emailField.setPromptText("Email (opcional)");

        Button salvarButton = new Button("Salvar");
        Button voltarButton = new Button("Voltar ao Menu");
        Label messageLabel = new Label();

        salvarButton.setOnAction(e -> {
            String nome = nomeField.getText().trim();
            String telefone = telefoneField.getText().trim();
            String endereco = enderecoField.getText().trim();
            String email = emailField.getText().trim();

            if (nome.isEmpty() || telefone.isEmpty() || endereco.isEmpty()) {
                messageLabel.setText("❌ Preencha os campos obrigatórios.");
                return;
            }

            Cliente cliente = new Cliente();
            cliente.setNome(nome);
            cliente.setTelefone(telefone);
            cliente.setEndereco(endereco);
            cliente.setEmail(email.isEmpty() ? null : email);

            ClienteDAO dao = new ClienteDAO();
            dao.salvar(cliente);

            messageLabel.setText("✅ Cliente cadastrado com sucesso!");

            nomeField.clear();
            telefoneField.clear();
            enderecoField.clear();
            emailField.clear();
        });

        // Ação do botão de voltar
        voltarButton.setOnAction(e -> {
            MenuView menuView = new MenuView();
            stage.setScene(menuView.getScene(stage));
        });

        VBox vbox = new VBox(10, title, nomeField, telefoneField, enderecoField, emailField, salvarButton, voltarButton, messageLabel);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        return new Scene(vbox, 350, 400);
    }
}
