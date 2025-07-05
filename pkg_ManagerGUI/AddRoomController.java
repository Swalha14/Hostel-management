package pkg_ManagerGUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import pkg_db.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddRoomController {

    @FXML private TextField roomNumberField;
    @FXML private TextField capacityField;
    @FXML private ComboBox<String> genderCombo;
    @FXML private ComboBox<String> roomTypeCombo;

    @FXML
    private void handleAddRoom(ActionEvent event) {
        String roomNumber = roomNumberField.getText();
        String capacityText = capacityField.getText();
        String gender = genderCombo.getValue();
        String roomType = roomTypeCombo.getValue();

        if (roomNumber.isEmpty() || capacityText.isEmpty() || gender == null || roomType == null) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Capacity must be a number.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String insertRoom = "INSERT INTO rooms (room_number, room_type, gender, capacity, current_occupants) VALUES (?, ?, ?, ?, 0)";
            PreparedStatement stmt = conn.prepareStatement(insertRoom);
            stmt.setString(1, roomNumber);
            stmt.setString(2, roomType);
            stmt.setString(3, gender);
            stmt.setInt(4, capacity);
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Room added successfully!");
            clearForm();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerProfile.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) roomNumberField.getScene().getWindow(); // Get current window
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error: " + e.getMessage());
        }
    }

    private void clearForm() {
        roomNumberField.clear();
        capacityField.clear();
        genderCombo.getSelectionModel().clearSelection();
        roomTypeCombo.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Room Management");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
