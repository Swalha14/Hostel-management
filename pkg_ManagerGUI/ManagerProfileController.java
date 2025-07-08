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
import java.net.URL;

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

            Scene scene = new Scene(loginRoot);
            URL cssURL = getClass().getResource("/pkg_Styles/style.css");
            if (cssURL != null) {
                scene.getStylesheets().add(cssURL.toExternalForm());
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
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

    // ✅ Single corrected loadScene method
    private void loadScene(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            // Load stylesheet
            URL cssURL = getClass().getResource("/pkg_Styles/style.css");
            if (cssURL != null) {
                scene.getStylesheets().add(cssURL.toExternalForm());
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
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
