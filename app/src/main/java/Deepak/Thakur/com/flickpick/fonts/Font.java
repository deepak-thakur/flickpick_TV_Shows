package Deepak.Thakur.com.flickpick.fonts;

import android.app.Application;

import Deepak.Thakur.com.flickpick.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;



public class Font extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/got.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
