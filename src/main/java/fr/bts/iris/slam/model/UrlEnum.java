package fr.bts.iris.slam.model;

public enum UrlEnum {

    LOGIN("http://localhost:3333/api/auth/login"),
    REGISTER("http://localhost:3333/api/auth/register"), // IDEE
    SESSION("http://localhost:3333/api/auth/session"),
    CHALLENGE("http://localhost:3333/api/challenge/current"),
    UPLOAD("http://localhost:3333/uploads");

    private final String url;

    UrlEnum(String url) { this.url = url; }

    @Override
    public String toString() { return this.url; }

}
