package lyjak.anna.inzynierka.viewmodel.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Anna on 20.10.2017.
 */

public class GoogleMapsStaticUtil {

    public static Bitmap getGoogleMapStaticPicture(double lat, double longi){
        String stringUrl = "http://maps.google.com/maps/api/staticmap?center=" + lat + ","
                + longi + "&zoom=15&size=400x200&maptype=roadmap&sensor=false" +
                "&markers=color:orange%7Clabel:P%7C" + lat + "," + longi; //bez &maptype=roadmap
        return getStaticGoogleMaps(stringUrl);
    }

    public static Bitmap getGoogleMapStaticPictureWithPolyline(double lat, double longi, String path){
        String stringUrl = "http://maps.google.com/maps/api/staticmap?center=" + lat + ","
                + longi + "&zoom=7&size=400x200&maptype=roadmap&sensor=false" +
                "&markers=color:orange%7Clabel:Start%7C" + lat + "," + longi +
                "&path=weight:5%7Ccolor:orange%7Cenc:" + path;
        return getStaticGoogleMaps(stringUrl);
    }

    public static Bitmap getGoogleMapStaticPictureWithPolyline(String path){
        String stringUrl = "http://maps.google.com/maps/api/staticmap?" +
                "&zoom=7&size=400x200&maptype=roadmap&sensor=false" +
                "&path=weight:5%7Ccolor:orange%7Cenc:" + path;
        return getStaticGoogleMaps(stringUrl);
    }

    private static Bitmap getStaticGoogleMaps(String stringUrl) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bitmap bmp = null;
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection urlConnection =  (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.connect();
            InputStream in = urlConnection.getInputStream();
            bmp = BitmapFactory.decodeStream(in);
            in.close();
            urlConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }

}
