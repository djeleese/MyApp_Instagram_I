package com.codepath.myapp_instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private EditText etDescription;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;
    public String photoFileName = "photo.jpg";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private File photoFile;

    ProgressBar pb ;

    //Uri img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        etDescription = findViewById( R.id.etDescription );
        btnCaptureImage = findViewById( R.id.btnCaptureImage );
        ivPostImage = findViewById( R.id.ivPostImage );
        btnSubmit = findViewById( R.id.btnSubmit );
        pb = (ProgressBar) findViewById(R.id.pbLoading);
        btnCaptureImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchCamera();
            }
        } );
       // queryPosts();
        btnSubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description= etDescription.getText().toString();
                ParseUser user=ParseUser.getCurrentUser();
                if (photoFile == null||ivPostImage.getDrawable()==null) {
                    Log.d( TAG, "There is no pic" );
                    Toast.makeText( MainActivity.this,"There is no pic",Toast.LENGTH_SHORT ).show();
                }
                savePost(description, user, photoFile);

            }
        } );
    }

    private void LaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri( photoFileName );

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile( MainActivity.this, "com.codepath.fileprovider", photoFile );
        intent.putExtra( MediaStore.EXTRA_OUTPUT, fileProvider );
        //img=fileProvider;
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity( getPackageManager() ) != null) {
            // Start the image capture intent to take photo
            startActivityForResult( intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE );
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                //Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName));
// by this point we have the camera photo on disk
                Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
// See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 200);

                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                //ImageView ivPreview = (ImageView) findViewById(R.id.ivPostImage);
                ivPostImage.setImageBitmap(resizedBitmap);

                //ivPostImage.setImageURI( img );
            } else
                { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    // Returns the File for a photo stored on disk given the fileName
        public File getPhotoFileUri(String fileName){
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File( getExternalFilesDir( Environment.DIRECTORY_PICTURES ), TAG );

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d( TAG, "failed to create directory" );
            }

            // Return the file target for the photo based on filename
            File file = new File( mediaStorageDir.getPath() + File.separator + fileName );

            return file;
        }


    private void savePost(String description, ParseUser parseUser, File photoFile){
        Post post=new Post();
        post.setKeyDescription(description);
        post.setUser(parseUser);
        post.setKeyImage( new ParseFile( photoFile));


        pb.setVisibility(ProgressBar.VISIBLE);

        post.saveInBackground( new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.d( TAG, "Error saving" );
                    e.printStackTrace();
                    return;
                }
                // on some click or some loading we need to wait for...

// run a background job and once complete

                Log.d( TAG, "Succes" );
                pb.setVisibility(ProgressBar.INVISIBLE);
                etDescription.setText("");
                ivPostImage.setImageResource(0);

            }
        } );

    }


        private void queryPosts(){
            final ParseQuery<Post> postParseQuery = new ParseQuery<Post>( Post.class );
            postParseQuery.include( Post.KEY_USER );
            postParseQuery.findInBackground( new FindCallback<Post>() {

                @Override
                public void done(List<Post> posts, ParseException e) {
                    if (e != null) {
                        Log.e( TAG, "Error with Query" );
                        e.printStackTrace();
                        return;
                    }
                    for (int i = 0; i < posts.size(); i++) {
                        Post post = posts.get( i );
                        Log.d( TAG, "Post:" + post.getKeyDescription() + ", username:" + post.getUser().getUsername() );
                    }


                }
            } );
        }
    }

