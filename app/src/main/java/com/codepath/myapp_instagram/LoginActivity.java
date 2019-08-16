package com.codepath.myapp_instagram;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText etUserName;
    private EditText etPassword;
    private Button btnLogin;

    public static final String TAG="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        etUserName = findViewById( R.id.etUserName );
        etPassword = findViewById( R.id.etPassword );
        btnLogin = findViewById( R.id.btnLogin );
        btnLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUserName.getText().toString();
                String Password = etPassword.getText().toString();
                login( username, Password );

            }

        } );


    }
    private void login(String username, String password) {
        ParseUser.logInInBackground( username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e !=null){
                    Log.e(TAG, "Issue with login" );
                    e.printStackTrace();
                    return;
                }
                goMainActivity();

            }
        } );

        }

    private void goMainActivity() {
        Intent i= new Intent( this, MainActivity.class) ;
        startActivity(i);
        finish();
    }
}



