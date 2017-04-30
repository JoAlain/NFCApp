package mbds.nfc.activity;

import android.os.AsyncTask;
import android.os.Bundle;

import mbds.nfc.utils.ImageLoader;

/**
 * Created by Joe on 14/03/2017.
 */

public class SplashScreenActivity extends SplashActivity{

    private static final String IMAGE_URL = "";

    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageLoader = new ImageLoader();
        mImageLoader.execute(IMAGE_URL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mImageLoader.getStatus() != AsyncTask.Status.FINISHED) {
            mImageLoader.cancel(true);
        }
    }
}
