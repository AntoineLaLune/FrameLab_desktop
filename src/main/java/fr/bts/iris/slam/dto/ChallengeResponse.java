package fr.bts.iris.slam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.bts.iris.slam.model.Challenge;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ChallengeResponse {

    private String success;
    private String message;
    private Challenge challenge;

    public ChallengeResponse() {}

    public String getSuccess() { return this.success; }
    public String getMessage() { return  this.message; }
    public Challenge getChallenge() { return this.challenge; }

    public void setMessage(String message) { this.message = message; }
    public void setSuccess(String success) { this.success = success; }
    public void setChallenge(Challenge challenge) { this.challenge = challenge; }
    
}
