package de.sopamo.triangula.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import de.sopamo.triangula.android.levels.Level;
import de.sopamo.triangula.android.musicProcessing.MusicPlayer;
import de.sopamo.triangula.android.tools.Hooks;

import java.util.HashMap;
import java.util.List;

public class App extends Application implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {

    private static App context;
    private static App that;
    private static HashMap<Context,GoogleApiClient> mGoogleApiClients = new HashMap<Context, GoogleApiClient>();
    private static SharedPreferences settings;
    private static List<Level> levelList;
    private static AudioManager audioManager;
    private static int timesMuted;

    private Context activityContext;
    private ConnectionCallback callback;
    private boolean mResolvingConnectionFailure = false;
    private boolean mSignInFlow = false;
    private boolean mAchievements = false;
    private static boolean mSignedIn = false;
    private SharedPreferences.Editor mSharedPreferences;


    private static MusicPlayer musicPlayer;
    public static MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public void onCreate() {
        super.onCreate();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        SharedPreferences sp =  getSharedPreferences("play_services",MODE_PRIVATE);
        settings = getSharedPreferences("settings",MODE_PRIVATE);
        mSharedPreferences = sp.edit();
        context = this;
        that = this;
        musicPlayer = new MusicPlayer();
        musicPlayer.init();
    }



    public static App getContext() {
        return context;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignedIn = true;
        mSharedPreferences.putBoolean("declined",false);
        mSharedPreferences.commit();
        callback.onConnected(mGoogleApiClients.get(activityContext));
        if(mAchievements) {
            mAchievements = false;
            ((Activity)activityContext).startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClients.get(activityContext)), 5001);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClients.get(activityContext).connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mSignedIn = false;
        mSharedPreferences.putBoolean("declined",true);
        mSharedPreferences.commit();
        if (mResolvingConnectionFailure) {
            return;
        }
        if (mSignInFlow) {
            mSignInFlow = false;
            mResolvingConnectionFailure = true;
            if (!BaseGameUtils.resolveConnectionFailure(((Activity)activityContext), mGoogleApiClients.get(activityContext), connectionResult,
                    9001, "Fehler beim einloggen")) {
                return;
            }
            mResolvingConnectionFailure = false;
        }
        callback.onConnectionFailed();
    }

    private boolean isSignedIn() {
        return mSignedIn&&mGoogleApiClients.get(activityContext)!=null&&mGoogleApiClients.get(activityContext).isConnected();
    }

    public void onResult(int resultCode) {
        mResolvingConnectionFailure = false;
        if (resultCode == Activity.RESULT_OK) {
            // Make sure the app is not already connected or attempting to connect
            if (!mGoogleApiClients.get(activityContext).isConnecting() &&
                    !mGoogleApiClients.get(activityContext).isConnected()) {
                mGoogleApiClients.get(activityContext).connect();
            }
        }
    }

    public static void connectToPlayServices(ConnectionCallback callBack) {
        if(connectedToPlayServices()) callBack.onConnected(mGoogleApiClients.get(context));
        that.callback = callBack;
        that.mSignInFlow = true;
        mGoogleApiClients.put(that.activityContext, new GoogleApiClient.Builder(that.activityContext).addConnectionCallbacks(that).addOnConnectionFailedListener(that)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES).build());
        mGoogleApiClients.get(that.activityContext).connect();
    }

    public static void connectToPlayServices() {
        connectToPlayServices(new ConnectionCallback() {
            @Override
            public void onConnected(GoogleApiClient client) {
            }

            @Override
            public void onConnectionFailed() {
            }
        });
    }

    public static void connectToPlayServices(boolean startAchievementsAfterwards) {
        connectToPlayServices(new ConnectionCallback() {
            @Override
            public void onConnected(GoogleApiClient client) {
            }

            @Override
            public void onConnectionFailed() {
            }
        },startAchievementsAfterwards);
    }

    public static void connectToPlayServices(ConnectionCallback callBack,boolean startAchievementsAfterwards) {
        that.mAchievements = startAchievementsAfterwards;
        connectToPlayServices(callBack);
    }

    public static boolean connectedToPlayServices() {
        return that.isSignedIn();
    }

    public static GoogleApiClient getGoogleApiClient() {
        if(!connectedToPlayServices()) throw new ClientIsNotSignedInException();
        return mGoogleApiClients.get(that.activityContext);
    }

    public interface ConnectionCallback {
        public void onConnected(GoogleApiClient client);
        public void onConnectionFailed();
    }

    public static class ClientIsNotSignedInException extends RuntimeException  {
        @Override
        public String getMessage() {
            return "User is not logged in please check before getting Client";
        }
    }

    public static void setActivityContext(Context activityContext) {
        that.activityContext = activityContext;
    }

    public static void setLevelList(List<Level> levelList) {
        App.levelList = levelList;
    }

    public static List<Level> getLevelList() {
        return levelList;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Hooks.call(Hooks.DESTROY_ALL);
    }

    public static String getSetting(String key) {
        return settings.getString(key,"");
    }

    public static void setSetting(String key,String value) {
        settings.edit().putString(key,value).commit();
    }
}


