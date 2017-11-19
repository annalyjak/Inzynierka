package lyjak.anna.inzynierka.viewmodel.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import lyjak.anna.inzynierka.service.model.utils.GoogleMapsStaticUtil;

/**
 * Created by Anna on 18.11.2017.
 */


public class PolylineImageFromUrlAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;

    public PolylineImageFromUrlAsyncTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... points) {
        return GoogleMapsStaticUtil.getGoogleMapStaticPictureWithPolyline(points[0]);
    }

    protected void onPostExecute(Bitmap bmp) {
        if (bmp != null) {
            imageView.setImageBitmap(bmp);
        }
    }
}