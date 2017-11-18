package lyjak.anna.inzynierka.viewmodel.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import lyjak.anna.inzynierka.service.utils.GoogleMapsStaticUtil;

/**
 * Created by Anna on 18.11.2017.
 */

public class PolylineImageFromUrlAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView picture;

    public PolylineImageFromUrlAsyncTask(ImageView picture) {
        this.picture = picture;
    }

    @Override
    protected Bitmap doInBackground(String... points) {
        return GoogleMapsStaticUtil.getGoogleMapStaticPictureWithPolyline(points[0]);
    }

    protected void onPostExecute(Bitmap bmp) {
        if (bmp != null) {
            picture.setImageBitmap(bmp);
        }
    }
}
