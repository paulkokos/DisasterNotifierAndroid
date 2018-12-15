package gr.teicm.ieee.madc.disasternotifierandroid.controller;

import org.json.JSONException;

import gr.teicm.ieee.madc.disasternotifierandroid.exception.ConflictException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ForbiddenException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.MethodNotAllowedException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NetworkException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NotFoundException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.UnauthorizedException;
import gr.teicm.ieee.madc.disasternotifierandroid.model.spring.Auth;

public interface AuthController {
    Auth login(String username, String password) throws JSONException, NetworkException, NotFoundException, ForbiddenException, ConflictException, UnauthorizedException, MethodNotAllowedException;

    Auth register(String username,
                  String password,
                  String eMail,
                  Float latitude,
                  Float longitude,
                  String firebaseToken) throws JSONException, NetworkException, NotFoundException, ForbiddenException, ConflictException, UnauthorizedException, MethodNotAllowedException;
}
