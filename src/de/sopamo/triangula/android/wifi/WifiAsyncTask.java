package de.sopamo.triangula.android.wifi;

import android.os.AsyncTask;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WifiAsyncTask extends AsyncTask {

    @Override
    protected String doInBackground(Object[] params) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(8000);
            clientSocket = serverSocket.accept();

            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

            oos.writeBytes("Test");
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void send(String host) {
        Socket socket = null;
        try {
            socket = new Socket(host, 8000);
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Connected");
            System.out.println("Input: " + input.readObject());
            while (true) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                String s = br.readLine();
                output.writeObject(s);
                output.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
