package co.edu.udea.cmovil.gr5.twiiteryamba_compmov_lab01;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.thenewcircle.yamba.client.YambaClient;
import com.thenewcircle.yamba.client.YambaClientException;
import com.thenewcircle.yamba.client.YambaStatus;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class RefreshService extends IntentService {
    /*
       Agregar el método onCreate a la clase de nuestro service.
     */

    private static final String TAG = RefreshService.class.getSimpleName();
    private static boolean isEmpty = false;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (isEmpty){
            Toast.makeText(this, "Please update your username and password", Toast.LENGTH_LONG).show();
            isEmpty = false;
        }
        Log.d(TAG, "onDestroyed");
    }


    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "co.edu.udea.cmovil.gr5.twiiteryamba_compmov_lab01.action.FOO";
    private static final String ACTION_BAZ = "co.edu.udea.cmovil.gr5.twiiteryamba_compmov_lab01.action.BAZ";

    private static final String EXTRA_PARAM1 = "co.edu.udea.cmovil.gr5.twiiteryamba_compmov_lab01.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "co.edu.udea.cmovil.gr5.twiiteryamba_compmov_lab01.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RefreshService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RefreshService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public RefreshService() {
        super("RefreshService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(TAG, "onStarted");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            final String username = prefs.getString("username", "");
            final String password = prefs.getString("password", "");

            if ((TextUtils.isEmpty(username)) || TextUtils.isEmpty(password)){
                isEmpty = true;
                return;
            }
    
            // Actualizar el servicio.
            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            
            YambaClient cloud = new YambaClient(username, password);
            try {
                List<YambaStatus> timeline = cloud.getTimeline(20);
                for (YambaStatus status : timeline){
                    Log.d(TAG, String.format("%s, %s", status.getUser(), status.getMessage()));

                    // Se utiliza el mismo objeto, por tal se debe limpiar primero.
                    values.clear();
                    values.put(StatusContract.Column.ID, status.getId());
                    values.put(StatusContract.Column.USER, status.getUser());
                    values.put(StatusContract.Column.CREATED_AT, status.getCreatedAt().getTime());
                    // Se guarda el estado en la base de datos.
                    db.insertWithOnConflict(StatusContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                }
            } catch (YambaClientException e) {
                Log.e(TAG, "Failed to fetch the timeline", e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
