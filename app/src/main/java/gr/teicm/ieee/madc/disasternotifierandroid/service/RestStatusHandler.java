package gr.teicm.ieee.madc.disasternotifierandroid.service;

import gr.teicm.ieee.madc.disasternotifierandroid.exception.ConflictException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ForbiddenException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.MethodNotAllowedException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NetworkException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NoContentException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NotFoundException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.UnauthorizedException;

public class RestStatusHandler {

    public static void isOk(Integer statusCode) throws UnauthorizedException, ForbiddenException, NotFoundException, MethodNotAllowedException, ConflictException, NetworkException, NoContentException {
        switch (statusCode) {
            case 200: {
                return;
            }
            case 201: {
                return;
            }
            case 204: {
                throw new NoContentException();
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
