package lyjak.anna.inzynierka.model.findRoute;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lyjak.anna.inzynierka.viewmodel.others.RouteBeetweenTwoPointsDTO;

/**
 *
 * Created by Anna on 26.12.2016.
 */

@SuppressWarnings("ALL")
public class RoadFinder {

    private static final String URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_KEY = "";
    private FindDirectionListener listener;
    private LatLng origin; // start's point
    private LatLng destination; // destination point
    private String url;

    RoadFinder(FindDirectionListener listener, LatLng start, LatLng end) {
        this.listener = listener;
        this.origin = start;
        this.destination = end;
    }

    void execute() throws UnsupportedEncodingException{
        listener.onStartFindDirection();
        DownloadRawData temp = new DownloadRawData();
        if(url != null){
            temp.execute(url);
        }
        else {
            temp.execute(createUrl());
        }
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlOrigin = origin.latitude + "," + origin.longitude;
        String urlDestination = destination.latitude + "," + destination.longitude;
        return URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination +
                "&key=" + GOOGLE_KEY;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        AsyncTask.Status status;
        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuilder buffer = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                return buffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSON(res);
                status = Status.FINISHED;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    Method uses JSONObject to detect polyline between two points
     */
    private void parseJSON(String data) throws JSONException {
        if (data == null)
            return;

        List<RouteBeetweenTwoPointsDTO> routes = new ArrayList<>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            RouteBeetweenTwoPointsDTO route = new RouteBeetweenTwoPointsDTO();

            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
            JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

            route.setDistance(jsonDistance.getInt("value"));
            route.setDuration(jsonDuration.getInt("value"));
            route.createStartPoint(
                    jsonStartLocation.getDouble("lat"),
                    jsonStartLocation.getDouble("lng"));
            route.createEndPoint(
                    jsonEndLocation.getDouble("lat"),
                    jsonEndLocation.getDouble("lng"));
            route.setEndPlaceName(jsonLeg.getString("end_address"));
            route.setStartPlaceName(jsonLeg.getString("start_address"));
            route.setOriginEndPlace(destination);
            route.setOriginStartPlace(origin);

            route.setRoutePoints(PolyUtil.decode(overview_polylineJson.getString("points")));

            routes.add(route);
        }

        listener.onSucceedFindDirection(routes);
        listener.onStoreFindDirection();
    }
}
