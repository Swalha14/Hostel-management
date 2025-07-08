package pkg_Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import pkg_StudentGUI.StudentProfileController;
import pkg_db.DatabaseConnection;
import pkg_classes.Student;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private void ActionLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String studentQuery = "SELECT * FROM students WHERE username = ? AND password = ?";
            PreparedStatement studentStmt = conn.prepareStatement(studentQuery);
            studentStmt.setString(1, username);
            studentStmt.setString(2, password);

            ResultSet rsStudent = studentStmt.executeQuery();

            if (rsStudent.next()) {
                String id = rsStudent.getString("id");
                String name = rsStudent.getString("name");
                String email = rsStudent.getString("email");
                String gender = rsStudent.getString("gender");
                String roomPreference = rsStudent.getString("room_preference");

                Student loggedInStudent = new Student(id, name, email, username, password, gender, roomPreference);
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pkg_StudentGUI/StudentProfile.fxml"));
                    Parent profileRoot = loader.load();

                    StudentProfileController controller = loader.getController();
                    controller.setStudent(loggedInStudent);

                    Stage stage = new Stage();
                    stage.setTitle("Student Profile");

                    Scene scene = new Scene(profileRoot);
                    URL cssURL = getClass().getResource("/pkg_Styles/style.css");
                    if (cssURL != null) {
                        scene.getStylesheets().add(cssURL.toExternalForm());
                    }

                    stage.setScene(scene);
                    stage.show();

                    ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Student login failed, try manager login
                String managerQuery = "SELECT * FROM managers WHERE username = ? AND password = ?";
                PreparedStatement managerStmt = conn.prepareStatement(managerQuery);
                managerStmt.setString(1, username);
                managerStmt.setString(2, password);

                ResultSet rsManager = managerStmt.executeQuery();

                if (rsManager.next()) {
                    String id = rsManager.getString("manager_id");
                    String name = rsManager.getString("name");
                    String email = rsManager.getString("email");

                    pkg_classes.Manager loggedInManager = new pkg_classes.Manager(id, name, email, username, password);

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pkg_ManagerGUI/ManagerProfile.fxml"));
                        Parent profileRoot = loader.load();

                        pkg_ManagerGUI.ManagerProfileController controller = loader.getController();
                        controller.setManager(loggedInManager);

                        Stage stage = new Stage();
                        stage.setTitle("Manager Profile");

                        Scene scene = new Scene(profileRoot);
                        URL cssURL = getClass().getResource("/pkg_Styles/style.css");
                        if (cssURL != null) {
                            scene.getStylesheets().add(cssURL.toExternalForm());
                        }

                        stage.setScene(scene);
                        stage.show();

                        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Show alert for invalid credentials
                    showAlert("Login Failed", "Invalid username or password. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionCancel(ActionEvent event) {
        try {
            Parent welcomeRoot = FXMLLoader.load(getClass().getResource("/pkg_Welcome/Welcome.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Welcome");

            Scene scene = new Scene(welcomeRoot);
            URL cssURL = getClass().getResource("/pkg_Styles/style.css");
            if (cssURL != null) {
                scene.getStylesheets().add(cssURL.toExternalForm());
            }

            stage.setScene(scene);
            stage.show();

            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔔 Reusable method to show alert popups
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
