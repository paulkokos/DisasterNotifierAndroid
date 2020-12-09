package gr.teicm.ieee.madc.disasternotifierandroid.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import gr.teicm.ieee.madc.disasternotifierandroid.ActivityIntentStart;
import gr.teicm.ieee.madc.disasternotifierandroid.R;
import gr.teicm.ieee.madc.disasternotifierandroid.activity.auth.LoginActivity;
import gr.teicm.ieee.madc.disasternotifierandroid.service.NetworkPolicyService;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        NetworkPolicyService.setPolicy();

        if (hasNotPermissions()) {
            askPermissions();
        } else {
            if (isAuthorized()) {
                openCentralActivity();
            } else {
                openLoginActivity();
            }
        }

    }

    private void openLoginActivity() {
        ActivityIntentStart.doTransition(this, LoginActivity.class);
    }

    private void openCentralActivity() {
        ActivityIntentStart.doTransition(this, CentralActivity.class);
    }

    private boolean isAuthorized() {
//        try {
//            new AuthService(getFilesDir().getCanonicalPath());
            return true;
//        } catch (UnauthorizedException | IOException e) {
//            e.printStackTrace();
//            return false;
//        }
    }

    private void askPermissions() {
        ActivityCompat.requestPermissions(
                SplashActivity.this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                100
        );
    }

    private boolean hasPermissions() {
        int accessFineLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int accessCoarseLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        boolean accessFineLocationBool = accessFineLocation == PackageManager.PERMISSION_GRANTED;
        boolean accessCoarseLocationBool = accessCoarseLocation == PackageManager.PERMISSION_GRANTED;

        return (accessFineLocationBool && accessCoarseLocationBool);
    }

    private boolean hasNotPermissions() {
        return !hasPermissions();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            boolean granted = false;

            for (int result : grantResults) {
                granted = result == PackageManager.PERMISSION_GRANTED;
            }

            if (!granted) {
                new AlertDialog
                        .Builder(SplashActivity.this)
                        .setTitle("We need your location for this app...")
                        .setMessage("Please allow our app your location permission!")
                        .setPositiveButton("OK", (dialog, which) -> this.refresh())
                        .create()
                        .show();
            } else {
                refresh();
            }


        }
    }

    private void refresh() {
        ActivityIntentStart.doTransition(SplashActivity.this, SplashActivity.class);
    }


}
