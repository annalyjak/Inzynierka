package lyjak.anna.inzynierka.viewmodel.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import lyjak.anna.inzynierka.service.utils.GoogleMapsStaticUtil;

/**
 * Created by Anna on 18.11.2017.
 */

public class PointImageFromUrlAsyncTask extends AsyncTask<LatLng, Void, Bitmap> {

    private ImageView picture;

    public PointImageFromUrlAsyncTask(ImageView picture) {
        this.picture = picture;
    }

    @Override
    protected Bitmap doInBackground(LatLng... points) {
        return GoogleMapsStaticUtil.getGoogleMapStaticPicture(
                points[0].latitude,
                points[0].longitude
        );
    }

    protected void onPostExecute(Bitmap bmp) {
        if (bmp!=null) {
            picture.setImageBitmap(bmp);
        }
    }
}