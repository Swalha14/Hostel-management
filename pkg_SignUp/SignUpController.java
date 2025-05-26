package pkg_SignUp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import pkg_db.DatabaseConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import pkg_classes.Student;


public class SignUpController implements Initializable {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtStudentID;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private ComboBox<String> comboGender;

    @FXML
    private ComboBox<String> comboRoomType;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnCancel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate gender options
        comboGender.getItems().addAll("Male", "Female");

        // Populate room type options
        comboRoomType.getItems().addAll("Single", "Double", "Triple");
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String username = txtUsername.getText();
        String studentId = txtStudentID.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        String gender = comboGender.getValue();
        String roomType = comboRoomType.getValue();

        if (name.isEmpty()||email.isEmpty() || username.isEmpty() || studentId.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty() || gender == null || roomType == null) {
            showAlert(AlertType.ERROR, "Form Error!", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return;
        }

        Student student = new Student(studentId, name, email, username, password, gender, roomType);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO students (id, name, email, username, password, gender, room_preference) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, student.getId());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getUsername());
            stmt.setString(5, student.getPassword());
            stmt.setString(6, student.getGender());
            stmt.setString(7, student.getRoomPreference());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Sign up successful!");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }


@FXML
    private void handleCancel(ActionEvent event) {
        try {
            Parent welcomeRoot = FXMLLoader.load(getClass().getResource("/pkg_Welcome/Welcome.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Welcome");
            stage.setScene(new Scene(welcomeRoot));
            stage.show();

            // Close signup window
            ((Stage)((Node) event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

