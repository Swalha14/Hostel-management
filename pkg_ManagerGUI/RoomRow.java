package pkg_ManagerGUI;

import javafx.beans.property.*;

public class RoomRow {
    private final int roomId;
    private final SimpleStringProperty roomNumber, roomType, gender;
    private final SimpleIntegerProperty availableSlots;

    public RoomRow(int roomId, String roomNumber, String roomType, String gender, int availableSlots) {
        this.roomId = roomId;
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.roomType = new SimpleStringProperty(roomType);
        this.gender = new SimpleStringProperty(gender);
        this.availableSlots = new SimpleIntegerProperty(availableSlots);
    }

    public int getRoomId() { return roomId; }

    public String getRoomNumber() { return roomNumber.get(); }
    public String getRoomType() { return roomType.get(); }
    public String getGender() { return gender.get(); }
    public int getAvailableSlots() { return availableSlots.get(); }

    public SimpleStringProperty roomNumberProperty() { return roomNumber; }
    public SimpleStringProperty roomTypeProperty() { return roomType; }
    public SimpleStringProperty genderProperty() { return gender; }
    public SimpleIntegerProperty availableSlotsProperty() { return availableSlots; }
}
