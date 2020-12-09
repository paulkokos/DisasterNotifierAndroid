package gr.teicm.ieee.madc.disasternotifierandroid.activity.auth;

import android.app.AlertDialog;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;

import java.io.IOException;

import gr.teicm.ieee.madc.disasternotifierandroid.ActivityIntentStart;
import gr.teicm.ieee.madc.disasternotifierandroid.R;
import gr.teicm.ieee.madc.disasternotifierandroid.controller.AuthController;
import gr.teicm.ieee.madc.disasternotifierandroid.controller.impl.AuthControllerImpl;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ConflictException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.ForbiddenException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.MethodNotAllowedException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NetworkException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NoContentException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.NotFoundException;
import gr.teicm.ieee.madc.disasternotifierandroid.exception.UnauthorizedException;
import gr.teicm.ieee.madc.disasternotifierandroid.model.spring.Auth;
import gr.teicm.ieee.madc.disasternotifierandroid.service.AuthService;
import gr.teicm.ieee.madc.disasternotifierandroid.service.GeoLocation;
import gr.teicm.ieee.madc.disasternotifierandroid.service.NetworkPolicyService;

@SuppressWarnings("FieldCanBeLocal")
public class RegisterActivity extends AppCompatActivity {

    private AuthController authController;
    private EditText username;
    private EditText password;
    private EditText eMail;
    private Button register;
    private TextView login;
    private String sharedToken;
    private GeoLocation geoLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        NetworkPolicyService.setPolicy();

        findUIObjects();
        setEventHandlers();
        initObjects();
        findFirebaseTokenAsync();
    }

    private void initObjects() {
        authController = new AuthControllerImpl();
        geoLocation = new GeoLocation(RegisterActivity.this);
    }

    private void findFirebaseTokenAsync() {
        FirebaseInstanceId
                .getInstance()
                .getInstanceId()
                .addOnSuccessListener(
                        RegisterActivity.this,
                        instanceIdResult -> {
                            sharedToken = instanceIdResult.getToken();
                            System.out.print(sharedToken);
                        }
                );
    }

    private void setEventHandlers() {
        register.setOnClickListener(registerClick());
//        login.setOnClickListener(loginClick());
    }

    private void findUIObjects() {
        username = findViewById(R.id.usernameField);
        password = findViewById(R.id.passwordField);
        eMail = findViewById(R.id.emailField);
        register = findViewById(R.id.registerButton);
        login = findViewById(R.id.loginButton);
    }

//    private View.OnClickListener loginClick() {
//        return v -> ActivityIntentStart.doTransition(RegisterActivity.this, LoginActivity.class);
//    }

    private View.OnClickListener registerClick() {
        return v -> {

            Location location = getLocation();

            if (hasNotLocation()) {
                new AlertDialog
                        .Builder(RegisterActivity.this)
                        .setTitle("We have a problem...")
                        .setMessage("We can't find your location. Please wait and press the 'register' button again!")
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
                return;
            }

            if (hasNotToken()) {
                new AlertDialog
                        .Builder(RegisterActivity.this)
                        .setTitle("We have a problem...")
                        .setMessage("We can't find your device id for notifications. Please wait and press the 'register' button again!")
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
                return;
            }

            try {
                Auth register = registerAndGetAuth(location);
                saveAuth(register);
//                openCentralActivity();
            } catch (JSONException | MethodNotAllowedException | NotFoundException | UnauthorizedException | ForbiddenException | IOException | NoContentException e) {
                new AlertDialog
                        .Builder(RegisterActivity.this)
                        .setTitle("We have a problem...")
                        .setMessage("Try again later, if this problem persists contact with us!")
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
            } catch (NetworkException e) {
                new AlertDialog
                        .Builder(RegisterActivity.this)
                        .setTitle("We have a problem...")
                        .setMessage("Check your internet access and try again!")
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
            } catch (ConflictException e) {
                new AlertDialog
                        .Builder(RegisterActivity.this)
                        .setTitle("We have a problem...")
                        .setMessage("We have already a user with this email and/or username! Please change them and try again!")
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
            }

        };
    }

//    private void openCentralActivity() {
//        ActivityIntentStart.doTransition(RegisterActivity.this, CentralActivity.class);
//    }

    private void saveAuth(Auth register) throws IOException {
        new AuthService(getFilesDir().getCanonicalPath(), register);
    }

    private Auth registerAndGetAuth(Location location) throws JSONException, NetworkException, NotFoundException, ForbiddenException, ConflictException, UnauthorizedException, MethodNotAllowedException, NoContentException {
        return authController.register(
                username.getText().toString(),
                password.getText().toString(),
                eMail.getText().toString(),
                (float) location.getLatitude(),
                (float) location.getLongitude(),
                sharedToken
        );
    }

    private boolean hasToken() {
        return sharedToken != null;
    }

    private boolean hasNotToken() {
        return !hasToken();
    }

    private boolean hasLocation() {
        return new GeoLocation(RegisterActivity.this).getLocation() != null;

    }

    private boolean hasNotLocation() {
        return !hasLocation();
    }

    private Location getLocation() {
        return geoLocation.getLocation();
    }

    @Override
    public void onBackPressed() {
        ActivityIntentStart.doTransition(RegisterActivity.this, LoginActivity.class);
    }
}
