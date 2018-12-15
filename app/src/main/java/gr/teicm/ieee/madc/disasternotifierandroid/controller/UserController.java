package gr.teicm.ieee.madc.disasternotifierandroid.controller;

import org.json.JSONException;

import gr.teicm.ieee.madc.disasternotifierandroid.exception.ConflictException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ForbiddenException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.MethodNotAllowedException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NetworkException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NoContentException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NotFoundException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.UnauthorizedException;

public interface UserController {

    void updateLocation(String authorization, Double latitude, Double longitude) throws ForbiddenException, MethodNotAllowedException, ConflictException, NetworkException, NotFoundException, UnauthorizedException, JSONException, NoContentException;

    void updateToken(String authorization, String token) throws JSONException, ForbiddenException, MethodNotAllowedException, ConflictException, NetworkException, NotFoundException, UnauthorizedException, NoContentException;

}
