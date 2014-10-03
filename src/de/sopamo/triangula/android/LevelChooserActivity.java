package de.sopamo.triangula.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import de.sopamo.triangula.android.adapters.LevelAdapter;
import de.sopamo.triangula.android.levels.Level;
import de.sopamo.triangula.android.levels.backgroundElements.LevelTemplate;
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
import java.util.ArrayList;
import java.util.List;

//TODO: implement official levels, my levels (username from google+)


/**
 * Created by moe on 02.10.14.
 */
public class LevelChooserActivity extends Activity {

    public static Level level;
    private static final String BASE_URL = "http://triangula.papaya-fiction.com/getlevels.json";


    private Spinner typeSpinner;
    private Button searchButton;
    private EditText searchField;
    private ListView levelList;
    private String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_level_chooser);

        typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        searchButton = (Button) findViewById(R.id.search_button);
        searchField = (EditText) findViewById(R.id.search_field);
        levelList = (ListView) findViewById(R.id.level_list);

        final List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Official levels");
        spinnerArray.add("Community levels");
        spinnerArray.add("My levels");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchValue = searchField.getText().toString();
                String url ="";


                if (typeSpinner.getSelectedItemPosition() == 0) { //official levels

                    url=BASE_URL;
                    //TODO: implement official levels


                } else if (typeSpinner.getSelectedItemPosition() == 1) { //community levels
                    if (searchValue.isEmpty()) {
                        url =BASE_URL;
                    } else {
                        url = BASE_URL + "?search=" + searchValue;
                    }


                } else if(typeSpinner.getSelectedItemPosition() == 2) { //my levels)
                    userId = "dummyUser";
                    if(searchValue.isEmpty()) {
                        url=BASE_URL + "?tag=" + userId;
                    } else {
                        url = BASE_URL + "?tag=" + userId + "&search=" + searchValue;
                    }

                }


                LoadingTask loadingTask = new LoadingTask();
                loadingTask.execute(url);

            }
        });



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

                for(int i=0; i<root.length(); i++) {

                    JSONObject playerObj = root.getJSONObject(i);

                    String name = playerObj.getString("name");

                    JSONArray levelArray = playerObj.getJSONArray("levels");


                    for(int j=0; j<levelArray.length(); j++) {

                        LevelTemplate l = new LevelTemplate();
                        l.setCreatorTag(name);
                        l.setLevelName(levelArray.getString(j));
                        l.setIsOnlineLevel(true);
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




