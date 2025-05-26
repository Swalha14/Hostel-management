package pkg_classes;

public class Student extends User {
    public String gender;
    public String roomPreference;
    public String username;

    public Student(String id, String name, String email, String username, String password, String gender, String roomPreference) {
        super(id, name, email, password);
        this.username = username;
        this.gender = gender;
        this.roomPreference = roomPreference;
    }

    public String getGender() {
        return gender; }
    public String getRoomPreference() {
        return roomPreference; }
    public String getUsername() {
        return username; }

    public void setGender(String gender) {
        this.gender = gender; }
    public void setRoomPreference(String roomPreference) {
        this.roomPreference = roomPreference; }
    public void setUsername(String username) {
        this.username = username; }
}
