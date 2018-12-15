package gr.teicm.ieee.madc.disasternotifierandroid.controller.impl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractMap;

import gr.teicm.ieee.madc.disasternotifierandroid.controller.AuthController;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ConflictException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ForbiddenException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.MethodNotAllowedException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NetworkException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NotFoundException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.UnauthorizedException;
import gr.teicm.ieee.madc.disasternotifierandroid.globals.AppConfig;
import gr.teicm.ieee.madc.disasternotifierandroid.model.network.HTTPMethods;
import gr.teicm.ieee.madc.disasternotifierandroid.model.spring.Auth;
import gr.teicm.ieee.madc.disasternotifierandroid.service.NetworkService;
import gr.teicm.ieee.madc.disasternotifierandroid.service.RestStatusHandler;

public class AuthControllerImpl implements AuthController {
    @Override
    public Auth login(String username, String password) throws JSONException, NetworkException, NotFoundException, ForbiddenException, ConflictException, UnauthorizedException, MethodNotAllowedException {
        // Init Network Service
        NetworkService networkService = new NetworkService(HTTPMethods.POST, AppConfig.APILogin);

        networkService.addHeader(new AbstractMap.SimpleEntry<>("Accepts", "application/json"));
        networkService.addHeader(new AbstractMap.SimpleEntry<>("Content-Type", "application/json"));

        networkService.addBody("username", username);
        networkService.addBody("password", password);

        String call = networkService.call();

        RestStatusHandler.isOk(
                networkService.getStatusCode()
        );

        return getAuthFromRawJsonObject(new JSONObject(call));
    }

    @Override
    public Auth register(
            String username,
            String password,
            String eMail,
            Float latitude,
            Float longitude,
            String firebaseToken
    ) throws JSONException, NetworkException, NotFoundException, ForbiddenException, ConflictException, UnauthorizedException, MethodNotAllowedException {
        NetworkService networkService = new NetworkService(HTTPMethods.POST, AppConfig.APIRegister);

        networkService.addHeader(new AbstractMap.SimpleEntry<>("Accepts", "application/json"));
        networkService.addHeader(new AbstractMap.SimpleEntry<>("Content-Type", "application/json"));

        networkService.addBody("username", username);
        networkService.addBody("password", password);
        networkService.addBody("eMail", eMail);

        networkService.addBody("firebaseToken", firebaseToken);

        JSONObject location = new JSONObject();
        location.put("latitude", latitude);
        location.put("longitude", longitude);

        networkService.addBody("location", location);

        String call = networkService.call();

        RestStatusHandler.isOk(
                networkService.getStatusCode()
        );


        return getAuthFromRawJsonObject(new JSONObject(call));
    }

    private Auth getAuthFromRawJsonObject(JSONObject jsonObject) throws JSONException {
        return new Auth(jsonObject.getJSONObject("data").getString("accessToken"));
    }

}
