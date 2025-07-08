package pkg_ManagerGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pkg_db.DatabaseConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

public class DeleteStudentController {

    @FXML private TableView<StudentRow> tableStudents;
    @FXML private TableColumn<StudentRow, String> colId;
    @FXML private TableColumn<StudentRow, String> colName;
    @FXML private TableColumn<StudentRow, String> colGender;
    @FXML private TableColumn<StudentRow, String> colPreference;

    @FXML private TextField txtStudentID;
    @FXML private Label lblMessage;

    private ObservableList<StudentRow> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colPreference.setCellValueFactory(new PropertyValueFactory<>("roomPref"));

        loadStudentsFromDatabase();
    }

    private void loadStudentsFromDatabase() {
        studentList.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id, name, gender, room_preference FROM students";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                studentList.add(new StudentRow(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getString("room_preference")
                ));
            }

            tableStudents.setItems(studentList);

        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load students:\n" + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteStudent(ActionEvent event) {
        String studentId = txtStudentID.getText().trim();

        if (studentId.isEmpty()) {
            lblMessage.setText("Please enter a student ID.");
            lblMessage.setStyle("-fx-text-fill: red;");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String deleteQuery = "DELETE FROM students WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteQuery);
            stmt.setString(1, studentId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                lblMessage.setText("Student deleted successfully.");
                lblMessage.setStyle("-fx-text-fill: green;");
                txtStudentID.clear();
                loadStudentsFromDatabase();
            } else {
                lblMessage.setText("Student not found.");
                lblMessage.setStyle("-fx-text-fill: red;");
            }

        } catch (SQLException e) {
            showAlert("Error", "Deletion failed:\n" + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pkg_ManagerGUI/ManagerProfile.fxml"));
            Parent managerRoot = loader.load();

            Scene scene = new Scene(managerRoot);

            // ✅ Apply style.css
            URL cssURL = getClass().getResource("/pkg_Styles/style.css");
            if (cssURL != null) {
                scene.getStylesheets().add(cssURL.toExternalForm());
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Manager Dashboard");
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Unable to return to manager dashboard:\n" + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
