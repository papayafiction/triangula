package de.sopamo.triangula.android.levels;

import android.os.AsyncTask;
import de.sopamo.triangula.android.LevelChooserActivity;
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

public class OnlineData extends AsyncTask<String,Void,String> {

    private OnlineLevel level;

    public OnlineData(OnlineLevel level) {
        this.level = level;
    }

    @Override
    protected String doInBackground(String... params) {

        BufferedReader in = null;
        String data = null;

        try {
            HttpClient client = new DefaultHttpClient();

            URI website = new URI(params[0]);
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
    protected void onPostExecute(String s) {
        level.setLevelString(s);
        LevelChooserActivity.startGame();
    }
}
