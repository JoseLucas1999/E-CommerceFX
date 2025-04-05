package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import dao.ProdutoDAO;
import model.Produto;

public class ProdutoView {

    public Scene getScene(Stage stage) {
        Label title = new Label("Cadastro de Produto");

        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome do Produto");

        TextField descricaoField = new TextField();
        descricaoField.setPromptText("Descrição");

        TextField precoField = new TextField();
        precoField.setPromptText("Preço Base");

        Button salvarBtn = new Button("Salvar");
        Label msgLabel = new Label();

        salvarBtn.setOnAction(e -> {
            try {
                String nome = nomeField.getText();
                String descricao = descricaoField.getText();
                double preco = Double.parseDouble(precoField.getText());

                Produto produto = new Produto(0, nome, descricao, preco);
                ProdutoDAO dao = new ProdutoDAO();
                dao.inserir(produto);

                msgLabel.setText("✅ Produto salvo com sucesso!");

                // limpa os campos
                nomeField.clear();
                descricaoField.clear();
                precoField.clear();
            } catch (NumberFormatException ex) {
                msgLabel.setText("❌ Preço inválido.");
            } catch (Exception ex) {
                msgLabel.setText("❌ Erro ao salvar o produto.");
                ex.printStackTrace();
            }
        });

        Button voltarBtn = new Button("Voltar");
        voltarBtn.setOnAction(e -> {
            MenuView menuView = new MenuView();
            stage.setScene(menuView.getScene(stage));
        });

        VBox vbox = new VBox(10, title, nomeField, descricaoField, precoField, salvarBtn, voltarBtn, msgLabel);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        return new Scene(vbox, 350, 300);
    }
}
