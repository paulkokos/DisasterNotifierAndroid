package gr.teicm.ieee.madc.disasternotifierandroid.activity.general;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import gr.teicm.ieee.madc.disasternotifierandroid.R;
import gr.teicm.ieee.madc.disasternotifierandroid.globals.AppConfig;
import gr.teicm.ieee.madc.disasternotifierandroid.service.PreferencesService;

public class SettingsActivity extends AppCompatActivity {

    private SeekBar nearDistance;
    private TextView nearDistanceValue;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findUIObjects();
        initObjects();
        setEventHandlers();

    }

    private void initObjects() {
        String nearDistancePref = new PreferencesService(this).get("NEAR_DISTANCE");

        if (nearDistancePref == null) {
            nearDistancePref = AppConfig.DefaultNearDistance;
        }

        nearDistance.setProgress(Integer.parseInt(nearDistancePref));
        nearDistanceValue.setText(nearDistancePref.concat(" meters"));

    }

    private void setEventHandlers() {
        nearDistance.setOnSeekBarChangeListener(nearDistanceChange());
        save.setOnClickListener(saveClick());
    }

    private void findUIObjects() {
        nearDistance = findViewById(R.id.nearDistanceBar);
        nearDistanceValue = findViewById(R.id.nearDistanceValueView);
        save = findViewById(R.id.saveButton);
    }

    private View.OnClickListener saveClick() {
        return v -> {
            PreferencesService preferencesService = new PreferencesService(SettingsActivity.this);
            preferencesService.set("NEAR_DISTANCE", String.valueOf(nearDistance.getProgress()));
        };
    }

    private SeekBar.OnSeekBarChangeListener nearDistanceChange() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                nearDistanceValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //
            }
        };
    }

//    @Override
//    public void onBackPressed() {
//        ActivityIntentStart.doTransition(SettingsActivity.this, CentralActivity.class);
//    }
}
