package lyjak.anna.inzynierka.service.respository;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lyjak.anna.inzynierka.viewmodel.listeners.FindDirectionListener;
import lyjak.anna.inzynierka.viewmodel.others.RouteBeetweenTwoPointsDTO;

/**
 *
 * Created by Anna on 26.12.2016.
 */

public class RoadFinder {

    private static final String URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_KEY = "";
    private FindDirectionListener listener;
    private LatLng origin; // start's point
    private LatLng destination; // destination point
    private String url; // road's url between two points
    private DownloadRawData temp;

    public RoadFinder(FindDirectionListener listener, LatLng start, LatLng end) {
        this.listener = listener;
        this.origin = start;
        this.destination = end;
    }

    public void execute() throws UnsupportedEncodingException{
        listener.onStartFindDirection();
        temp  = new DownloadRawData();
        if(url != null){
            Log.i("", url);
            temp.execute(url);
        }
        else {
            Log.i("", createUrl());
            temp.execute(createUrl());
        }
    }

    /*
    Method returns url (string of url) between two points in the GoogleMaps
     */
    private String createUrl() throws UnsupportedEncodingException {
        String urlOrigin = origin.latitude + "," + origin.longitude; // URLEncoder.encode(origin, "utf-8");
        String urlDestination = destination.latitude + "," + destination.longitude; // URLEncoder.encode(destination, "utf-8");
        return URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination +
                "&key=" + GOOGLE_KEY;
    }

    /*
    Method returns actuall status of private DownloadRawData task
     */
    public AsyncTask.Status getStatus(){
        return temp.status;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        protected AsyncTask.Status status; // auxiliary variable of DownloadRowData AsyncTask status
        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
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

        List<RouteBeetweenTwoPointsDTO> routes = new ArrayList<RouteBeetweenTwoPointsDTO>();
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
            route.createStartPoint(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
            route.createEndPoint(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
            route.setEndPlaceName(jsonLeg.getString("end_address"));
            route.setStartPlaceName(jsonLeg.getString("start_address"));
            route.setOriginEndPlace(destination);
            route.setOriginStartPlace(origin);

            route.setRoutePoints(decodePolyLine(overview_polylineJson.getString("points")));

            routes.add(route);
        }

        listener.onSucceedFindDirection(routes);
        listener.onStoreFindDirection();
    }

    /*
    Method changes string of Polyline to list of LatLng
    return list of latlng between two points
     */
    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}
