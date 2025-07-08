import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import pkg_classes.Fee;
import pkg_classes.Student;
import pkg_classes.Room;
import pkg_classes.Service;
import pkg_classes.User;
import pkg_classes.Manager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/pkg_Welcome/Welcome.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Welcome Page");
            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}

