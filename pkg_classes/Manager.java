package pkg_classes;

public class Manager extends User {
    private String username;

    public Manager(String id, String name, String email, String username, String password) {
        super(id, name, email, password);
        this.username = username;
    }

    // Getter and Setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
