package gr.teicm.ieee.madc.disasternotifierandroid.service;

import gr.teicm.ieee.madc.disasternotifierandroid.exception.ConflictException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ForbiddenException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.MethodNotAllowedException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NetworkException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NotFoundException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.UnauthorizedException;

public class RestStatusHandler {

    public static void isOk(Integer statusCode) throws UnauthorizedException, ForbiddenException, NotFoundException, MethodNotAllowedException, ConflictException, NetworkException {
        switch (statusCode) {
            case 200: {
                return;
            }
            case 201: {
                return;
            }
            case 401: {
                throw new UnauthorizedException();
            }
            case 403: {
                throw new ForbiddenException();
            }
            case 404: {
                throw new NotFoundException();
            }
            case 405: {
                throw new MethodNotAllowedException();
            }
            case 409: {
                throw new ConflictException();
            }
            default: {
                throw new NetworkException();
            }
        }

    }

}
