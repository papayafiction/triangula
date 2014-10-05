package de.sopamo.triangula.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.google.android.gms.plus.Plus;
import de.sopamo.triangula.android.adapters.LevelAdapter;
import de.sopamo.triangula.android.levels.BaseOnlineLevel;
import de.sopamo.triangula.android.levels.Level;
import de.sopamo.triangula.android.levels.official.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//TODO: implement official levels, my levels (username from google+)


/**
 * Created by moe on 02.10.14.
 */
public class LevelChooserActivity extends Activity {

    public static Level level;
    private static final String BASE_URL = "http://triangula-login.papaya-fiction.com/api/getlevels.json";


    private Spinner typeSpinner;
    private Button searchButton;
    private EditText searchField;
    private ListView levelList;
    private String userId;
    private TextView emptyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_chooser);

        typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        searchButton = (Button) findViewById(R.id.search_button);
        searchField = (EditText) findViewById(R.id.search_field);
        levelList = (ListView) findViewById(R.id.level_list);
        emptyTextView = (TextView) findViewById(android.R.id.empty);


        final List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Official levels");
        spinnerArray.add("Community levels (online)");
        spinnerArray.add("My levels (online)");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchValue = searchField.getText().toString();
                String url ="";


                if (typeSpinner.getSelectedItemPosition() == 0) { //official levels

                    loadOfficialLevels(searchValue);
                    return;

                } else if (typeSpinner.getSelectedItemPosition() == 1) { //community levels
                    if (searchValue.isEmpty()) {
                        url = BASE_URL;
                    } else {
                        url = BASE_URL + "?search=" + searchValue;
                    }


                } else if(typeSpinner.getSelectedItemPosition() == 2) { //my levels)

                    if(null==userId) {
                        //TODO: get google+ user id
                    }

                    if(searchValue.isEmpty()) {
                        url = BASE_URL + "?googleid=" + userId;
                    } else {
                        url = BASE_URL + "?googleid=" + userId + "&search=" + searchValue;
                    }

                }


                LoadingTask loadingTask = new LoadingTask();
                loadingTask.execute(url);

            }
        });



        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String searchValue = null;
                try {
                    searchValue = URLEncoder.encode(searchField.getText().toString(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String url ="";



                if(i==0) {  //official levels

                    loadOfficialLevels(searchValue);
                    return;

                } else if(i==1) { //community levels (online)

                    if (searchValue.isEmpty()) {
                        url = BASE_URL;
                    } else {
                        url = BASE_URL + "?search=" + searchValue;
                    }

                } else if(i==2) { //my levels (online)

                    if(null==userId) {
                        if(App.connectedToPlayServices()) {
                            userId = Plus.AccountApi.getAccountName(App.getGoogleApiClient());
                            try {
                                userId = URLEncoder.encode(userId,"utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if(searchValue.isEmpty()) {
                        url = BASE_URL + "?google_email=" + userId;
                    } else {
                        url = BASE_URL + "?google_email=" + userId + "&search=" + searchValue;
                    }
                }


                LoadingTask loadingTask = new LoadingTask();
                loadingTask.execute(url);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //nothing to do here!
            }
        });



        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    (null!=event&&event.getAction() == KeyEvent.ACTION_DOWN &&
                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    searchButton.callOnClick();

                }
                return false; // pass on to other listeners.

            }
        });


        levelList.setEmptyView(emptyTextView);


    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp =  getSharedPreferences("play_services",MODE_PRIVATE);
        if(!sp.getBoolean("declined",false)) {
            App.connectToPlayServices();
        }
    }

    private void loadOfficialLevels(String searchValue) {

        searchValue=searchValue.toLowerCase();

        List<Level> levels = new ArrayList<Level>();
        levels.add(new Level1());
        levels.add(new Level2());
        levels.add(new Level3());
        levels.add(new Level4());
        levels.add(new Movethetime());
        levels.add(new Doorception());
        levels.add(new NextLevel());
        levels.add(new Steps());
        levels.add(new Starter());
        levels.add(new Ascending());
        levels.add(new Waypoint());
        levels.add(new Sixpack());
        levels.add(new Foxtail());

        if(!searchValue.isEmpty()) {

            for (Iterator<Level> iterator = levels.iterator(); iterator.hasNext(); ) {
                Level level = iterator.next();
                if(!level.getLevelName().toLowerCase().contains(searchValue)){
                    iterator.remove();
                }
            }

        }

        levelList.setAdapter(new LevelAdapter(getApplicationContext(),levels));

    }




    class LoadingTask extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog = new ProgressDialog(LevelChooserActivity.this);
        InputStream inputStream = null;
        String result = "";

        protected void onPreExecute() {

            progressDialog.setMessage("Updating...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    LoadingTask.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                HttpClient httpClient = new DefaultHttpClient();


                params[0] = params[0].replaceAll("\\s","");


                HttpGet httpGet = new HttpGet(params[0]);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();

                // Read content & Log
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncodingException", e1.toString());
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }
            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

                return result;

            } catch (Exception e) {
                Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
                return null;
            }

        }


        protected void onPostExecute(String jsonRaw) {

            List<Level> levels = new ArrayList<Level>();


            try {

                JSONArray root = new JSONObject(jsonRaw).getJSONArray("players");
                int next = 1;
                for(int i=0; i<root.length(); i++) {
                    JSONObject playerObj = root.getJSONObject(i);

                    String name = playerObj.getString("name");


                    JSONArray levelArray = playerObj.getJSONArray("levels");


                    for(int j=0; j<levelArray.length(); j++) {

                        String levelUrl = levelArray.getString(j);

                        String levelName=levelUrl.replace(levelUrl.substring(0, levelUrl.indexOf("-")+1), "");
                        levelName=levelName.replace(levelName.substring(levelName.indexOf("."), levelName.length()), "");

                        BaseOnlineLevel l = new BaseOnlineLevel(name, levelName, levelUrl,next++);
                        levels.add(l);
                    }

                }

                levelList.setAdapter(new LevelAdapter(getApplicationContext(),levels));

            } catch (JSONException e) {
                    Log.e("JSONException", "Error: " + e.toString());

            } finally {
                this.progressDialog.dismiss();
            }
        }
    }




}




