package de.sopamo.triangula.android;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import de.sopamo.triangula.android.levels.Level;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by moe on 03.10.14.
 */
public class StartLevelService extends IntentService{

    private static String BASE_URL = "http://triangula.papaya-fiction.com/levels/";
    private Level level;



    public StartLevelService() {
        super("StartLevelService");

    }



    @Override
    protected void onHandleIntent(Intent intent) {


        level = (Level) intent.getSerializableExtra("level");

        if(level.isOnlineLevel()) {
            LoadLevel loadLevelTask = new LoadLevel();
            loadLevelTask.execute();

        } else {

            Intent startLevel = new Intent(getApplicationContext(), GameActivity.class);
            startLevel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startLevel.putExtra("level",level);
            startActivity(startLevel);

        }


    }






    class LoadLevel extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            BufferedReader in = null;
            String data = null;

            try {
                HttpClient client = new DefaultHttpClient();

                URI website = new URI(BASE_URL+level.getCreatorTag() + "-"+level.getLevelName()+ ".txt");
                HttpGet request = new HttpGet();
                request.setURI(website);
                HttpResponse response = client.execute(request);
                int status = response.getStatusLine().getStatusCode();
                if(status != 200) {
                    return "";
                }

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String l = "";
                String nl = System.getProperty("line.separator");
                while ((l = in.readLine()) != null) {
                    sb.append(l + nl);
                }
                in.close();
                data = sb.toString();
                return data;
            } catch (ClientProtocolException e) {
                return "";
            } catch (IOException e) {
                return "";
            } catch (URISyntaxException e) {
                return "";
            } finally {
                if (in != null) {
                    try {
                        in.close();
                        return data;
                    } catch (Exception e) {
                        return "";
                    }
                }
            }
        }


        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);

            level.setLevelString(jsonData);


            final Intent startLevel = new Intent(getApplicationContext(), GameActivity.class);
            startLevel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startLevel.putExtra("level",level);
            startActivity(startLevel);


        }
    }

}
