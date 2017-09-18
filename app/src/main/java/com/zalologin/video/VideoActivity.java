package com.zalologin.video;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.zalologin.R;

/**
 * //Todo
 * <p>
 * Created by HOME on 9/6/2017.
 */

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        VideoView videoView =(VideoView)findViewById(R.id.videoView1);

        //Creating MediaController
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);

        try {
            //set the media controller in the VideoView
            videoView.setMediaController(mediaController);
            //set the uri of the video to be played
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.beautiful_girl));
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


//        //specify the location of media file
//        Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/media/1.mp4");

        //Setting MediaController and URI, then starting the videoView
//        videoView.setMediaController(mediaController);
//        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
    }
}
