package fr.bts.iris.slam.controller;

import fr.bts.iris.slam.model.Project;

import java.io.IOException;

public abstract class Controller {

    protected void setInt(String name, int value) {}
    protected void setString(String name, String value) {}
    protected void setProject(String name, Project value) throws IOException {}

}