package view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import dao.UsuarioDAO;
import model.Usuario;

public class LoginView {
    public Scene getScene(Stage stage) {
        //Criação dos componentes da interface
    	Label title = new Label("Login");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Usuário");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Senha");

        //Botão de login e label de mensagem
        Button loginButton = new Button("Entrar");
        Label messageLabel = new Label();

        //Ação do botão - Pega os textos digitados.
        //Chama o UsuarioDAO e o método autenticar(login, senha), que consulta no banco.
        //Se o user for diferente de null, o login foi aceito.
        loginButton.setOnAction(e -> {
            String usuario = usernameField.getText();
            String senha = passwordField.getText();

            UsuarioDAO dao = new UsuarioDAO();
            Usuario user = dao.autenticar(usuario, senha);

            if (user != null) {
             // Aqui podemos carregar a próxima tela
                MenuView menuView = new MenuView();
                stage.setScene(menuView.getScene(stage));

                
            } else {
                messageLabel.setText("❌ Usuário ou senha inválidos!");
            }
        });

        VBox vbox = new VBox(10, title, usernameField, passwordField, loginButton, messageLabel);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        return new Scene(vbox, 300, 200);
    }
}
