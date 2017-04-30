package mbds.nfc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import mbds.nfc.MainActivity;
import mbds.nfc.R;
import mbds.nfc.utils.SessionManager;

public class SplashActivity extends Activity {
    private static final long SPLASH_DURATION = 5000L;

    private Handler mHandler;
    private Runnable mRunnable;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new SessionManager(getApplicationContext());
        boolean b = session.isLoggedIn();
        //System.out.println(" erreur 1 ========================= mandalo eto");
        if(b)
        {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            Log.e(" erreur 2", "mandalo 2");
        }
        //System.out.println(" erreur 1 ========================= mandalo eto");
        mHandler = new Handler();


        ImageView imageView = (ImageView) findViewById(R.id.imageView4);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
        Glide.with(this).load(R.drawable.chargement).into(imageViewTarget);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                dismissSplash();
            }
        };

        // allow user to click and dismiss the splash screen prematurely
        View rootView = findViewById(android.R.id.content);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSplash();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, SPLASH_DURATION);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    private void dismissSplash() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}

