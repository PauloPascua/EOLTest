package paulopascua.com.eoltest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Paulo on 2017-02-19.
 */

public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
    static URL imageUrl;

    public LoadImageTask(URL imageUrl) {
        this.imageUrl = imageUrl;
    }

    public interface Listener {
        void onImageLoaded(Bitmap bitmap);
        void onError();
    }

    private Listener mListener;

    @Override
    protected Bitmap doInBackground(String... args) {
        try {
            return BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            mListener.onImageLoaded(bitmap);
        } else {
            mListener.onError();
        }
    }
}
