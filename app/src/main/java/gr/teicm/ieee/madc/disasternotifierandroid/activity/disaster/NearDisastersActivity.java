package gr.teicm.ieee.madc.disasternotifierandroid.activity.disaster;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import gr.teicm.ieee.madc.disasternotifierandroid.R;
import gr.teicm.ieee.madc.disasternotifierandroid.controller.DisasterController;
import gr.teicm.ieee.madc.disasternotifierandroid.controller.impl.DisasterControllerImpl;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ConflictException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ForbiddenException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.MethodNotAllowedException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NetworkException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NoContentException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NotFoundException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.UnauthorizedException;
import gr.teicm.ieee.madc.disasternotifierandroid.globals.AppConfig;
import gr.teicm.ieee.madc.disasternotifierandroid.model.spring.Disaster;
import gr.teicm.ieee.madc.disasternotifierandroid.service.AuthService;
import gr.teicm.ieee.madc.disasternotifierandroid.service.GeoLocation;
import gr.teicm.ieee.madc.disasternotifierandroid.service.NetworkPolicyService;
import gr.teicm.ieee.madc.disasternotifierandroid.service.PreferencesService;

@SuppressWarnings("FieldCanBeLocal")
public class NearDisastersActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private GeoLocation geoLocation;
    private List<Disaster> disasters;
    private AuthService authService;
    private DisasterController disasterController;
    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_disasters);

        NetworkPolicyService.setPolicy();
        findUIObjects();
        initObjects();
    }

    private void initObjects() {
        disasterController = new DisasterControllerImpl();
        disasters = new ArrayList<>();
        geoLocation = new GeoLocation(NearDisastersActivity.this);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void findUIObjects() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }

    @SuppressLint("MissingPermission")
    private void findLocationAndUpdateMap(final GoogleMap googleMap) {

        if (hasPermissions()) {
            googleMap.setMyLocationEnabled(true);

            location = getLocation();

            if (hasLocation()) {
                googleMap.moveCamera(
                        CameraUpdateFactory.newLatLng(
                                new LatLng(location.getLatitude(), location.getLongitude())
                        )
                );
            }
        }

    }

    private boolean hasLocation() {
        return location != null;
    }

    private boolean hasNotLocation() {
        return !hasLocation();
    }

    private Location getLocation() {
        return geoLocation.getLocation();
    }

    @Override
    public void onBackPressed() {
//        ActivityIntentStart.doTransition(NearDisastersActivity.this, CentralActivity.class);
    }

    private CircleOptions generateCircle(Float latitude, Float longitude, Long radius, Integer color, Long zindex) {
        return new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(radius)
                .fillColor(color)
                .zIndex(zindex)
                .strokeColor(color)
                .strokeWidth(1);
    }

    private MarkerOptions generateMarker(Float latitude, Float longitude, String name, String type, BitmapDescriptor icon) {
        return new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(name + " - " + type)
                .icon(icon);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        getNearDisastersFromServer();

        addAllDisastersOnMap(googleMap);

        findLocationAndUpdateMap(googleMap);


    }

    private void addAllDisastersOnMap(GoogleMap googleMap) {
        for (Disaster disaster : disasters) {
            addDisasterOnMap(googleMap, disaster);
        }
    }

    private void addDisasterOnMap(GoogleMap googleMap, Disaster disaster) {
        googleMap.addCircle(
                generateCircle(
                        disaster.getDisasterLocation().getLatitude(),
                        disaster.getDisasterLocation().getLongitude(),
                        disaster.getRedRadius(),
                        0xB0FF0000,
                        disaster.getId() * 10 + 3
                )
        );
        googleMap.addCircle(
                generateCircle(
                        disaster.getDisasterLocation().getLatitude(),
                        disaster.getDisasterLocation().getLongitude(),
                        disaster.getOrangeRadius(),
                        0xB0FFA500,
                        disaster.getId() * 10 + 2
                )
        );
        googleMap.addCircle(
                generateCircle(
                        disaster.getDisasterLocation().getLatitude(),
                        disaster.getDisasterLocation().getLongitude(),
                        disaster.getGreenRadius(),
                        0xB000FF00,
                        disaster.getId() * 10 + 1
                )
        );

        googleMap.addMarker(
                generateMarker(
                        disaster.getDisasterLocation().getLatitude(),
                        disaster.getDisasterLocation().getLongitude(),
                        disaster.getDisasterType(),
                        "Danger",
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
        );

        if (disaster.getSafeLocation().getLatitude() != 0) {
            googleMap.addMarker(
                    generateMarker(
                            disaster.getSafeLocation().getLatitude(),
                            disaster.getSafeLocation().getLongitude(),
                            disaster.getDisasterType(),
                            "Green",
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                    )
            );
        }
    }

    private void getNearDisastersFromServer() {


        String near_distance = new PreferencesService(this).get("NEAR_DISTANCE");

        if (near_distance == null) {
            near_distance = AppConfig.DefaultNearDistance;
        }

        try {
            authService = new AuthService(getFilesDir().getAbsolutePath());
            disasters = NearDisastersActivity.this.disasterController.getNear(
                    authService.get().getAccessToken(),
                    near_distance
            );
            System.out.print(disasters.toString());
        } catch (ForbiddenException | JSONException | NotFoundException | ConflictException | MethodNotAllowedException e) {
            new AlertDialog
                    .Builder(NearDisastersActivity.this)
                    .setTitle("We have a problem...")
                    .setMessage("Try again later, if this problem persists contact with us!")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        } catch (NetworkException e) {
            new AlertDialog
                    .Builder(NearDisastersActivity.this)
                    .setTitle("We have a problem...")
                    .setMessage("Check your internet access and try again!")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        } catch (UnauthorizedException e) {
            openLoginActivity();
        } catch (NoContentException e) {
            new AlertDialog
                    .Builder(NearDisastersActivity.this)
                    .setTitle("OK...")
                    .setMessage(
                            "You don't have any disaster near to you with the current near distance option!!"
                                    .concat("\n")
                                    .concat("You can check your currently near distance, go back and open the 'Settings' page!")
                    )
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        }
    }

    private void openLoginActivity() {
//        ActivityIntentStart.doTransition(NearDisastersActivity.this, LoginActivity.class);
    }

    private boolean hasPermissions() {
//        int accessFineLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//        int accessCoarseLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

//        boolean accessFineLocationBool = accessFineLocation == PackageManager.PERMISSION_GRANTED;
//        boolean accessCoarseLocationBool = accessCoarseLocation == PackageManager.PERMISSION_GRANTED;

//        return (accessFineLocationBool && accessCoarseLocationBool);
        return true;
    }

    boolean hasNotPermissions() {
        return !hasPermissions();
    }


}