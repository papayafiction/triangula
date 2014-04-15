package de.sopamo.triangula.android.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.*;
import android.widget.Toast;

import java.util.ArrayList;

public class WifiConnection extends BroadcastReceiver {

    private WifiP2pManager.Channel channel;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.PeerListListener peerListListener;
    private ArrayList peers = new ArrayList();
    private Context context;
    private String host;
    private WifiAsyncTask wifiAsyncTask;



    public WifiConnection(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, IntentFilter intentFilter, Context context) {

        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;
        this.context = context;
        wifiAsyncTask = new WifiAsyncTask();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                //
            } else {
                //
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (wifiP2pManager != null) {
                WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peerList) {
                        // Out with the old, in with the new.
                        peers.clear();
                        peers.addAll(peerList.getDeviceList());
                    }
                };
                wifiP2pManager.requestPeers(channel, peerListListener);

                if(peers.iterator().hasNext()) {
                    WifiP2pDevice device = (WifiP2pDevice) peers.iterator().next();
                    //Toast.makeText(context, device.toString(), Toast.LENGTH_LONG).show();
                    connectDevice(device);
                }
            }
        }else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if (wifiP2pManager == null) {
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {

                // We are connected with the other device, request connection
                // info to find group owner IP

                wifiP2pManager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {
                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo info) {
                        host = info.groupOwnerAddress.getHostAddress();
                        //Toast.makeText(context, "Host-IP-Adresse gefunden", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } /* else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager()
                    .findFragmentById(R.id.frag_list);
            fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
        }         */

    }

    public void connectDevice(WifiP2pDevice device) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.

            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(context, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void connectSockets() {
        wifiAsyncTask.send(host);
    }

    public ArrayList groupOwners(ArrayList list) {
        return null;
    }

    /*
    - Multicast und normales senden getrennt -> in Methode dann abfragen, ob es nur zwei Geräte sind,
    dann normal verbinden, sonst Multicast. Oder nur normal? Immerhin wenig Daten und Multicast angeblich Batterifresser
    (siehe dazu Link auf der Seite, wo daa vorgeschlagen wurde
    (http://stackoverflow.com/questions/19197038/android-wi-fi-direct-network)

    - womit die Daten versenden? Intent/Bundle/einfach so/usw

    -  dann dazu empfangen und weitergeben

    - und wie? Sockets und ServerSockets und so

    - evtl check ob Wifi aktiviert wurde bzw abfangen, da dann normal ja die App crasht (aber abwarten ob das überhaupt so ist)

    - Host muss wohl IP-Adresse jedes Geräts in der Gruppe an alle anderen weitergeben? Oder reicht, wenn Host Daten an alle sendet
      -> kein direktes "kennen" der Clients untereinander nötig
     */
}
