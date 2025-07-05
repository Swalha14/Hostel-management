package pkg_ManagerGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class DeleteStudentController {

    @FXML
    private TextField txtStudentID;

    @FXML
    private Label lblMessage;

    @FXML
    private void handleDeleteStudent(ActionEvent event) {
        String studentId = txtStudentID.getText().trim();

        if (studentId.isEmpty()) {
            lblMessage.setText("Please enter a student ID.");
            return;
        }

        // TODO: Replace this with actual deletion logic (DB or List)
        boolean deleted = mockDeleteStudent(studentId);

        if (deleted) {
            lblMessage.setStyle("-fx-text-fill: green;");
            lblMessage.setText("Student deleted successfully.");
            txtStudentID.clear();
        } else {
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Student not found.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pkg_ManagerGUI/ManagerProfile.fxml"));
            Parent managerRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(managerRoot));
            stage.setTitle("Manager Dashboard");
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Unable to return to manager dashboard:\n" + e.getMessage());
        }
    }

    private boolean mockDeleteStudent(String id) {
        // Fake deletion logic for now
        return id.equalsIgnoreCase("12345"); // pretend only this ID exists
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
