package Deepak.Thakur.com.flickpick.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import Deepak.Thakur.com.flickpick.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Splash extends AppCompatActivity {

    CountDownTimer mCountdown;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mCountdown = new CountDownTimer(2500, 200) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFinish() {
               fadeOut();
               finish();
            }
        };
        mCountdown.start();

    }
    private void fadeOut() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    @Override
    public void onBackPressed() {

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
