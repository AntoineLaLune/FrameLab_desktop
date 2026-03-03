package fr.bts.iris.slam.controller;

import fr.bts.iris.slam.model.User;

public abstract class Controller {

    protected void setInt(String name, int value) {}
    protected void setString(String name, String value) {}
    protected void setUser(String name, User value) {} // ← Temporary send the user with a setter, for development only (Will be changed)

}