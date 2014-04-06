package de.sopamo.triangula.android.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.*;
import android.util.Log;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class WifiConnection extends BroadcastReceiver {

    private WifiP2pManager.Channel channel;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.PeerListListener peerListListener;
    private ArrayList peers = new ArrayList();
    private Context context;
    public static InetAddress otherDevice, host;
    private Boolean isConnected = false;

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
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Log.e("WifiConnection: onReceive: WIFI_P2P_STATE_CHANGED_ACTION", "WIFI_P2P_STATE_ENABLED");
            } else {
                Log.e("WifiConnection: onReceive: WIFI_P2P_STATE_CHANGED_ACTION", "WIFI_P2P_STATE_DISABLED");
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
                    Toast.makeText(context, device.toString(), Toast.LENGTH_SHORT).show();
                    //connectDevice();
                }
            }
        }else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            Log.e("WifiConnection: onReceive: WIFI_P2P_CONNECTION_CHANGED_ACTION", "else-if");
            if (wifiP2pManager == null) {
                return;
            }
            Log.e("WifiConnection: onReceive: WIFI_P2P_CONNECTION_CHANGED_ACTION", "wifiP2pManager: not NULL");
            NetworkInfo networkInfo = intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            //checks if the device is connected
            if (networkInfo.isConnected()) {
                Log.e("WifiConnection: onReceive: WIFI_P2P_CONNECTION_CHANGED_ACTION", "networkInfo: isConnected: TRUE");
                // We are connected with the other device, request connection
                // info to find group owner IP

                wifiP2pManager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {
                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo info) {
                        Log.e("WifiConnection: onReceive: WIFI_P2P_CONNECTION_CHANGED_ACTION", "requestConnectionInfo");
                        otherDevice = info.groupOwnerAddress;
                        isConnected = true;
                    }
                });
            } else {
                Log.e("WifiConnection: onReceive: WIFI_P2P_CONNECTION_CHANGED_ACTION", "networkInfo: isConnected: FALSE");
                isConnected = false;
            }
        } /* else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager()
                    .findFragmentById(R.id.frag_list);
            fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
        }         */

    }

    /*public void connectDevice() { //WifiP2pDevice device
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = this.device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(context, "Connect succeeded.",
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(context, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }       */

    //connects to the given device
    public void connectDevice(final WifiP2pDevice deviceToConnect) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = deviceToConnect.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(context, "Connect succeeded.",
                        Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(context, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public InetAddress getHost() {
        return otherDevice;
    }

    public void disconnectDevice() {
        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "Disconnect succeeded.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(context, "Disconnect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<WifiP2pDevice> getPeers() {
        return peers;
    }

    //checks the connection status of a device
    public static String getDeviceStatus(WifiP2pDevice device) {
        int deviceStatus = device.status;
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";

        }
    }

    //gets the local ip address
    public static InetAddress getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //Soll den Vorgang automatisieren
    public void initConnection(WifiP2pDevice deviceToConnectTo) {
        connectDevice(deviceToConnectTo);
        //sollte eigentlich den Handshake machen
        /*if(deviceToConnectTo.isGroupOwner()) {
            ServerHandshake handshake = new ServerHandshake();
            handshake.execute();
        } else {
            ClientHandshake handshake  = new ClientHandshake();
            handshake.execute(otherDevice);
        }  */
    }
}
