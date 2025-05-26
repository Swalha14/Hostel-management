package pkg_ManagerGUI;

import pkg_classes.Manager;

public class ManagerProfileController {
    // The Manager object passed from login
    private Manager manager;

    // This method will be called by LoginController to pass the logged-in manager
    public void setManager(Manager manager) {
        this.manager = manager;

    }

}
