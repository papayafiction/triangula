package de.sopamo.triangula.android.wifi;

import android.os.AsyncTask;
import android.util.Log;
import de.sopamo.triangula.android.GameActivity;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerAsyncTask extends AsyncTask<Void, Void, Void> {

    private GameActivity activity;
    String test = "NULL";

    @Override
    protected Void doInBackground(Void... params) {
        activity.receiveStatus.setText("doInBackground_SERVER");
        //loop for transferring data; this task has to be canceled manually
        while(true) {
            handleConnection();
        }
    }

    @Override
    protected void onPreExecute() {
        activity.receiveStatus.setText("onPreExecute_SERVER");
        super.onPreExecute();
    }

    private void handleConnection() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(8957);
            clientSocket = serverSocket.accept();

            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            test = (String) input.readObject();

            clientSocket.close();
            activity.outputText.append(test + "\r\n");
            Log.e("ServerAsyncTask", test);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setActivity(GameActivity activity)  {
        this.activity = activity;
    }
}
