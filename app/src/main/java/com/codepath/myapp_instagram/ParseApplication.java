package com.codepath.myapp_instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {



    @Override
    public void onCreate() {

        super.onCreate();
        ParseObject.registerSubclass( Post.class );


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(" instagram-huguens")
                .clientKey("codepath-instagram")
                .server("http://instagram-huguens.herokuapp.com/parse").build());
    }
}





