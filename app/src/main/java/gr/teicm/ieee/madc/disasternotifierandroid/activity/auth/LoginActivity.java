package gr.teicm.ieee.madc.disasternotifierandroid.activity.auth;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;

import java.io.IOException;

import gr.teicm.ieee.madc.disasternotifierandroid.ActivityIntentStart;
import gr.teicm.ieee.madc.disasternotifierandroid.R;
import gr.teicm.ieee.madc.disasternotifierandroid.activity.CentralActivity;
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
import gr.teicm.ieee.madc.disasternotifierandroid.service.NetworkPolicyService;

@SuppressWarnings("FieldCanBeLocal")
public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private TextView register;

    private String sharedToken;
    private AuthController authController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NetworkPolicyService.setPolicy();

        findUIObjects();
        setEventHandlers();
        initObjects();

        requestFirebaseTokenAsync();
    }

    private void requestFirebaseTokenAsync() {
        FirebaseInstanceId
                .getInstance()
                .getInstanceId()
                .addOnSuccessListener(
                        LoginActivity.this,
                        instanceIdResult -> {
                            sharedToken = instanceIdResult.getToken();
                            System.out.print(sharedToken);
                        }
                );
    }

    private void initObjects() {
        authController = new AuthControllerImpl();
    }

    private void setEventHandlers() {
        login.setOnClickListener(loginClick());
        register.setOnClickListener(registerClick());
    }

    private void findUIObjects() {
        username = findViewById(R.id.usernameField);
        password = findViewById(R.id.passwordField);
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.registerButton);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog
                .Builder(LoginActivity.this)
                .setTitle("Closing the app...")
                .setMessage("Do you want close the application?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .create()
                .show();
    }

    private View.OnClickListener registerClick() {
        return v -> ActivityIntentStart.doTransition(LoginActivity.this, RegisterActivity.class);
    }

    private View.OnClickListener loginClick() {
        return v -> {
            try {
                Auth login = loginAndGetAuth();
                saveAuth(login);
                openCentralActivity();
            } catch (JSONException | MethodNotAllowedException | ConflictException | ForbiddenException | IOException | NoContentException e) {
                new AlertDialog
                        .Builder(LoginActivity.this)
                        .setTitle("We have a problem...")
                        .setMessage("Try again later, if this problem persists contact with us!")
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
            } catch (NetworkException e) {
                new AlertDialog
                        .Builder(LoginActivity.this)
                        .setTitle("We have a problem...")
                        .setMessage("Check your internet access and try again!")
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
            } catch (NotFoundException | UnauthorizedException e) {
                new AlertDialog
                        .Builder(LoginActivity.this)
                        .setTitle("We have a problem...")
                        .setMessage("Your username and/or password is invalid! Please try again!")
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
            }
        };
    }

    private void openCentralActivity() {
        ActivityIntentStart.doTransition(LoginActivity.this, CentralActivity.class);
    }

    private void saveAuth(Auth login) throws IOException {
        new AuthService(getFilesDir().getCanonicalPath(), login);
    }

    private Auth loginAndGetAuth() throws JSONException, NetworkException, NotFoundException, ForbiddenException, ConflictException, UnauthorizedException, MethodNotAllowedException, NoContentException {
        return authController.login(username.getText().toString(), password.getText().toString());
    }
}
