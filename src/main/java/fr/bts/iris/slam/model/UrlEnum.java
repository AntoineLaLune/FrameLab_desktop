package fr.bts.iris.slam.model;

public enum UrlEnum {

    LOGIN("http://framelab.xn--trebois-owa.ip-ddns.com/api/auth/login"),
    REGISTER("http://framelab.xn--trebois-owa.ip-ddns.com/api/auth/register"), // IDEE
    SESSION("http://framelab.xn--trebois-owa.ip-ddns.com/api/auth/session"),
    CHALLENGE("http://framelab.xn--trebois-owa.ip-ddns.com/api/challenges/current"),
    UPLOAD("http://framelab.xn--trebois-owa.ip-ddns.com/uploads"),
    SUBMIT("http://framelab.xn--trebois-owa.ip-ddns.com/api/participations");

    private final String url;

    UrlEnum(String url) { this.url = url; }

    @Override
    public String toString() { return this.url; }

}
