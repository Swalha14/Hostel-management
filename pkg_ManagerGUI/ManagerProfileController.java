package pkg_ManagerGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import pkg_classes.Manager;

import java.io.IOException;

public class ManagerProfileController {

    private Manager manager;

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pkg_Login/Login.fxml"));
            Parent loginRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAllocateRoom(ActionEvent event) {
        loadScene("/pkg_ManagerGUI/RoomAllocation.fxml", "Allocate Rooms", event);
    }

    @FXML
    private void handleAddRoom(ActionEvent event) {
        loadScene("/pkg_ManagerGUI/AddRoom.fxml", "Add Room", event);
    }

    @FXML
    private void handleDeleteRoom(ActionEvent event) {
        loadScene("/pkg_ManagerGUI/DeleteRoom.fxml", "Delete Room", event);
    }

    @FXML
    private void handleDeleteStudent(ActionEvent event) {
        loadScene("/pkg_ManagerGUI/DeleteStudent.fxml", "Delete Student", event);
    }

    @FXML
    private void handleRoomifyServices(ActionEvent event) {
        loadScene("/pkg_ManagerGUI/RoomifyServices.fxml", "Roomify Services", event);
    }

    private void loadScene(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (loader.getLocation() == null) {
                throw new IOException("FXML file not found at: " + fxmlPath);
            }

            Parent root = loader.load();

            // Optional: pass manager to the new controller if needed
            // RoomifyServicesController controller = loader.getController();
            // controller.setManager(this.manager);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // ✅ For debugging
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load page: " + title + "\n" + e.getMessage());
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
