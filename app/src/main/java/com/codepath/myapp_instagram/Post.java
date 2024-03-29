package com.codepath.myapp_instagram;


import android.widget.ProgressBar;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.security.Key;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String  KEY_DESCRIPTION="description";
    public static final String  KEY_USER="user";
    public static final String  KEY_IMAGE="image";


    // Ensure that your subclass has a public default constructor

    public String getKeyDescription() {

        return getString(KEY_DESCRIPTION);
    }

    public void setKeyDescription(String description) {
        put(KEY_DESCRIPTION,description);
    }
public ParseFile getImage(){
return getParseFile( KEY_IMAGE );
}
public void setKeyImage(ParseFile parseFile){
        put( KEY_IMAGE, parseFile );
}
public ParseUser getUser(){
        return getParseUser( KEY_USER );
}
public void setUser(ParseUser parseUser){
        put( KEY_USER, parseUser );
}


}