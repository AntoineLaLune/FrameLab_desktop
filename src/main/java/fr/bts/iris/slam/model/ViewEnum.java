package fr.bts.iris.slam.model;

public enum ViewEnum {

    LOGIN("/view/login-view.fxml"),
    REGISTER("/view/register-view.fxml"),
    HOME("/view/home-view.fxml"),
    EDITOR("/view/editor-view.fxml"),
    SUBMIT("/view/submit-view.fxml");

    private final String view;

    ViewEnum(String view) { this.view = view; }

    @Override
    public String toString() { return this.view; }

}
