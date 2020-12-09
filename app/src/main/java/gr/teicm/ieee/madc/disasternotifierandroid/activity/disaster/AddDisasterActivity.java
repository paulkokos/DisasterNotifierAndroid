package gr.teicm.ieee.madc.disasternotifierandroid.activity.disaster;

import android.app.AlertDialog;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import gr.teicm.ieee.madc.disasternotifierandroid.ActivityIntentStart;
import gr.teicm.ieee.madc.disasternotifierandroid.R;
import gr.teicm.ieee.madc.disasternotifierandroid.activity.auth.LoginActivity;
import gr.teicm.ieee.madc.disasternotifierandroid.controller.DisasterController;
import gr.teicm.ieee.madc.disasternotifierandroid.controller.impl.DisasterControllerImpl;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ConflictException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ForbiddenException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.MethodNotAllowedException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NetworkException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NoContentException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NotFoundException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.UnauthorizedException;
import gr.teicm.ieee.madc.disasternotifierandroid.model.spring.Disaster;
import gr.teicm.ieee.madc.disasternotifierandroid.service.AuthService;
import gr.teicm.ieee.madc.disasternotifierandroid.service.GeoLocation;
import gr.teicm.ieee.madc.disasternotifierandroid.service.NetworkPolicyService;

public class AddDisasterActivity extends AppCompatActivity {

    private Spinner typeOfDisaster;
    private SeekBar redRadius;
    private TextView redRadiusValueView;
    private Button submit;

    private AuthService authService;
    private DisasterController disasterController;
    private GeoLocation geoLocation;
    private Location currentLocation;
    private ArrayList<String> disasterTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_disaster);

        NetworkPolicyService.setPolicy();

        findUIObjects();
        setEventHandlers();
        initObjects();
        setData();

        if (isNotAuthorized()) {
            openLoginActivity();
        }
    }

    private void setData() {
        disasterTypes.add("FIRE");
        disasterTypes.add("FLOOD");

        typeOfDisaster.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, disasterTypes)
        );

        redRadiusValueView.setText(
                String.valueOf(redRadius.getProgress()).concat(" meters")
        );
    }

    private void initObjects() {
        geoLocation = new GeoLocation(this);
        disasterController = new DisasterControllerImpl();
        disasterTypes = new ArrayList<>();
    }

    private boolean isNotAuthorized() {
        return !isAuthorized();
    }

    private void openLoginActivity() {
        ActivityIntentStart.doTransition(AddDisasterActivity.this, LoginActivity.class);
    }

    private void setEventHandlers() {
        redRadius.setOnSeekBarChangeListener(redRadiusChange());
        submit.setOnClickListener(submitClick());
    }

    private void findUIObjects() {
        typeOfDisaster = findViewById(R.id.typeOfDisasterSpinner);
        redRadius = findViewById(R.id.redRadiusBar);
        redRadiusValueView = findViewById(R.id.redRadiusValueView);
        submit = findViewById(R.id.submitButton);
    }

    private SeekBar.OnSeekBarChangeListener redRadiusChange() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                redRadiusValueView.setText(String.valueOf(progress).concat(" meters"));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Nothing
            }
        };
    }

    private View.OnClickListener submitClick() {
        return v -> {

            findLocation();

            if (hasNotLocation()) {
                new AlertDialog
                        .Builder(AddDisasterActivity.this)
                        .setTitle("We have a problem...")
                        .setMessage("We can't find your location. Please wait and press the 'submit' button again!")
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
            } else {

                try {
                    disasterController.post(
                            generateDisaster(),
                            authService.get().getAccessToken()
                    );

                    new AlertDialog
                            .Builder(AddDisasterActivity.this)
                            .setTitle("OK")
                            .setMessage("We added your report and we have notify the nearby citizens!")
                            .setPositiveButton("OK", (dialog, which) -> openCentralActivity())
                            .create()
                            .show();
                } catch (ForbiddenException | JSONException | NotFoundException | ConflictException | MethodNotAllowedException | NoContentException e) {
                    new AlertDialog
                            .Builder(AddDisasterActivity.this)
                            .setTitle("We have a problem...")
                            .setMessage("Try again later, if this problem persists contact with us!")
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                } catch (NetworkException e) {
                    new AlertDialog
                            .Builder(AddDisasterActivity.this)
                            .setTitle("We have a problem...")
                            .setMessage("Check your internet access and try again!")
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                } catch (UnauthorizedException e) {
                    openLoginActivity();
                }
            }
        };
    }

    private void openCentralActivity() {
//        ActivityIntentStart.doTransition(AddDisasterActivity.this, CentralActivity.class);
    }

    private boolean hasLocation() {
        return currentLocation != null;
    }

    private boolean hasNotLocation() {
        return !hasLocation();
    }

    private void findLocation() {
        currentLocation = geoLocation.getLocation();
    }

//    @NonNull
    private Disaster generateDisaster() {
        return new Disaster(
                null,
                typeOfDisaster.getSelectedItem().toString(),
                new gr.teicm.ieee.madc.disasternotifierandroid.model.spring.Location(
                        (float) currentLocation.getLatitude(),
                        (float) currentLocation.getLongitude()
                ),
                null,
                (long) redRadius.getProgress(),
                null,
                null
        );
    }

    @Override
    public void onBackPressed() {
        openCentralActivity();
    }

    private boolean isAuthorized() {
        try {
            authService = new AuthService(getFilesDir().getCanonicalPath());
            return true;
        } catch (UnauthorizedException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
