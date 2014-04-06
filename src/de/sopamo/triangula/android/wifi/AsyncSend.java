package de.sopamo.triangula.android.wifi;

import android.os.AsyncTask;
import android.util.Log;

import java.io.*;
import java.net.Socket;


public class AsyncSend extends AsyncTask<Object, Void, Void> {

    @Override
    protected Void doInBackground(Object... params) {
        Log.e("SEND", "doInBackground_CLIENT");
        Socket socket = null;

        try {
            Log.e("SEND", "TRY");
            socket = new Socket(WifiConnection.otherDevice, 8957);
            Log.e("SEND", "AFTER SOCKET");
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            //while (true) {
            Log.e("SEND", "WHILE");
            //String test = activity.text.getText().toString();
            String test = WifiConnection.otherDevice.toString();
            output.writeObject(test);
            Log.e("SEND", "AFTER WRITE OUTPUT");
            //activity.result.setText(params[0].toString());
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        Log.e("AsyncSend", "onPreExecute_CLIENT");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.e("AsyncSend", "onPostExecute_CLIENT");
        super.onPostExecute(aVoid);
    }
}
