package gr.teicm.ieee.madc.disasternotifierandroid.activity.disaster;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gr.teicm.ieee.madc.disasternotifierandroid.ActivityIntentStart;
import gr.teicm.ieee.madc.disasternotifierandroid.R;
import gr.teicm.ieee.madc.disasternotifierandroid.activity.CentralActivity;
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
import gr.teicm.ieee.madc.disasternotifierandroid.service.NetworkPolicyService;

public class MyReportedDisastersActivity extends AppCompatActivity {

    private AuthService authService;
    private DisasterController disasterController;
    private ListView listView;
    private ArrayList<String> aboutArray;
    private List<Disaster> myReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        NetworkPolicyService.setPolicy();

        findUIObjects();

        initObjects();

        if (isNotAuthorized()) {
            openLoginActivity();
        }

        try {
            myReports = disasterController.getMyReports(
                    authService.get().getAccessToken()
            );
        } catch (ForbiddenException | JSONException | NotFoundException | ConflictException | MethodNotAllowedException e) {
            new AlertDialog
                    .Builder(MyReportedDisastersActivity.this)
                    .setTitle("We have a problem...")
                    .setMessage("Try again later, if this problem persists contact with us!")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        } catch (NetworkException e) {
            new AlertDialog
                    .Builder(MyReportedDisastersActivity.this)
                    .setTitle("We have a problem...")
                    .setMessage("Check your internet access and try again!")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        } catch (UnauthorizedException e) {
            openLoginActivity();
        } catch (NoContentException e) {
            new AlertDialog
                    .Builder(MyReportedDisastersActivity.this)
                    .setTitle("OK...")
                    .setMessage("You don't have report any disaster yet!")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        }

        loadReportedDisasters();

        loadReportedDisastersOnListView();

    }

    private void loadReportedDisastersOnListView() {
        listView.setAdapter(
                new ArrayAdapter<>(
                        MyReportedDisastersActivity.this,
                        android.R.layout.simple_list_item_1,
                        aboutArray
                )
        );
    }

    private void loadReportedDisasters() {
        for (Disaster disaster : myReports) {
            aboutArray.add(disaster.toString());
        }
    }

    private void initObjects() {
        myReports = new ArrayList<>();
        aboutArray = new ArrayList<>();
        disasterController = new DisasterControllerImpl();
    }

    private void openLoginActivity() {
//        ActivityIntentStart.doTransition(MyReportedDisastersActivity.this, LoginActivity.class);

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

    private void findUIObjects() {
        listView = findViewById(R.id.listView);
    }

    @Override
    public void onBackPressed() {
        ActivityIntentStart.doTransition(MyReportedDisastersActivity.this, CentralActivity.class);
    }
}
