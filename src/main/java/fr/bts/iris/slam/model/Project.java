package fr.bts.iris.slam.model;

import java.util.ArrayList;

public class Project {

    int id;
    String name;
    ArrayList<Layer> layers;
    int user_id;
    int challenge_id;
    String challenge_name;

    public Project(String name, int user_id, int challenge_id, String challenge_name) {
        this.id = -1;
        this.name = name;
        this.layers = new ArrayList<>();
        this.user_id = user_id;
        this.challenge_id = challenge_id;
        this.challenge_name = challenge_name;
    }

    public Project(int id, String name, int user_id, int challenge_id, String challenge_name) {
        this.id = id;
        this.name = name;
        this.layers = new ArrayList<>();
        this.user_id = user_id;
        this.challenge_id = challenge_id;
        this.challenge_name = challenge_name;
    }

    public Project(int id, String name, ArrayList<Layer> layers, int user_id, int challenge_id, String challenge_name) {
        this.id = id;
        this.name = name;
        this.layers = layers;
        this.user_id = user_id;
        this.challenge_id = challenge_id;
        this.challenge_name = challenge_name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public ArrayList<Layer> getLayers() { return layers; }
    public int getUser_id() { return user_id; }
    public int getChallenge_id() { return challenge_id; }
    public String getChallenge_name() { return challenge_name; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLayers(ArrayList<Layer> layers) { this.layers = layers; }
    public void setUser_id(int user_id) { this.user_id = user_id; }
    public void setChallenge_id(int challenge_id) { this.challenge_id = challenge_id; }
    public void setChallenge_name(String challenge_name) { this.challenge_name = challenge_name; }

    public void addLayer(Layer layer) { this.layers.add(layer); }

}
