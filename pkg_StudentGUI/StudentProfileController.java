package pkg_StudentGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import pkg_classes.Service;
import pkg_classes.Student;
import pkg_db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
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
    private Label txtName;// This must match fx:id in FXML
    @FXML
    private Label txtEmail;
    @FXML
    private Label txtStudentID;
    @FXML
    private Label txtUsername;
    @FXML
    private AnchorPane ProfilePane;
    @FXML
    private AnchorPane RoomDetailsPane;
    @FXML
    private AnchorPane ServicesPane;
    @FXML
    private TableView<Service> tableServices;

    @FXML
    private TableColumn<Service, String> colServiceName;

    @FXML
    private TableColumn<Service, String> colDescription;



    @FXML
    private void handleViewProfile() {
        // Show only the profile pane
        ProfilePane.setVisible(true);

        // Optionally hide others (if added later)
         RoomDetailsPane.setVisible(false);
        ServicesPane.setVisible(false);
    }


    public void setStudent(Student student) {
        this.student = student;
        populateProfile();
    }

    private void populateProfile() {
            if (student != null) {
                txtName.setText(student.getName());
                txtEmail.setText(student.getEmail());
                txtStudentID.setText(student.getId());
                txtUsername.setText(student.getUsername());
            }


    }
    @FXML
    private void handleViewServices() {
        ServicesPane.setVisible(true);
        ProfilePane.setVisible(false);
        RoomDetailsPane.setVisible(false);
        // Set up columns (you only need to do this once)
        colServiceName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadServicesFromDatabase();
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











    private void loadServicesFromDatabase() {
        ObservableList<Service> servicesList = FXCollections.observableArrayList();
        String query = "SELECT service_name, description FROM services";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("service_name");
                String desc = rs.getString("description");
                servicesList.add(new Service(name, desc));
            }

            tableServices.setItems(servicesList);

        } catch (SQLException e) {
            e.printStackTrace(); // You can show an Alert here too
        }
    }













}
