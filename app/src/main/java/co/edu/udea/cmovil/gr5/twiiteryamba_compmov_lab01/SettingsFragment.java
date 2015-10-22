package co.edu.udea.cmovil.gr5.twiiteryamba_compmov_lab01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


/*
    Interfaz gráfica que aparece al seleccionar la opción settings.
    Tiene la lista que definimos en el recurso XML "Setting"

    Clase donde se almacena además las preferencias configuradas por el
    usuario de la aplicación
 */
public class SettingsFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onStart() {
        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        getActivity().sendBroadcast(new Intent("com.marakana.android.yamba.action.UPDATED_INTERVAL"));
    }


}
