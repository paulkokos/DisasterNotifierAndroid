package gr.teicm.ieee.madc.disasternotifierandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

@SuppressWarnings("SameParameterValue")
public class ActivityIntentStart {
    public static void doTransition(AppCompatActivity current, Class<?> next) {
        doTransition(current, next, android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private static void doTransition(AppCompatActivity current, Class<?> next, int fade_in, int fade_out) {
        Intent intent = new Intent(current, next);
        current.startActivity(intent);
        current.overridePendingTransition(fade_in, fade_out);
        current.finish();
    }

}
