package fr.bts.iris.slam.model;

public class Layer {

    int id;
    String name;
    int project_id;

    public Layer(String name, int project_id) {
        this.id = -1;
        this.name = name;
        this.project_id = project_id;
    }

    public Layer(int id, String name, int project_id) {
        this.id = id;
        this.name = name;
        this.project_id = project_id;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getProjectId() { return project_id; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setProjectId(int project_id) { this.project_id = project_id; }

}
