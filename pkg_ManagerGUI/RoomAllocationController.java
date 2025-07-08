package pkg_ManagerGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pkg_db.DatabaseConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class RoomAllocationController implements Initializable {

    @FXML private TableView<StudentRow> studentsTable;
    @FXML private TableColumn<StudentRow, String> colStudentId;
    @FXML private TableColumn<StudentRow, String> colStudentName;
    @FXML private TableColumn<StudentRow, String> colGender;
    @FXML private TableColumn<StudentRow, String> colRoomPreference;

    @FXML private TableView<RoomRow> roomsTable;
    @FXML private TableColumn<RoomRow, String> colRoomNumber;
    @FXML private TableColumn<RoomRow, String> colRoomType;
    @FXML private TableColumn<RoomRow, String> colRoomGender;
    @FXML private TableColumn<RoomRow, Integer> colAvailableSlots;

    private ObservableList<StudentRow> unassignedStudents = FXCollections.observableArrayList();
    private ObservableList<RoomRow> availableRooms = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Bind student columns
        colStudentId.setCellValueFactory(data -> data.getValue().idProperty());
        colStudentName.setCellValueFactory(data -> data.getValue().nameProperty());
        colGender.setCellValueFactory(data -> data.getValue().genderProperty());
        colRoomPreference.setCellValueFactory(data -> data.getValue().roomPrefProperty());

        // Bind room columns
        colRoomNumber.setCellValueFactory(data -> data.getValue().roomNumberProperty());
        colRoomType.setCellValueFactory(data -> data.getValue().roomTypeProperty());
        colRoomGender.setCellValueFactory(data -> data.getValue().genderProperty());
        colAvailableSlots.setCellValueFactory(data -> data.getValue().availableSlotsProperty().asObject());

        loadStudents();

        // Reload rooms when a student is selected
        studentsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                loadAvailableRooms(newSel.getGender(), newSel.getRoomPref());
            }
        });
    }

    private void loadStudents() {
        unassignedStudents.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id, name, gender, room_preference FROM students WHERE room_id IS NULL";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                unassignedStudents.add(new StudentRow(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getString("room_preference")
                ));
            }
            studentsTable.setItems(unassignedStudents);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error Loading Students", e.getMessage());
        }
    }

    private void loadAvailableRooms(String gender, String roomType) {
        availableRooms.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = """
                SELECT room_id, room_number, gender, room_type, capacity, current_occupants
                FROM rooms
                WHERE gender = ? AND room_type = ? AND current_occupants < capacity
            """;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, gender);
            stmt.setString(2, roomType);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int capacity = rs.getInt("capacity");
                int occupants = rs.getInt("current_occupants");
                availableRooms.add(new RoomRow(
                        rs.getInt("room_id"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                        rs.getString("gender"),
                        capacity - occupants
                ));
            }
            roomsTable.setItems(availableRooms);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error Loading Rooms", e.getMessage());
        }
    }

    @FXML
    private void handleAllocateRoom() {
        StudentRow selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
        RoomRow selectedRoom = roomsTable.getSelectionModel().getSelectedItem();

        if (selectedStudent == null || selectedRoom == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Missing", "Please select a student and a room.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Assign room to student
            PreparedStatement assignStmt = conn.prepareStatement("UPDATE students SET room_id = ? WHERE id = ?");
            assignStmt.setInt(1, selectedRoom.getRoomId());
            assignStmt.setString(2, selectedStudent.getId());
            assignStmt.executeUpdate();

            // 2. Increment current_occupants
            PreparedStatement updateRoomStmt = conn.prepareStatement("UPDATE rooms SET current_occupants = current_occupants + 1 WHERE room_id = ?");
            updateRoomStmt.setInt(1, selectedRoom.getRoomId());
            updateRoomStmt.executeUpdate();

            conn.commit();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Room allocated successfully!");

            // Refresh lists
            loadStudents();
            availableRooms.clear();
            roomsTable.setItems(availableRooms);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }
    @FXML
    private void handleCancel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerProfile.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            // Attach style.css
            URL cssURL = getClass().getResource("/pkg_Styles/style.css");
            if (cssURL != null) {
                scene.getStylesheets().add(cssURL.toExternalForm());
            }

            Stage stage = (Stage) studentsTable.getScene().getWindow();  // Get current window
            stage.setScene(scene);
            stage.setTitle("Manager Dashboard");
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to return to dashboard:\n" + e.getMessage());
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
