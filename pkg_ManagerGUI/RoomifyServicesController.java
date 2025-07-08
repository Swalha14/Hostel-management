
package pkg_ManagerGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import pkg_classes.Service;
import pkg_db.DatabaseConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomifyServicesController {

    @FXML
    private VBox servicesContainer;

    @FXML
    public void initialize() {
        loadServicesFromDatabase();
    }

    private void loadServicesFromDatabase() {
        ObservableList<Service> servicesList = FXCollections.observableArrayList();
        String query = "SELECT service_name, description FROM services";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            servicesContainer.getChildren().clear();

            while (rs.next()) {
                String name = rs.getString("service_name");
                String desc = rs.getString("description");
                Service service = new Service(name, desc);
                servicesList.add(service);

                VBox card = new VBox(5);
                card.getStyleClass().add("service-card");

                Label nameLabel = new Label(service.getServiceName());
                nameLabel.getStyleClass().add("service-title");

                Label descLabel = new Label(service.getDescription());
                descLabel.getStyleClass().add("service-description");
                descLabel.setWrapText(true);

                card.getChildren().addAll(nameLabel, descLabel);
                servicesContainer.getChildren().add(card);
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to load services:\n" + e.getMessage());
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pkg_ManagerGUI/ManagerProfile.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root); // Create ONE scene

            // Apply CSS
            URL cssURL = getClass().getResource("/pkg_Styles/style.css");
            if (cssURL != null) {
                scene.getStylesheets().add(cssURL.toExternalForm());
            }

            // Set scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Manager Dashboard");
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load Manager Dashboard: " + e.getMessage());
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
