package pkg_ManagerGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pkg_db.DatabaseConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

public class DeleteRoomController {

    @FXML private TableView<RoomRow> tableRooms;
    @FXML private TableColumn<RoomRow, String> colRoomNumber;
    @FXML private TableColumn<RoomRow, String> colRoomType;
    @FXML private TableColumn<RoomRow, String> colGender;
    @FXML private TableColumn<RoomRow, Integer> colAvailableSlots;

    @FXML private TextField roomNumberField;
    @FXML private Label lblMessage;

    private ObservableList<RoomRow> roomList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colAvailableSlots.setCellValueFactory(new PropertyValueFactory<>("availableSlots"));

        loadRoomsFromDatabase();
    }

    private void loadRoomsFromDatabase() {
        roomList.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT room_id, room_number, room_type, gender, capacity, current_occupants " +
                    "FROM rooms";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int available = rs.getInt("capacity") - rs.getInt("current_occupants");
                roomList.add(new RoomRow(
                        rs.getInt("room_id"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                        rs.getString("gender"),
                        available
                ));
            }

            tableRooms.setItems(roomList);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load rooms:\n" + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteRoom(ActionEvent event) {
        String roomNumber = roomNumberField.getText().trim();

        if (roomNumber.isEmpty()) {
            lblMessage.setText("Please enter a room number.");
            lblMessage.setStyle("-fx-text-fill: red;");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String deleteQuery = "DELETE FROM rooms WHERE room_number = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteQuery);
            stmt.setString(1, roomNumber);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                lblMessage.setText("Room deleted successfully.");
                lblMessage.setStyle("-fx-text-fill: green;");
                roomNumberField.clear();
                loadRoomsFromDatabase();
            } else {
                lblMessage.setText("Room not found.");
                lblMessage.setStyle("-fx-text-fill: red;");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Deletion failed:\n" + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pkg_ManagerGUI/ManagerProfile.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            URL cssURL = getClass().getResource("/pkg_Styles/style.css");
            if (cssURL != null) {
                scene.getStylesheets().add(cssURL.toExternalForm());
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
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
