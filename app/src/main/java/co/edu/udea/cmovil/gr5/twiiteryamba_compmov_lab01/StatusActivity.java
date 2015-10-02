package co.edu.udea.cmovil.gr5.twiiteryamba_compmov_lab01;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thenewcircle.yamba.client.YambaClient;

public class StatusActivity extends AppCompatActivity {

    /*
    Instanciar variables globales.
     */
    private static final String TAG = StatusActivity.class.getSimpleName();
    private Button mButtonTweet;
    private EditText mTextStatus;
    private TextView mTextCount;
    private int mDefaultColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        // Inicializando.
        mButtonTweet = (Button) findViewById(R.id.status_button_tweet);
        mTextStatus = (EditText) findViewById(R.id.status_text);
        mTextCount = (TextView) findViewById(R.id.status_text_count);
        mTextCount.setText(Integer.toString(140));
        mDefaultColor = mTextCount.getTextColors().getDefaultColor();

        mButtonTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = mTextStatus.getText().toString();
                PostTask postTask = new PostTask();
                postTask.execute(status);
                Log.d(TAG, "onClicked");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class PostTask extends AsyncTask<String, Void, String> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute(){
            progress = ProgressDialog.show(StatusActivity.this,"Posting", "Please wait...");
            progress.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                YambaClient cloud = new YambaClient("student", "password");
                cloud.postStatus(params[0]);

                Log.d(TAG, "Successfully posted to the cloud: " + params[0]);
                return "Successfully posted";

            } catch (Exception e) {
                Log.e(TAG, "Failed to post to the cloud", e);
                e.printStackTrace();
                return "Failed to post";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();
            if (this != null && result != null) {
                Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
