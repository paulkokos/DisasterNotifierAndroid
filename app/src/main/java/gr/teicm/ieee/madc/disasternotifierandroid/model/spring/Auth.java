package gr.teicm.ieee.madc.disasternotifierandroid.model.spring;

import java.io.Serializable;

public class Auth implements Serializable {

    private final String accessToken;

    public Auth(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}