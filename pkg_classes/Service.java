package pkg_classes;

public class Service {
    private String serviceName;
    private String description;

    public Service(String serviceName, String description) {
        this.serviceName = serviceName;
        this.description = description;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getDescription() {
        return description;
    }
}
