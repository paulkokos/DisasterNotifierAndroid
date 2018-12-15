package gr.teicm.ieee.madc.disasternotifierandroid.service;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import gr.teicm.ieee.madc.disasternotifierandroid.globals.AppConfig;
import gr.teicm.ieee.madc.disasternotifierandroid.model.network.HTTPMethods;

public class NetworkService {
    private final HTTPMethods method;
    private final Map<String, String> headers;
    private final JSONObject body;
    private final String url;
    private HttpURLConnection protocol;
    private int statusCode;

    public NetworkService(HTTPMethods method, String url) {
        headers = new HashMap<>();
        body = new JSONObject();

        headers.put("User-Agent", AppConfig.UserAgent);

        this.method = method;
        this.url = url;

        this.statusCode = 0;
    }

    public void addHeader(Map.Entry<String, String> entry) {
        headers.put(entry.getKey(), entry.getValue());
    }

    public void addBody(String key, String value) throws JSONException {
        body.put(key, value);
    }

    public void addBody(String key, JSONObject value) throws JSONException {
        body.put(key, value);
    }

    public String call() {
        String response;

        switch (method) {
            case GET:
                response = doGetRequest();
                break;
            case POST:
                response = doPostRequest();
                break;
            case PUT:
                response = doPutRequest();
                break;
            case DELETE:
                response = doDeleteRequest();
                break;
            default:
                response = "";
                break;
        }

        protocol.disconnect();

        try {
            statusCode = protocol.getResponseCode();
        } catch (IOException e) {
            statusCode = 0;
        }

        return response;
    }

    private String doGetRequest() {

        try {
            protocol = (HttpURLConnection) new URL(url).openConnection();

            protocol.setRequestMethod(this.method.toString());

            protocol.setConnectTimeout(AppConfig.ConnectTimeout);
            protocol.setReadTimeout(AppConfig.ReadTimeout);

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                protocol.addRequestProperty(entry.getKey(), entry.getValue());
            }

            protocol.connect();
            return InputStreamToStringService.getString(protocol.getInputStream());
        } catch (Exception ex) {
            return InputStreamToStringService.getString(protocol.getErrorStream());
        }
    }

    private String doPostRequest() {

        try {
            protocol = (HttpURLConnection) new URL(url).openConnection();

            protocol.setRequestMethod(this.method.toString());

            protocol.setDoInput(true);
            protocol.setDoOutput(true);

            protocol.setConnectTimeout(AppConfig.ConnectTimeout);
            protocol.setReadTimeout(AppConfig.ReadTimeout);

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                protocol.addRequestProperty(entry.getKey(), entry.getValue());
            }

            OutputStream os = protocol.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8")
            );
            writer.write(body.toString());

            writer.flush();
            writer.close();
            os.close();

            protocol.connect();

            return InputStreamToStringService.getString(protocol.getInputStream());
        } catch (IOException e) {
            return InputStreamToStringService.getString(protocol.getErrorStream());
        }
    }

    private String doPutRequest() {

        try {
            protocol = (HttpURLConnection) new URL(url).openConnection();

            protocol.setRequestMethod(this.method.toString());

            protocol.setDoInput(true);
            protocol.setDoOutput(true);

            protocol.setConnectTimeout(AppConfig.ConnectTimeout);
            protocol.setReadTimeout(AppConfig.ReadTimeout);

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                protocol.addRequestProperty(entry.getKey(), entry.getValue());
            }

            OutputStream os = protocol.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(body.toString());

            writer.flush();
            writer.close();
            os.close();

            protocol.connect();

            return InputStreamToStringService.getString(protocol.getInputStream());
        } catch (IOException e) {
            return InputStreamToStringService.getString(protocol.getErrorStream());
        }
    }

    private String doDeleteRequest() {
        throw new UnsupportedOperationException();
    }

    public int getStatusCode() {
        return statusCode;
    }
}
