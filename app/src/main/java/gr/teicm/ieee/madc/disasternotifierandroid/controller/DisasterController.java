package gr.teicm.ieee.madc.disasternotifierandroid.controller;

import org.json.JSONException;

import java.util.List;

import gr.teicm.ieee.madc.disasternotifierandroid.exception.ConflictException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ForbiddenException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.MethodNotAllowedException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NetworkException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NotFoundException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.UnauthorizedException;
import gr.teicm.ieee.madc.disasternotifierandroid.model.spring.Disaster;

public interface DisasterController {

    List<Disaster> getNear(String authorization, String distance) throws JSONException, ForbiddenException, MethodNotAllowedException, ConflictException, NetworkException, NotFoundException, UnauthorizedException;

    Disaster get(Long id, String authorization) throws JSONException, ForbiddenException, MethodNotAllowedException, ConflictException, NetworkException, NotFoundException, UnauthorizedException;

    void post(Disaster disaster, String authorization) throws ForbiddenException, MethodNotAllowedException, ConflictException, NetworkException, NotFoundException, UnauthorizedException, JSONException;

    Disaster put(Long id, Disaster disaster, String authorization);

    boolean delete(Long id, String authorization);

    List<Disaster> get(String authorization) throws ForbiddenException, MethodNotAllowedException, ConflictException, NetworkException, NotFoundException, UnauthorizedException, JSONException;

    List<Disaster> getMyReports(String authorization) throws ForbiddenException, MethodNotAllowedException, ConflictException, NetworkException, NotFoundException, UnauthorizedException, JSONException;

}
