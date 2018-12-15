package gr.teicm.ieee.madc.disasternotifierandroid.service;

import java.io.IOException;

import gr.teicm.ieee.madc.disasternotifierandroid.exception.UnauthorizedException;
import gr.teicm.ieee.madc.disasternotifierandroid.globals.AppConfig;
import gr.teicm.ieee.madc.disasternotifierandroid.model.spring.Auth;

public class AuthService {

    private final String filesDir;
    private Auth auth;

    public AuthService(String filesDir, Auth auth) throws IOException {
        this.filesDir = filesDir;
        this.auth = auth;
        save();
    }

    public AuthService(String filesDir) throws UnauthorizedException {
        this.filesDir = filesDir;
        try {
            this.auth = load();
        } catch (IOException | ClassNotFoundException e) {
            throw new UnauthorizedException();
        }
    }

    private void save() throws IOException {
        FileService fileService = new FileService();
        fileService.saveFile(filesDir + AppConfig.AuthFile, auth);
    }

    private Auth load() throws IOException, ClassNotFoundException {
        return (Auth) new FileService().readFile(filesDir + AppConfig.AuthFile);
    }

    public Auth get() {
        return auth;
    }

}
