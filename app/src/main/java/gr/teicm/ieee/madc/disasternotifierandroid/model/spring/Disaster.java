package gr.teicm.ieee.madc.disasternotifierandroid.model.spring;

//import android.support.annotation.NonNull;

public class Disaster {

    private Long id;
    private String disasterType;
    private Location disasterLocation;
    private Location safeLocation;

    private Long redRadius;
    private Long orangeRadius;
    private Long greenRadius;

    public Disaster(Long id, String disasterType, Location disasterLocation, Location safeLocation, Long redRadius, Long orangeRadius, Long greenRadius) {
        this.id = id;
        this.disasterType = disasterType;
        this.disasterLocation = disasterLocation;
        this.safeLocation = safeLocation;
        this.redRadius = redRadius;
        this.orangeRadius = orangeRadius;
        this.greenRadius = greenRadius;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(String disasterType) {
        this.disasterType = disasterType;
    }

    public Location getDisasterLocation() {
        return disasterLocation;
    }

    public void setDisasterLocation(Location disasterLocation) {
        this.disasterLocation = disasterLocation;
    }

    public Location getSafeLocation() {
        return safeLocation;
    }

    public void setSafeLocation(Location safeLocation) {
        this.safeLocation = safeLocation;
    }

    public Long getRedRadius() {
        return redRadius;
    }

    public void setRedRadius(Long redRadius) {
        this.redRadius = redRadius;
    }

    public Long getOrangeRadius() {
        return orangeRadius;
    }

    public void setOrangeRadius(Long orangeRadius) {
        this.orangeRadius = orangeRadius;
    }

    public Long getGreenRadius() {
        return greenRadius;
    }

    public void setGreenRadius(Long greenRadius) {
        this.greenRadius = greenRadius;
    }

//    @NonNull
    @Override
    public String toString() {
        return getDisasterType().concat(" ").concat(getDisasterLocation().toString()).concat(" ");
    }
}
