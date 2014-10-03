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

    /**
     * Returns a similar color to the input color
     * This brings some diversity in the game
     *
     * @param color The base color
     * @return The simliar color
     */
    public static int getSimilarColor(int base) {
        float hsv[] = new float[3];
        Color.RGBToHSV(Color.red(base),Color.green(base),Color.blue(base),hsv);
        hsv[1] += Math.random()*0.1-0.05;
        hsv[2] += Math.random()*0.1-0.05;
        return Color.HSVToColor(hsv);
    }


}
