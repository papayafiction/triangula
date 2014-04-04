package de.sopamo.triangula.android.tools;

import android.graphics.Color;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class Util {
    public static int hex2Color(String colorStr) {
        return Color.parseColor("#" + colorStr);
    }

    public static float[] getColorParts(int color) {
        float[] parts = new float[3];
        parts[0] = (float) Color.red(color) / 255f;
        parts[1] = (float) Color.green(color) / 255f;
        parts[2] = (float) Color.blue(color) / 255f;
        return parts;
    }


}
