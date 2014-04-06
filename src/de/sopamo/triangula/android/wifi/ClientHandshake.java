package de.sopamo.triangula.android.wifi;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandshake extends AsyncTask<InetAddress, Void, Void> {

    @Override
    protected Void doInBackground(InetAddress... params) {
        Socket socket = null;

        try {
            Log.e("SEND", "TRY");
            socket = new Socket(params[0], 9000);
            Log.e("SEND", "AFTER SOCKET");
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            //while (true) {
            Log.e("SEND", "WHILE");
            output.writeObject(WifiConnection.getLocalIpAddress());
            Log.e("SEND", "AFTER WRITE OUTPUT");
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
}
