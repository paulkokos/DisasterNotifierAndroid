package gr.teicm.ieee.madc.disasternotifierandroid.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings("FieldCanBeLocal")
public class PreferencesService {
    private final String MY_PREF;

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public PreferencesService(Context context) {
        MY_PREF = context.getPackageName();
        this.sharedPreferences = context.getSharedPreferences(MY_PREF, 0);
        this.editor = this.sharedPreferences.edit();
    }

    public void set(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
    }

    public String get(String key) {
        return this.sharedPreferences.getString(key, null);
    }

    public void clear(String key) {
        this.editor.remove(key);
        this.editor.commit();
    }

    public void clear() {
        this.editor.clear();
        this.editor.commit();
    }
}
