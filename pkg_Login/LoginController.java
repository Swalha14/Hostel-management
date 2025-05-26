package pkg_Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import pkg_StudentGUI.StudentProfileController;
import pkg_db.DatabaseConnection;
import pkg_classes.Student;  // Import Student class

import java.io.IOException;
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
            // First, try student login (your existing logic)
            String studentQuery = "SELECT * FROM students WHERE username = ? AND password = ?";
            PreparedStatement studentStmt = conn.prepareStatement(studentQuery);
            studentStmt.setString(1, username);
            studentStmt.setString(2, password);

            ResultSet rsStudent = studentStmt.executeQuery();

            if (rsStudent.next()) {
                // Student login success - existing code unchanged
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
                    stage.setScene(new Scene(profileRoot));
                    stage.show();

                    // Close login window
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
                    // Manager login success
                    String id = rsManager.getString("manager_id");
                    String name = rsManager.getString("name");
                    String email = rsManager.getString("email");
                    String mgrUsername = rsManager.getString("username");

                    // Assuming your Manager constructor is Manager(id, name, email, username, password)
                    pkg_classes.Manager loggedInManager = new pkg_classes.Manager(id,name,email,username, password);

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pkg_ManagerGUI/ManagerProfile.fxml"));
                        Parent profileRoot = loader.load();

                        // Assuming you have a ManagerProfileController with setManager method
                        pkg_ManagerGUI.ManagerProfileController controller = loader.getController();
                        controller.setManager(loggedInManager);

                        Stage stage = new Stage();
                        stage.setTitle("Manager Profile");
                        stage.setScene(new Scene(profileRoot));
                        stage.show();

                        // Close login window
                        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Neither student nor manager login succeeded
                    System.out.println("Invalid credentials.");
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
            stage.setScene(new Scene(welcomeRoot));
            stage.show();

            // Close login window
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
