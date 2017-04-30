package mbds.nfc.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import mbds.nfc.R;
import mbds.nfc.utils.ImageLoader;

/**
 * Created by Joe on 19/03/2017.
 */

public class WorkerSplashActivity extends SplashActivity {

    private static final String IMAGE_URL = "";

    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageLoader = new ImageLoader();
        mImageLoader.execute(IMAGE_URL);

        ImageView imageView = (ImageView) findViewById(R.id.imageView4);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
        Glide.with(this).load(R.drawable.chargement).into(imageViewTarget);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mImageLoader.getStatus() != AsyncTask.Status.FINISHED) {
            mImageLoader.cancel(true);
        }
    }

}
