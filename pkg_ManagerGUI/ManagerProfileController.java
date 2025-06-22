package pkg_ManagerGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pkg_classes.Manager;

import java.io.IOException;

public class ManagerProfileController {
    // The Manager object passed from login
    private Manager manager;



    // This method will be called by LoginController to pass the logged-in manager
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
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleAllocateRoom(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pkg_ManagerGUI/RoomAllocation.fxml")
            );
            Parent allocRoot = loader.load();

            // (Optional) pass data to the allocation controller here:
            // RoomAllocationController ctrl = loader.getController();
            // ctrl.setManager(this.manager);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(allocRoot));
            stage.setTitle("Allocate Rooms");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






}
