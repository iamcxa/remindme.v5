package tw.geodoer.main.taskPreference.view;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tw.geodoer.geotodo.R;


/**
 * @author cxa
 */
public class AppPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void addPreferencesFromIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.addPreferencesFromIntent(intent);
    }

    @Override
    public void addPreferencesFromResource(int preferencesResId) {
        // TODO Auto-generated method stub
        super.addPreferencesFromResource(preferencesResId);
    }

    @Override
    public Preference findPreference(CharSequence key) {
        // TODO Auto-generated method stub
        return super.findPreference(key);
    }

    @Override
    public PreferenceManager getPreferenceManager() {
        // TODO Auto-generated method stub
        return super.getPreferenceManager();
    }

    @Override
    public PreferenceScreen getPreferenceScreen() {
        // TODO Auto-generated method stub
        return super.getPreferenceScreen();
    }

    @Override
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        // TODO Auto-generated method stub
        super.setPreferenceScreen(preferenceScreen);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {
        // TODO Auto-generated method stub
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();


    }


}
