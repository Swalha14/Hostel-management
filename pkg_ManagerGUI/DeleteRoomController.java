package pkg_ManagerGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class DeleteRoomController {

    @FXML
    private TextField roomNumberField;

    @FXML
    private void handleDeleteRoom(ActionEvent event) {
        String roomNumber = roomNumberField.getText().trim();

        if (roomNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Room number cannot be empty.");
            return;
        }

        // Placeholder for actual deletion logic (e.g., DB call)
        System.out.println("Room deleted: " + roomNumber);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Room " + roomNumber + " deleted successfully!");
        roomNumberField.clear();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) roomNumberField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pkg_ManagerGUI/ManagerProfile.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) roomNumberField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager Dashboard");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load Manager Dashboard:\n" + e.getMessage());
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
