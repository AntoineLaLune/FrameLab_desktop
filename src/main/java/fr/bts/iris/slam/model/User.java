package fr.bts.iris.slam.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class User {

    private int id;
    private String email;
    private String password;
    private String last_name;
    private String first_name;
    private boolean is_admin;

    public User() {}

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getLast_name() { return last_name; }
    public String getFirst_name() { return first_name; }
    public boolean isIs_admin() { return is_admin; }

    public void setId(int id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setLast_name(String last_name) { this.last_name = last_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }
    public void setIs_admin(boolean is_admin) { this.is_admin = is_admin; }

}
