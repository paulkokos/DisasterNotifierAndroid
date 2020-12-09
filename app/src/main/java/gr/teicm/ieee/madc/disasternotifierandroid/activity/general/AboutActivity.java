package gr.teicm.ieee.madc.disasternotifierandroid.activity.general;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import gr.teicm.ieee.madc.disasternotifierandroid.R;

public class AboutActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> aboutArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        findUIObjects();

        initObjects();
        addData();

        updateUIObjects();

    }

    private void updateUIObjects() {
        listView.setAdapter(
                new ArrayAdapter<>(
                        AboutActivity.this,
                        android.R.layout.simple_list_item_1,
                        aboutArray
                )
        );
    }

    private void addData() {
        aboutArray.add(getString(R.string.nameKey) + "\n" + getString(R.string.nameValue));
        aboutArray.add(getString(R.string.versionKey) + "\n" + getString(R.string.versionValue));
        aboutArray.add(getString(R.string.developerKey) + "\n" + getString(R.string.developerValue));
        aboutArray.add(getString(R.string.devNoA) + "\n" + getString(R.string.devIK));
        aboutArray.add(getString(R.string.devNoB) + "\n" + getString(R.string.devPK));
        aboutArray.add(getString(R.string.ieeeKey) + "\n" + getString(R.string.ieeeValue));

        aboutArray.add(getString(R.string.licenseKey) + "\n" + getString(R.string.licenseValue));
    }

    private void initObjects() {
        aboutArray = new ArrayList<>();
    }

    private void findUIObjects() {
        listView = findViewById(R.id.listView);
    }

//    @Override
//    public void onBackPressed() {
//        ActivityIntentStart.doTransition(this, CentralActivity.class);
//    }
}
