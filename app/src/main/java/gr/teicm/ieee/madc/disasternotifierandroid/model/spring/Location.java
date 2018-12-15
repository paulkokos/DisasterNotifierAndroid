package gr.teicm.ieee.madc.disasternotifierandroid.model.spring;

import android.support.annotation.NonNull;

public class Location {

    private Float latitude;
    private Float longitude;

    public Location(Float latitude, Float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {
        this.latitude = (float) 0;
        this.longitude = (float) 0;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    @NonNull
    @Override
    public String toString() {
        return getLatitude().toString().concat(" ").concat(getLongitude().toString());
    }
}
