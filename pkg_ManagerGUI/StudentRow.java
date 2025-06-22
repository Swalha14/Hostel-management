package pkg_ManagerGUI;

import javafx.beans.property.SimpleStringProperty;

public class StudentRow {
    private final SimpleStringProperty id, name, gender, roomPref;

    public StudentRow(String id, String name, String gender, String roomPref) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.gender = new SimpleStringProperty(gender);
        this.roomPref = new SimpleStringProperty(roomPref);
    }

    public String getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getGender() { return gender.get(); }
    public String getRoomPref() { return roomPref.get(); }

    public SimpleStringProperty idProperty() { return id; }
    public SimpleStringProperty nameProperty() { return name; }
    public SimpleStringProperty genderProperty() { return gender; }
    public SimpleStringProperty roomPrefProperty() { return roomPref; }
}
