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
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddRoomController {

    @FXML private TextField roomNumberField;
    @FXML private TextField capacityField;
    @FXML private ComboBox<String> genderCombo;
    @FXML private ComboBox<String> roomTypeCombo;

    @FXML
    private void handleAddRoom(ActionEvent event) {
        String roomNumber = roomNumberField.getText().trim();
        String capacityText = capacityField.getText().trim();
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
            showAlert(Alert.AlertType.ERROR, "Capacity must be a valid number.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if room already exists
            String checkQuery = "SELECT COUNT(*) FROM rooms WHERE room_number = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, roomNumber);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                showAlert(Alert.AlertType.WARNING, "Room already exists.");
                return;
            }

            // Insert room into database
            String insertRoom = "INSERT INTO rooms (room_number, room_type, gender, capacity, current_occupants) VALUES (?, ?, ?, ?, 0)";
            PreparedStatement insertStmt = conn.prepareStatement(insertRoom);
            insertStmt.setString(1, roomNumber);
            insertStmt.setString(2, roomType);
            insertStmt.setString(3, gender);
            insertStmt.setInt(4, capacity);

            int rowsInserted = insertStmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Room added successfully.");
                clearForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to add room.");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerProfile.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Add style.css
            scene.getStylesheets().add(getClass().getResource("/pkg_Styles/style.css").toExternalForm());

            Stage stage = (Stage) roomNumberField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Manager Dashboard");
            stage.show();
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
