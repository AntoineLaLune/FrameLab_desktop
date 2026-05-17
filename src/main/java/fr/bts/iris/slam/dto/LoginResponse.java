package fr.bts.iris.slam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.bts.iris.slam.model.User;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class LoginResponse {

    private String success;
    private String message;
    private User data;

    public LoginResponse() {}

    public String getSuccess() { return this.success; }
    public String getMessage() { return this.message; }
    public User getData() { return this.data; }

    public void setMessage(String message) { this.message = message; }
    public void setSuccess(String success) { this.success = success; }
    public void setData(User data) { this.data = data; }

}
