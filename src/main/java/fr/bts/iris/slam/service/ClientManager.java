package fr.bts.iris.slam.service;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.http.HttpClient;

import com.github.mizosoft.methanol.Methanol;
import fr.bts.iris.slam.model.User;

public class ClientManager {

    private static User currentUser;

    private static Methanol httpClient;
    public static HttpClient getHttpClient() {
        if (httpClient == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            httpClient = Methanol.newBuilder()
                    .cookieHandler(cookieManager)
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
        }
        return httpClient;
    }

    public static User getCurrentUser() {
        return ClientManager.currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        ClientManager.currentUser = currentUser;
    }
}
