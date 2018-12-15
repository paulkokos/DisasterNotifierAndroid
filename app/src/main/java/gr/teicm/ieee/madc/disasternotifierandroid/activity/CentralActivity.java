package gr.teicm.ieee.madc.disasternotifierandroid.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;

import java.io.IOException;

import gr.teicm.ieee.madc.disasternotifierandroid.ActivityIntentStart;
import gr.teicm.ieee.madc.disasternotifierandroid.R;
import gr.teicm.ieee.madc.disasternotifierandroid.activity.auth.LoginActivity;
import gr.teicm.ieee.madc.disasternotifierandroid.activity.disaster.AddDisasterActivity;
import gr.teicm.ieee.madc.disasternotifierandroid.activity.disaster.MyReportedDisastersActivity;
import gr.teicm.ieee.madc.disasternotifierandroid.activity.disaster.NearDisastersActivity;
import gr.teicm.ieee.madc.disasternotifierandroid.activity.general.AboutActivity;
import gr.teicm.ieee.madc.disasternotifierandroid.activity.general.SettingsActivity;
import gr.teicm.ieee.madc.disasternotifierandroid.controller.UserController;
import gr.teicm.ieee.madc.disasternotifierandroid.controller.impl.UserControllerImpl;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ConflictException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ForbiddenException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.MethodNotAllowedException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NetworkException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NoContentException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NotFoundException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.UnauthorizedException;
import gr.teicm.ieee.madc.disasternotifierandroid.service.AuthService;
import gr.teicm.ieee.madc.disasternotifierandroid.service.GeoLocation;
import gr.teicm.ieee.madc.disasternotifierandroid.service.NetworkPolicyService;

@SuppressWarnings("FieldCanBeLocal")
public class CentralActivity extends AppCompatActivity {

    private Button near;
    private Button add;
    private Button myReports;
    private Button settings;
    private Button about;
    private Button exit;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);

        NetworkPolicyService.setPolicy();

        findUIObjects();
        setEventHandlers();

        if (isNotAuthorized()) {
            openLoginActivity();
        }

        findAndUpdateFirebaseTokenAsync();
        findAndUpdateLocationAsync();

    }

    private void findAndUpdateLocationAsync() {
        GeoLocation geoLocation = new GeoLocation(this);
    }

    private void findAndUpdateFirebaseTokenAsync() {
        FirebaseInstanceId
                .getInstance()
                .getInstanceId()
                .addOnSuccessListener(
                        CentralActivity.this,
                        instanceIdResult -> {
                            updateFirebaseToken(instanceIdResult.getToken());
                        }
                );
    }

    private void updateFirebaseToken(String token) {
        UserController userController = new UserControllerImpl();
        try {
            userController
                    .updateToken(
                            authService.get().getAccessToken(),
                            token
                    );
        } catch (JSONException | ForbiddenException | ConflictException | MethodNotAllowedException | NoContentException | UnauthorizedException | NetworkException | NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void openLoginActivity() {
        ActivityIntentStart.doTransition(CentralActivity.this, LoginActivity.class);
    }

    private boolean isNotAuthorized() {
        return !isAuthorized();
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

    private void setEventHandlers() {
        near.setOnClickListener(nearClick());
        add.setOnClickListener(addClick());
        myReports.setOnClickListener(myReportsClick());
        settings.setOnClickListener(settingsClick());
        about.setOnClickListener(aboutClick());
        exit.setOnClickListener(exitClick());
    }

    private void findUIObjects() {
        near = findViewById(R.id.nearButton);
        add = findViewById(R.id.addButton);
        myReports = findViewById(R.id.myReportsButton);
        settings = findViewById(R.id.settingsButton);
        about = findViewById(R.id.aboutButton);
        exit = findViewById(R.id.exitButton);
    }

    @Override
    public void onBackPressed() {
        exit.performClick();
    }

    private View.OnClickListener nearClick() {
        return v -> ActivityIntentStart.doTransition(CentralActivity.this, NearDisastersActivity.class);

    }

    private View.OnClickListener addClick() {
        return v -> ActivityIntentStart.doTransition(CentralActivity.this, AddDisasterActivity.class);

    }

    private View.OnClickListener myReportsClick() {
        return v -> ActivityIntentStart.doTransition(CentralActivity.this, MyReportedDisastersActivity.class);

    }

    private View.OnClickListener settingsClick() {
        return v -> ActivityIntentStart.doTransition(CentralActivity.this, SettingsActivity.class);

    }

    private View.OnClickListener aboutClick() {
        return v -> ActivityIntentStart.doTransition(CentralActivity.this, AboutActivity.class);

    }

    private View.OnClickListener exitClick() {
        return v -> new AlertDialog
                .Builder(v.getContext())
                .setTitle("Closing the app...")
                .setMessage("Do you want close the application?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .create()
                .show();
    }
}
