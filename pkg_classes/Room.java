package pkg_classes;

public class Room {
    private int roomId;
    private String roomNumber;
    private String gender;      // "Male", "Female", "Unisex"
    private String roomType;    // "Single", "Double"
    private boolean isOccupied;
    private int capacity;
    private int currentOccupants;

    public Room(int roomId, String roomNumber, String gender, String roomType,
                boolean isOccupied, int capacity, int currentOccupants) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.gender = gender;
        this.roomType = roomType;
        this.isOccupied = isOccupied;
        this.capacity = capacity;
        this.currentOccupants = currentOccupants;
    }

    // Getters
    public int getRoomId() {
        return roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getGender() {
        return gender;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentOccupants() {
        return currentOccupants;
    }

    // Setters
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setCurrentOccupants(int currentOccupants) {
        this.currentOccupants = currentOccupants;
    }
    public boolean addOccupant() {
        if (currentOccupants < capacity) {
            currentOccupants++;
            if (currentOccupants == capacity) {
                isOccupied = true;
            }
            return true;
        }
        return false;
    }

    public boolean removeOccupant() {
        if (currentOccupants > 0) {
            currentOccupants--;
            if (currentOccupants < capacity) {
                isOccupied = false;
            }
            return true;
        }
        return false;
    }





}
