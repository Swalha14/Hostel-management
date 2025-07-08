package pkg_classes;

public class Student extends User implements IStudent {
    public String gender;
    public String roomPreference;
    public String username;

    public Student(String id, String name, String email, String username, String password, String gender, String roomPreference) {
        super(id, name, email, password);
        this.username = username;
        this.gender = gender;
        this.roomPreference = roomPreference;
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public String getGender() {
        return gender; }

    @Override
    public String getRoomPreference() {
        return roomPreference; }

    @Override
    public String getUsername() {
        return username; }

    @Override
    public void setGender(String gender) {
        this.gender = gender; }

    @Override
    public void setRoomPreference(String roomPreference) {
        this.roomPreference = roomPreference; }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }
}
