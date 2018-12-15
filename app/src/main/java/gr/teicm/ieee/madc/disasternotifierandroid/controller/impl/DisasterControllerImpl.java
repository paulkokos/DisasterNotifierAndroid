package gr.teicm.ieee.madc.disasternotifierandroid.controller.impl;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import gr.teicm.ieee.madc.disasternotifierandroid.controller.DisasterController;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ConflictException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ForbiddenException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.MethodNotAllowedException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NetworkException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NotFoundException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.UnauthorizedException;
import gr.teicm.ieee.madc.disasternotifierandroid.globals.AppConfig;
import gr.teicm.ieee.madc.disasternotifierandroid.model.network.HTTPMethods;
import gr.teicm.ieee.madc.disasternotifierandroid.model.spring.Disaster;
import gr.teicm.ieee.madc.disasternotifierandroid.model.spring.Location;
import gr.teicm.ieee.madc.disasternotifierandroid.service.NetworkService;
import gr.teicm.ieee.madc.disasternotifierandroid.service.RestStatusHandler;

public class DisasterControllerImpl implements DisasterController {
    @Override
    public List<Disaster> getNear(String authorization, String nearDistance) throws JSONException, ForbiddenException, MethodNotAllowedException, ConflictException, NetworkException, NotFoundException, UnauthorizedException {
        NetworkService networkService = new NetworkService(HTTPMethods.GET, AppConfig.APINear + nearDistance);

        networkService.addHeader(new AbstractMap.SimpleEntry<>("Authorization", authorization));

        networkService.addHeader(new AbstractMap.SimpleEntry<>("Accepts", "application/json"));
        networkService.addHeader(new AbstractMap.SimpleEntry<>("Content-Type", "application/json"));

        String call = networkService.call();

        RestStatusHandler.isOk(
                networkService.getStatusCode()
        );

        JSONObject response = new JSONObject(call);
        JSONArray jsonArray = response.getJSONArray("data");

        return getDisastersFromRawJSONArray(jsonArray);

    }

    @NonNull
    private List<Disaster> getDisastersFromRawJSONArray(JSONArray jsonArray) throws JSONException {
        List<Disaster> disasters = new ArrayList<>();


        for (int i = 0; i < jsonArray.length(); i++) {
            disasters.add(parseDisaster(jsonArray.getJSONObject(i)));
        }
        return disasters;
    }

    @Override
    public Disaster get(Long id, String authorization) throws JSONException, ForbiddenException, MethodNotAllowedException, ConflictException, NetworkException, NotFoundException, UnauthorizedException {
        NetworkService networkService = new NetworkService(HTTPMethods.GET, AppConfig.APIDisaster + id);

        networkService.addHeader(new AbstractMap.SimpleEntry<>("Accepts", "application/json"));
        networkService.addHeader(new AbstractMap.SimpleEntry<>("Content-Type", "application/json"));

        String call = networkService.call();

        RestStatusHandler.isOk(
                networkService.getStatusCode()
        );


        return parseDisaster(new JSONObject(call).getJSONObject("data"));
    }

    @Override
    public void post(Disaster disaster, String authorization) throws ForbiddenException, MethodNotAllowedException, ConflictException, NetworkException, NotFoundException, UnauthorizedException, JSONException {
        NetworkService networkService = new NetworkService(HTTPMethods.POST, AppConfig.APIDisaster);

        networkService.addHeader(new AbstractMap.SimpleEntry<>("Authorization", authorization));

        networkService.addHeader(new AbstractMap.SimpleEntry<>("Accepts", "application/json"));
        networkService.addHeader(new AbstractMap.SimpleEntry<>("Content-Type", "application/json"));

        networkService.addBody("disasterType", disaster.getDisasterType());

        JSONObject disasterLocation = new JSONObject();
        disasterLocation.put("latitude", disaster.getDisasterLocation().getLatitude());
        disasterLocation.put("longitude", disaster.getDisasterLocation().getLongitude());
        networkService.addBody("disasterLocation", disasterLocation);

        networkService.addBody("redRadius", String.valueOf(disaster.getRedRadius()));

        String call = networkService.call();

        RestStatusHandler.isOk(
                networkService.getStatusCode()
        );

        JSONObject response = new JSONObject(call);


        parseDisaster(response.getJSONObject("data"));
    }

    @Override
    public Disaster put(Long id, Disaster disaster, String authorization) {
        return null;
    }

    @Override
    public boolean delete(Long id, String authorization) {
        return false;
    }

    @Override
    public List<Disaster> get(String authorization) throws ForbiddenException, MethodNotAllowedException, ConflictException, NetworkException, NotFoundException, UnauthorizedException, JSONException {
        NetworkService networkService = new NetworkService(HTTPMethods.GET, AppConfig.APIDisaster);


        networkService.addHeader(new AbstractMap.SimpleEntry<>("Accepts", "application/json"));
        networkService.addHeader(new AbstractMap.SimpleEntry<>("Content-Type", "application/json"));

        String call = networkService.call();

        RestStatusHandler.isOk(
                networkService.getStatusCode()
        );

        JSONObject response = new JSONObject(call);
        JSONArray jsonArray = response.getJSONArray("data");

        return getDisastersFromRawJSONArray(jsonArray);
    }

    @Override
    public List<Disaster> getMyReports(String authorization) throws ForbiddenException, MethodNotAllowedException, ConflictException, NetworkException, NotFoundException, UnauthorizedException, JSONException {
        NetworkService networkService = new NetworkService(HTTPMethods.GET, AppConfig.APIMeReports);

        networkService.addHeader(new AbstractMap.SimpleEntry<>("Authorization", authorization));
        networkService.addHeader(new AbstractMap.SimpleEntry<>("Accepts", "application/json"));
        networkService.addHeader(new AbstractMap.SimpleEntry<>("Content-Type", "application/json"));

        String call = networkService.call();

        RestStatusHandler.isOk(
                networkService.getStatusCode()
        );

        JSONObject response = new JSONObject(call);
        JSONArray jsonArray = response.getJSONArray("data");

        return getDisastersFromRawJSONArray(jsonArray);
    }

    private Disaster parseDisaster(JSONObject disasterJson) throws JSONException {
        Long id;
        String type;
        Location disasterLocation;
        Location safeLocation;
        Long redRadius;
        Long yellowRadius;
        Long greenRadius;

        id = disasterJson.getLong("id");
        type = disasterJson.getString("disasterType");
        disasterLocation = new Location(
                Float.valueOf(disasterJson.getJSONObject("disasterLocation").getString("latitude")),
                Float.valueOf(disasterJson.getJSONObject("disasterLocation").getString("longitude"))
        );
        safeLocation = new Location(
                Float.valueOf(disasterJson.getJSONObject("safeLocation").getString("latitude")),
                Float.valueOf(disasterJson.getJSONObject("safeLocation").getString("longitude"))
        );

        redRadius = disasterJson.getLong("redRadius");
        yellowRadius = disasterJson.getLong("yellowRadius");
        greenRadius = disasterJson.getLong("greenRadius");


        return new Disaster(id, type, disasterLocation, safeLocation, redRadius, yellowRadius, greenRadius);
    }
}
