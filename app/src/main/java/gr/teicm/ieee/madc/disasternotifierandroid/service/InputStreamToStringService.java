package gr.teicm.ieee.madc.disasternotifierandroid.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

class InputStreamToStringService {
    public static String getString(InputStream inputStream) {

        if (inputStream == null) {
            return "{ \"message\" : \"" + "Error" + "\" }";
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toString();
    }
}
