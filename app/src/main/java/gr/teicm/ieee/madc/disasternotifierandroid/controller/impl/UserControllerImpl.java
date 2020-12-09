package gr.teicm.ieee.madc.disasternotifierandroid.controller.impl;

import org.json.JSONException;

import java.util.AbstractMap;

import gr.teicm.ieee.madc.disasternotifierandroid.controller.UserController;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ConflictException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ForbiddenException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.MethodNotAllowedException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NetworkException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NoContentException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NotFoundException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.UnauthorizedException;
import gr.teicm.ieee.madc.disasternotifierandroid.globals.AppConfig;
import gr.teicm.ieee.madc.disasternotifierandroid.model.network.HTTPMethods;
import gr.teicm.ieee.madc.disasternotifierandroid.service.NetworkService;
import gr.teicm.ieee.madc.disasternotifierandroid.service.RestStatusHandler;

public class UserControllerImpl implements UserController {
    @Override
    public void updateLocation(String authorization, Double latitude, Double longitude) throws ForbiddenException, MethodNotAllowedException, ConflictException, NetworkException, NotFoundException, UnauthorizedException, JSONException, NoContentException {
//        NetworkService networkService = new NetworkService(HTTPMethods.PUT, AppConfig.APIMeLocation);
//
//        networkService.addHeader(new AbstractMap.SimpleEntry<>("Authorization", authorization));
//
//        networkService.addHeader(new AbstractMap.SimpleEntry<>("Accepts", "application/json"));
//        networkService.addHeader(new AbstractMap.SimpleEntry<>("Content-Type", "application/json"));
//
//        networkService.addBody("latitude", String.valueOf(latitude));
//        networkService.addBody("longitude", String.valueOf(longitude));
//
//        networkService.call();
//
//        RestStatusHandler.isOk(
//                networkService.getStatusCode()
//        );
    }

    @Override
    public void updateToken(String authorization, String token) throws JSONException, ForbiddenException, MethodNotAllowedException, ConflictException, NetworkException, NotFoundException, UnauthorizedException, NoContentException {
//        NetworkService networkService = new NetworkService(HTTPMethods.PUT, AppConfig.APIMeToken);
//
//        networkService.addHeader(new AbstractMap.SimpleEntry<>("Authorization", authorization));
//
//        networkService.addHeader(new AbstractMap.SimpleEntry<>("Accepts", "application/json"));
//        networkService.addHeader(new AbstractMap.SimpleEntry<>("Content-Type", "application/json"));
//
//        networkService.addBody("firebaseToken", String.valueOf(token));
//
//        networkService.call();
//
//        RestStatusHandler.isOk(
//                networkService.getStatusCode()
//        );
    }
}
