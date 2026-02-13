package fr.bts.iris.slam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.bts.iris.slam.model.User;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class UserResponse {

    private String success;
    private String message;
    private User userData;

    public UserResponse() {}

    public String getSuccess() { return this.success; }
    public String getMessage() { return this.message; }
    public User getUserData() { return this.userData; }

    public void setMessage(String message) { this.message = message; }
    public void setSuccess(String success) { this.success = success; }
    public void setUserData(User userData) { this.userData = userData; }

}
