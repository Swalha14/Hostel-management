package pkg_Welcome;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class WelcomeController {

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignUp;

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/pkg_Login/Login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(loginRoot));
            stage.show();

            // Close welcome window
            ((Stage) btnLogin.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            // Load the FXML
            Parent signupRoot = FXMLLoader.load(getClass().getResource("/pkg_SignUp/SignUp.fxml"));

            // Create a new Scene
            Scene scene = new Scene(signupRoot);

            // Load and apply CSS
            URL cssURL = getClass().getResource("/pkg_Styles/style.css");
            if (cssURL != null) {
                scene.getStylesheets().add(cssURL.toExternalForm());
            } else {
                System.out.println("⚠️ CSS file not found!");
            }

            // Create and show the new stage
            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(scene);
            stage.show();

            // Close welcome window
            ((Stage) btnSignUp.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
