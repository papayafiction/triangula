package de.sopamo.triangula.android.wifi;

import android.os.AsyncTask;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerHandshake extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(9000);
            clientSocket = serverSocket.accept();

            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            WifiConnection.otherDevice = (InetAddress) input.readObject();

            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        super.onPostExecute(aVoid);
    }
}
