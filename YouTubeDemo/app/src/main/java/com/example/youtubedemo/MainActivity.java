package com.example.youtubedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class MainActivity extends YouTubeBaseActivity {

    YouTubePlayerView myPlayer;
    Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialise the variables
        playButton = findViewById(R.id.youtube_bn);
        myPlayer = findViewById(R.id.videoView);

        //listens for a successful return of the video content
        final YouTubePlayer.OnInitializedListener onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                //watch it is lowercase y.
                youTubePlayer.loadVideo("3LiubyYpEUk");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        //plays the video
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               myPlayer.initialize(YoutubeConfig.API_KEY, onInitializedListener);
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
    }
}