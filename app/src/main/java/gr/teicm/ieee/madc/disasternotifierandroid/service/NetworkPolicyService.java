package gr.teicm.ieee.madc.disasternotifierandroid.service;

import android.os.StrictMode;

public class NetworkPolicyService {
    public static void setPolicy() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
