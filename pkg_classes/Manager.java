package pkg_classes;

public class Manager extends User implements IManager{
    private String username;

    public Manager(String id, String name, String email, String username, String password) {
        super(id, name, email, password);
        this.username = username;
    }

    // Getter and Setter for username
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }








}
