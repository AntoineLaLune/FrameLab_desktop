package fr.bts.iris.slam.model;

public class Challenge {

    int id;
    String title;
    String description;
    String photo_url;
    String start_date;
    String end_date;
    int is_active;
    int creator_id;

    public Challenge() {}

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPhoto_url() { return photo_url; }
    public String getStart_date() { return start_date; }
    public String getEnd_date() { return end_date; }
    public int getIs_active() { return is_active; }
    public int getCreator_id() { return creator_id; }

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPhoto_url(String photo_url) { this.photo_url = photo_url; }
    public void setStart_date(String start_date) { this.start_date = start_date; }
    public void setEnd_date(String end_date) { this.end_date = end_date; }
    public void setIs_active(int is_active) { this.is_active = is_active; }
    public void setCreator_id(int creator_id) { this.creator_id = creator_id; }

}
