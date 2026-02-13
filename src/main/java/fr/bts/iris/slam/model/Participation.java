package fr.bts.iris.slam.model;

public class Participation {

    int id;
    String photo_url;
    String created;
    Boolean is_hidden;
    int challenge_id;
    int user_id;

    public Participation(int id, String photo_url, Boolean is_hidden, String created, int challenge_id, int user_id) {
        this.id = id;
        this.photo_url = photo_url;
        this.is_hidden = is_hidden;
        this.created = created;
        this.challenge_id = challenge_id;
        this.user_id = user_id;
    }

    public int getId() {return id;}
    public String getPhoto_url() {return photo_url;}
    public String getCreated() {return created;}
    public Boolean getIs_hidden() {return is_hidden;}
    public int getChallenge_id() {return challenge_id;}
    public int getUser_id() {return user_id;}

    public void setId(int id) { this.id = id; }
    public void setPhoto_url(String photo_url) { this.photo_url = photo_url; }
    public void setCreated(String created) { this.created = created; }
    public void setIs_hidden(Boolean is_hidden) { this.is_hidden = is_hidden; }
    public void setChallenge_id(int challenge_id) { this.challenge_id = challenge_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }

}
