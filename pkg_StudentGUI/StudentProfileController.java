package pkg_StudentGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import pkg_classes.Service;
import pkg_classes.Student;
import pkg_db.DatabaseConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class StudentProfileController {
    private Student student;

    @FXML
    private AnchorPane ProfilePane;
    @FXML
    private AnchorPane RoomDetailsPane;
    @FXML
    private AnchorPane ServicesPane;
    @FXML
    private VBox servicesContainer;

    @FXML
    private void handleViewProfile() {
        ProfilePane.setVisible(true);
        RoomDetailsPane.setVisible(false);
        ServicesPane.setVisible(false);

        if (student != null) {
            populateProfile();
        }
    }

    public void setStudent(Student student) {
        this.student = student;
        populateProfile(); // Initially populate when loading
    }

    private void populateProfile() {
        VBox profileContainer = new VBox(10); // container for cards
        profileContainer.setLayoutX(50);
        profileContainer.setLayoutY(50);

        profileContainer.getChildren().addAll(
                createProfileCard("Name", student.getName(), "pastel-blue"),
                createProfileCard("Username", student.getUsername(), "pastel-green"),
                createProfileCard("Email", student.getEmail(), "pastel-yellow"),
                createProfileCard("Student ID", student.getId(), "pastel-pink")
        );

        ProfilePane.getChildren().clear();
        ProfilePane.getChildren().add(profileContainer);
    }

    private VBox createProfileCard(String label, String value, String pastelColorClass) {
        VBox card = new VBox(5);
        card.getStyleClass().addAll("profile-card", pastelColorClass);

        Label title = new Label(label + ":");
        title.getStyleClass().add("profile-title");

        Label content = new Label(value);
        content.getStyleClass().add("profile-description");
        content.setWrapText(true);

        card.getChildren().addAll(title, content);
        return card;
    }

    @FXML
    private void handleViewServices() {
        ServicesPane.setVisible(true);
        ProfilePane.setVisible(false);
        RoomDetailsPane.setVisible(false);
        loadServicesFromDatabase();
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
            e.printStackTrace();
        }
    }
}
