package lyjak.anna.inzynierka.viewmodel.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import lyjak.anna.inzynierka.viewmodel.utils.GoogleMapsStaticUtil;

/**
 * Created by Anna on 18.11.2017.
 */

public class PointImageFromUrlAsyncTask extends AsyncTask<LatLng, Void, Bitmap> {

    private ImageView imageView;

    public PointImageFromUrlAsyncTask(ImageView imageView) {
        this.imageView = imageView;
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
            imageView.setImageBitmap(bmp);
        }
    }
}




